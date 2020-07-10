
resource "google_storage_bucket" "ingest" {
  name 		= "inbound-feed-bucket"
  location	= "EU"
  force_destroy = var.destroy_buckets
}