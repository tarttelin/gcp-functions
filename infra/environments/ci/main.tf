provider "google" {
  project = var.project
  version = "~> 3.29"
  region  = var.region
  zone    = "${var.region}-a"
}

provider "google-beta" {
  project = var.project
  region = "${var.region}-a"
}

module "build_artifacts_bucket" {
  source = "../../modules/web_bucket"
  destroy_buckets = var.destroy_buckets
  name = "ons-build-artifacts"
}

module "build_result_pubsub" {
  source = "../../modules/pubsub"
  name = "cloud-builds"
  region = var.region
}

resource "google_cloudbuild_trigger" "dataflow-trigger" {
  provider = "google-beta"
  description = "Push to master"
  github {
    owner = var.github_org
    name = "activity-pipeline"
    push {
      branch = "^master$"
    }
  }
  filename = "cloudbuild.yaml"
}