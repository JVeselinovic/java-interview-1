## Java Interview
## Introduction
This is a microservice that is responsible for managing biological samples sent by patients to Delfi Diagnostics'. As a
company, one of Delfi's main product is a diagnostic for cancer. A patient's blood is drawn by a phlebotomist and sent to
Delfi's lab. This biological sample is analyzed by Delfi to determine if the patient has cancer or not. The result is
sent back to the patient's physician.

The main responsibility of this microservice is to manage the lifecycle of the patient's biological sample. It exposes
various REST endpoints that make up its contract for any calling service. For the purpose
of this exercise it has been simplified to support the following functionality:

1. Authorized users can retrieve a paged list of all subjects
2. Authorized users can retrieve a particular subject based on its id
3. Authorized users can retrieve a particular subject based on its external id (an id given to the subject by our customer) 
4. Authorized users can retrieve a paged list of all samples
5. Authorized users can retrieve a particular sample based on its id
6. Authorized users can accession a sample. This involves creating a subject, and their associated samples

## Schema
The database is in Postgres and there are only two tables in the database. 
```mermaid
erDiagram
Subject |--|{ Sample : has  
```
A `Subject` has 1 or more `Sample`s. `Subject` has the following properties:
* id: numeric (primary key)
* external_id: varchar
* create_dt: timestamp
* create_by: varchar
* mod_dt: timestamp
* mod_by: varchar

`Sample` has the following properties:
* id: numeric
* sample_type_id: varchar
* collection_date: timestamp
* subject_id: numeric (foreign key to subject.id)
* create_dt: timestamp
* create_by: varchar
* mod_dt: timestamp
* mod_by: varchar
 
## System Architecture
The system is built-in on [Micronaut](https://micronaut.io/) framework. The system borrows principles from Domain-Driven Design 
an as such is structured along its two main domain models:
* sample
* subject

Each domain contains:
* A Feature class which contains all the business logic associated with the domain
* One or more model classes that represent DTOs and/or the database entity
* One or more repository classes that use JPA/Hibernate to interact with the database
* One or more controller classes that expose the REST endpoints and enforce authorization rules

```mermaid
flowchart LR;
User((User)) --|Auth|--> Auth[Auth]
User((User)) <-.|JWT Token|.- Auth[Auth]
subgraph service
User((User)) --|GET. Authorization: Bearer JWT Token|--> Ctrl[Controller]
Ctrl[Controller] --|Verifies token|--> Auth[Auth]
Ctrl[Controller] --> Ft[Feature]
Ft[Feature] --> Repo[Repository]
end
Repo[Repository] --> DB[(DB)]
Ctrl[Controller] --> User((User))
style Ft stroke:#333,stroke-width:4px
```
### Flow
Here is the standard user flow for accessing the service. 
A `User` authenticates with a separate`Auth` service to get a JWT token. The token includes the permissions granted to the user.
The User then makes an HTTPS request to the service which is received by the `Controller`. The `Controller` verifies the JWT
token for authenticity using the `Auth` service. It also checks if user has appropriate permissions based on the permissions
present in the JWT token. The `Controller` deserializes the request and calls the `Feature` class which in turn handles
all the business logic. `Feature` class uses the `Repository` class to interact with the database and returns any results back 
to the `User` via the `Controller`.

## Explain the exercise
### Fix test
In this exercise we will fix a broken test
### Add a feature (Optional)
In this exercise we will implement the feature to expose a REST endpoint to accession a new sample.