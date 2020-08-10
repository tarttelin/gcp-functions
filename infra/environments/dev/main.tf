provider "google" {
  project = var.project
  version = "~> 3.29"
  region  = var.region
  zone    = "${var.region}-a"
}

module "inbound_feed_bucket" {
  source = "../../modules/storage_bucket"
  destroy_buckets = var.destroy_buckets
  name = "inbound-feed-bucket"
}

module "error_bucket" {
  source = "../../modules/storage_bucket"
  destroy_buckets = var.destroy_buckets
  name = "inbound-errors-bucket"
}

module "bigquery" {
  source = "../../modules/bigquery"
}

module "streaming_success_queue" {
  source = "../../modules/pubsub"
  name = "streaming-success-topic"
  region = var.region
}

module "streaming_error_queue" {
  source = "../../modules/pubsub"
  name = "streaming-errors-topic"
  region = var.region
}

module "static_ip" {
  source = "../../modules/static_ip"
  region = var.region
  project = var.project
}
