
resource "google_storage_bucket" "ingest" {
  name 		= var.name
  location	= "EU"
  force_destroy = var.destroy_buckets
}