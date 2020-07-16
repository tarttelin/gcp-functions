package com.pyruby.cloudfunc;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.Table;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.storage.contrib.nio.CloudStorageFileSystem;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class StreamFromBucket implements BackgroundFunction<GcsEvent> {

    private final Gson gson = new Gson();

    @Override
    public void accept(GcsEvent gcsEvent, Context context) throws Exception {
        try(CloudStorageFileSystem fs = CloudStorageFileSystem.forBucket(gcsEvent.getBucket())) {
            Path path = fs.getPath(gcsEvent.getName());
            Stream<String> lines = Files.lines(path);
            BigQuery qry = BigQueryOptions.getDefaultInstance().getService();
            Table table = qry.getTable("hello_dataset", "person");
            Stream<InsertAllRequest.RowToInsert> stream = lines
                    .map(this::convert)
                    .filter(Objects::nonNull)
                    .map(InsertAllRequest.RowToInsert::of);
            InsertAllResponse insert = table.insert(stream::iterator);
            if (insert.hasErrors()) {
                System.out.println("Error: " + gson.toJson(insert.getInsertErrors()));
            } else {
                System.out.println("Successfully wrote to bigquery");
            }
        }
        registerSuccess(gcsEvent.getName());
    }

    private void registerSuccess(String filename) throws InterruptedException, IOException {
        Publisher publisher = null;
        TopicName name = TopicName.of("sandpit-282515", "streaming-success-topic");
        try {
            publisher = Publisher.newBuilder(name).build();
            ByteString data = ByteString.copyFromUtf8(String.format("file: %s uploaded successfully", filename));
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<>() {
                public void onSuccess(String messageId) {
                    System.out.println("published with message id: " + messageId);
                }

                public void onFailure(Throwable t) {
                    System.out.println("failed to publish: " + t);
                }
            }, MoreExecutors.directExecutor());
        } finally {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

    public Map<String, ?> convert(String entry) {
        return gson.fromJson(entry, Map.class);
    }
}
