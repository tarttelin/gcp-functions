resource "google_pubsub_topic" "streaming_success" {
  name = "streaming-success-topic"

  message_storage_policy {
    allowed_persistence_regions = [
      "europe-west2",
    ]
  }
}

resource "google_pubsub_topic" "streaming_errors" {
  name = "streaming-errors-topic"

  message_storage_policy {
    allowed_persistence_regions = [
      "europe-west2",
    ]
  }
}