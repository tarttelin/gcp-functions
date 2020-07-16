resource "google_pubsub_topic" "streaming_success" {
  name = var.name

  message_storage_policy {
    allowed_persistence_regions = [
      var.region,
    ]
  }
}