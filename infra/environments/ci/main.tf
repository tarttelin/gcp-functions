provider "google" {
  project = var.project
  version = "~> 3.29"
  region  = var.region
  zone    = "${var.region}-a"
}

module "build_artifacts_bucket" {
  source = "../../modules/storage_bucket"
  destroy_buckets = var.destroy_buckets
  name = "ons-build-artifacts"
}

module "build_result_pubsub" {
  source = "../../modules/pubsub"
  name = "cloud-builds"
  region = var.region
}