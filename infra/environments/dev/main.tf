provider "google" {
  project = var.project
  version = "~> 3.29"
  region  = "europe-west2"
  zone    = "europe-west2-a"
}

module "ingest_bucket" {
  source = "../../modules/ingest_bucket"
  destroy_buckets = var.destroy_buckets
}

module "bigquery" {
  source = "../../modules/bigquery"
}

module "pubsub" {
  source = "../../modules/pubsub"
}