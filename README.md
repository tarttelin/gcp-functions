# Sample cloud function

## Tools Required

* Java 11 : `java --version`
* gcloud : `gcloud --version`
* git : `git --version`
* Any latest IDE (IntelliJ Community Edition or Eclipse)

## Prerequisites

You'll need a user account on GCP and ideally a fresh project so you can throw it away once done with playing.

- Login and configure `gcloud` commandline as described in last section of this document
- Enable cloud function api
- Enable code build api

terraform 0.12.x
create a bucket for the terraform config to go in:

    gcloud mb gs://<PROJECT_ID>-tfstate

## Build

    ./gradlew clean build
    
## Run locally

    ./gradlew runFunction -PrunFunction.target=com.pyruby.cloudfunc.MetricsLoggerFunction -PrunFunction.port=8080
    
## gcloud deploy, list, describe and delete 

#### Deploy

    ./gradlew clean deployable
    
    gcloud functions deploy my-first-function --entry-point=com.pyruby.cloudfunc.MetricsLoggerFunction --runtime=java11 --trigger-http --memory=256MB --allow-unauthenticated --source=build/deployable
    
#### List

    gcloud functions list

#### Describe and Test

    gcloud functions describe my-first-function
    
    curl 'https://GCP_REGION-PROJECT_ID.cloudfunctions.net/my-first-function'

#### Delete

    gcloud functions delete my-first-function
    
## gcloud logs

    gcloud functions logs read my-first-function
    
    
## gcloud configuration commands

#### Update gcloud components:
    
    gcloud components update

#### Login to gcloud:

    gcloud auth login

#### Print access token:

    gcloud auth print-identity-token
    
#### List auth:

    gcloud auth list

#### Logout from gcloud:
    
    gcloud auth revoke

#### Set default project-id in gcloud:

    gcloud config set project crucial-module-283212

#### Unset default project in gcloud:
    
    gcloud config unset project