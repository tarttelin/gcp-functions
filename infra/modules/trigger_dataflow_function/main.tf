

data "google_project" "project" {
  project_id = var.project
}

resource "google_project_iam_binding" "project" {
  project = var.project
  role    = "roles/editor"

  members = [
    "serviceAccount:${data.google_project.project.number}@cloudbuild.gserviceaccount.com",
  ]
}