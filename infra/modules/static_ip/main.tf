resource "google_compute_network" "cloud-vpc" {
  name = "midl-cloud-vpc"
  auto_create_subnetworks = false
  routing_mode = "REGIONAL"
  project = var.project
}

resource "google_compute_subnetwork" "subnet" {
  name          = "my-subnetwork"
  network       = google_compute_network.cloud-vpc.self_link
  ip_cidr_range = "10.0.0.0/24"
  region        = var.region
  project       = google_compute_network.cloud-vpc.project
}

resource "google_compute_address" "midl-cloud-nat-address" {
  name    = "cloud-nat-ip"
  project = google_compute_network.cloud-vpc.project
  region  = google_compute_subnetwork.subnet.region
}

resource "google_compute_router" "cloud-router" {
  name    = "midl-router"
  project = google_compute_network.cloud-vpc.project
  region  = google_compute_subnetwork.subnet.region
  network = google_compute_network.cloud-vpc.self_link
}

resource "google_compute_router_nat" "nat" {
  name                               = "cloud-nat-router"
  project                            = google_compute_network.cloud-vpc.project
  router                             = google_compute_router.cloud-router.name
  region                             = google_compute_router.cloud-router.region
  nat_ip_allocate_option             = "MANUAL_ONLY"
  source_subnetwork_ip_ranges_to_nat = "ALL_SUBNETWORKS_ALL_IP_RANGES"
  nat_ips                            = [google_compute_address.midl-cloud-nat-address.self_link]

  log_config {
    enable = true
    filter = "ERRORS_ONLY"
  }
}

resource "google_vpc_access_connector" "connector" {
  name          = "cloud-vpc-connector"
  project       = google_compute_network.cloud-vpc.project
  region        = google_compute_router.cloud-router.region
  ip_cidr_range = "10.10.0.0/28"
  network       = google_compute_network.cloud-vpc.name
}

output "ip_address" {
  value = google_compute_address.midl-cloud-nat-address.address
}