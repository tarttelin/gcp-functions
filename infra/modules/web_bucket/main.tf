

resource "google_storage_bucket_access_control" "public_rule" {
  bucket = google_storage_bucket.public.name
  role   = "READER"
  entity = "allUsers"
}

resource "google_storage_bucket" "public" {
  name 		= var.name
  location	= "europe-west2"
  force_destroy = var.destroy_buckets

  website {
    main_page_suffix = "index.html"
    not_found_page   = "404.html"
  }
}