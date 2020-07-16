terraform {
  backend "gcs" {
    bucket = "sandpit-282515-tfstate"
    prefix = "env/ci"
  }
}