# Sample cloud function

You'll need a user account on GCP and ideally a fresh project so you can throw it away once done playing.

- Install `gcloud` commandline.
- Enable cloud function api
- Enable code build api

terraform 0.12.x
create a bucket for the terraform config to go in:

```shell script
$ gcloud mb gs://<PROJECT_ID>-tfstate
```