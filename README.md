# Trec-Apps-Services

Serves as a Collection of Various Services within the Trec-Apps Microservices ecosystem, including a Discovery Service, Gateway Service,
Authentication Services, Falsehoods Service, and more.

The Structure of this Repository is that the Root Directory of this repository holds a folder called *clients* which holds all of the front-end projects relevant to Trec-Apps.
Every other folder in the root directory holds a single Server-Side Project.

**Note: These services are currently written and configured to run on local machines (i.e. localhost)**

**Note: The Gateway and Discovery service needs work before they can be used in a production environment but are currently usable in a local environment. The User Service can create and manage accounts, as well as create an OAuth2 Client. Link and Picture Services are still in development and will need to be tested!**

## Services

There are multiple Services being developed for the Trec-Apps ecosystem, with more to come. Many of these services depend on other services to run. The Ideal Order to run
these services are:

1. Discover-Service (All other services expect this service to be running)
2. Gateway-Service (Clients send request to this service, which then Routes it to Other services)
3. User-Service (Manages Users and acts as the Authentication/Resource Server for OAuth2 Authentication)
4. Resource-Service (Allows Trec-Apps "employees" to add and update various resources such as Public Figures, outlets, Regions, and Institutions)
5. Falsehood-Auth-Service (Allows users to submit Falsehoods and Review them)
6. Falsehood-Search-Service (Allows anyone to Browse various falsehoods)

Note: Angular Servers can theoretically begin running at any point as long as the Relevent Back-End Services are Running before a Front-End Request is made.

Also, These are Gradle-based projects (except where otherwise noted)!

Many of them also use Environment Variables to configure themselves. Make sure these Environment Variables are set properly before launching these services

#### Environment Variables

Here is a List of Environment Variables to set! Note: In a local envoronment, all of these variable should be set. In a Production Environment, some of these variables
Do not need to be set in every environment, depending on the service being used.

Database Variables (Note: Multiple Databases are expected to be of the same type):

* DB_URL - The URL of the First Database to use
* DB_URL_2 - The URL of the Second Database to Use (the User-Service hosts some data on a second database)
* DB_USERNAME - The Username of the First Database
* DB_USERNAME_2 - The Username of the Second Database
* DB_PASSWORD - The password for the first Database
* DB_PASSWORD_2 - The password for the Second Database
* DB_DRIVER - the class of the Database Driver
* DB_DIALECT - The Hibernate Dialect

Security Features:

* EM_USERNAME - the username of the Email account to send emails from (Note: Right now, Gmail accounts are used)
* EM_PASSWORD - The password to the email account (Note: for Gmail and many other account types, this is different from a regular Personal Password)
* TREC_PUBLIC_KEY - File Location of the Public Key used for Credential JWT Reading
* TREC_PRIVATE_KEY - File Location of the Private Key used for Credential JWT generation

Note: For Private/Public Key information, see the *Generate Keys* Section

#### Discover Service

This Service Uses Netflix-Eureka to manage a Discovery Service in a microservices ecosystem.

```
cd Trec-Apps-Services/Discover-Service
gradle build
java -jar build/libs/Discover-Service...jar (Note, use Tab to get the right jar into the command)
```

#### Gateway Service

This Service Uses Netflix-Zuul to operate as Trec-Apps' Gateway service, where Requests from clients are made here and then routed to the appropriate Server.

```
cd Trec-Apps-Services/Gateway-Service
gradle build
java -jar build/libs/Gateway-Service...
```

#### User-Service

This Service Provides Support for Managing Trec-Accounts as well as Acts as an Authorization/Resource Server for these Trec-Accounts.

```
cd Trec-Apps-Services/User-Service
gradle build
java -jar build/libs/User-Service.jar
```

Environment Variables Used:
* DB_URL
* DB_URL_2
* DB_USERNAME
* DB_USERNAME_2
* DB_PASSWORD
* DB_PASSWORD_2
* DB_DRIVER
* DB_DIALECT
* EM_USERNAME
* EM_PASSWORD
* TREC_PUBLIC_KEY
* TREC_PRIVATE_KEY

#### Resource-Service

Not to be confused with an Oauth2 Resource Server (that is the User-Service above), the Resource-Service Allows Highly Repudable Users and Trec-Apps Employees to Contribute 
Articles that can be referenced by other services like the falsehoods services.

Users of this Service are expected to not only be authenticated, but have high Privledges

```
cd Trec-Apps-Services/Resource-Service
gradle build
java -jar build/libs/Resource-Service.jar
```

Environment Variables Used:
* DB_URL
* DB_USERNAME
* DB_PASSWORD
* DB_DRIVER
* DB_DIALECT

#### Falsehood-Search

This Service Focuses on Searching for Falsehoods. Code from this [Repository](https://github.com/TrecApps/false_hood) was ported to this service as it is likely that in Production,
Most Users are going to be searching falsehoods rather than Adding to them (hence why the service is being split here.

This Service is availabe to Unauthenticated Users

```
cd Trec-Apps-Services/Falsehood-Search-Service
gradle build
java -jar build/libs/Falsehood-Search-Service.jar
```

Environment Variables Used:
* DB_URL
* DB_USERNAME
* DB_PASSWORD
* DB_DRIVER
* DB_DIALECT

#### Falsehood-Auth-Service

This Service Focuses on Submitting Falsehoods and Reviewing them, rather than searching for them. This Service, taken from this [Repository](https://github.com/TrecApps/false_hood), 
Does Require User-Service Oauth2 Authentication.

```
// Note: This Service has not yet been Compiled and is Still in Development
```

Environment Variables Used:
* DB_URL
* DB_USERNAME
* DB_PASSWORD
* DB_DRIVER
* DB_DIALECT

#### Other Services Being considered

* Picture Service - Service Managing all images in Trec-Apps, allowing for access control and content advisory
* Linking Service - Enables links in entries to websites deemed safe by Trec-Apps
* More to Come

## Clients

Right now, there are 2 Clients available in this repository. Both are Angular clients. Both Run on localhost Port 4200 (so one at a time) and are automatically
proxied to localhosts 8080 port to address CRSF issues. Running

```
ng serve
```

implicitly invokes

```
ng serve --proxy-config proxy.conf.json
```

so the former command works!

#### User-Service-Client

This Client Serves as the Front-End for the User Service. Features offered include:
* Login (Seperate from an OAuth2-based Login)
* Account Creation
* Account Management
* OAuth2 Client Management (If you get approved, You can register for your Own OAuth2 client)
* Account Validation

#### Falsehood-Client

This Client Serves as the Front-End for the Falsehood Services. While the Falsehood-backend has been split up, the Front-end is still monolithic and based on this
[Repository](https://github.com/TrecApps/falsehood_client).

## Generate Keys

You should use Openssl to generate keys. Find an appropriate directory to store your keys in.

```
openssl genrsa -out [private-key-name].pem 4096
openssl rsa -in [private-key-name].pem -pubout -out [public-key-name].pem
openssl pkcs8 -topk8 -inform PEM -in [private-key-name].pem -out [v8-private-key-name].pem -nocrypt
```

That last step is used because Openssl initially generates a _pkcs1_ formated private key and the dependencies used in reading private keys expect the key to be in _pkcs8_ .

__Also: Remove the "Begin Public Key" line and "End Public Key" line and then remove the new line characters in your PUBLIC KEY (Don't tamper with your private key).__
