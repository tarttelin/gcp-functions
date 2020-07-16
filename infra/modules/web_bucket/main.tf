
resource "google_storage_bucket" "ingest" {
  name 		= var.name
  location	= "europe-west2"
  force_destroy = var.destroy_buckets

  website {
    main_page_suffix = "index.html"
    not_found_page   = "404.html"
  }
}