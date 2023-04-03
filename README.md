# certeasy
The easiest certificate authority for development and experiments


## Why?
Security Analysts expect client-server communication to be secured by TLS.
But How can we expect engineers to use TLS in production if they never used it in their very own localhost? But why haven’t they? Because it’s not straightforward. Setting up a CA and issuing certificates is certainly painful at this point. We must make it easier for engineers to use TLS in software they build and deploy locally. To make it easier we MUST simplify the issuance of certificates.


## Goals
* Make it easier to issue certificates without understanding the X509 specification
* Provide a self hosted easy to use certificate authority software to be used for experiments and testing in general.


## Features

* RESTFul API
* Web GUI
* Predefined types of certificates, encapsulating X509 complexity
* Exporting certificates in PEM Format

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
| ./.github | Contains Github configuration files | Yaml  |
| ./certeasy-core | Defines the core concepts related to certificates issuance | Java  |
| ./certeasy-bouncycastle | Implements a certificate generator using bouncycastle library | Java |
| ./certeasy-backend-app | Implements all the functionality and exposes a Restful API | Java/Quarkus |
| ./certeasy-frontend-app | Implements the certeasy web GUI | ReactJS |
| ./site | The Website of the project | ReactJS |


## Help wanted
We need help with the following work:

* Project website
* Frontend app


## Contributions
Yet to be documented