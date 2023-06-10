# certeasy
The easiest certificate authority for development and experiments

## Why?
Security Analysts expect client-server communication to be secured by TLS, but how can we expect engineers to use TLS in production if they never used it in their very own localhost? But why haven’t they? Because it’s not straightforward. 
Digging up openssl commands in stackoverflow is not really encouraging. Issuing test/lab certificates is certainly painful at this point. We must make it easier for engineers to use TLS in software they build and deploy locally. To make it easier we MUST simplify the issuance of certificates.

## Goals
* Make it easier to issue certificates without understanding the X509 specification
* Provide a self hosted easy to use certificate authority software to be used for experiments and testing in general.


## Features

* Well documented REST API
* User friendly Web GUI
* Predefined types/templates of certificates
* Exporting certificates and keys in PEM Format
* Import an existing CA

## Limitations

* Only a range of pre-selected attributes/extensions can be used/set on certificates
* The software makes assumptions about some certificate attributes/extensions and doesn’t allow you to change the values
* Certificates are stored on filesystem and it’s up to you to backup them up if you like
* The API and UI don’t have any authentication mechanism, because they are NOT meant for production
* You don’t get to choose the algorithm, all certificates are RSA based

## Directory structure

The cereasy project is a monorepo, with multiple sub-projects hosted under the same repo:

| Path               | Description        | Technologies |
| :---               | :---               | :---         |
| `./.github` | Contains Github configuration files | Yaml  |
| `./certeasy-core` | Defines the core concepts related to certificates issuance | Java  |
| `./certeasy-bouncycastle` | Implements a certificate generator using bouncycastle library | Java |
| `./certeasy-backend-app` | Implements all the functionality and exposes a Restful API | Java/Quarkus |
| `./certeasy-api-tests` | Implements API integration tests | Python |
| `./certeasy-frontend-app` | Implements the certeasy web GUI | Undefined |
| `./site` | The Website of the project | Undefined |


## Build & run locally

### Build Locally
You must have `Java17 JDK` installed in order to build locally.
All you need to do is to execute the `./build-local.sh` script. The script will produce a docker image tagged as `certeasy:local`.

### Run locally
Once you run the local build you can execute the certeasy headless using `docker run` as follows:
```bash
docker run -p 8080:8080 certeasy:local
```
You can access the API under `http://localhost:8080/api/`.

## Help wanted
Checkout the [Issues page](https://github.com/emjunior258/certeasy/issues) for a detailed list.

## How to contribute
Please check out the [Contributor guide](CONTRIBUTING.md).