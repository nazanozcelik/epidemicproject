# Link Converter

Link Converter is a `RESTful Web API` that allows us to convert web URL such as mydomain.com/boutique/woman to mobile links (deeplinks) and deeplinks to web URLs.
Web apps use **URLs** and mobile apps use **deeplinks**. Both apps use links to redirect specific page locations inside the apps.
Both web URL and deeplink generates a tiny link called shortlink that matches the same reference for both of them. This provides us to retrieve the deeplinks or web URLs if they've been already created before without creating them again.

Example converted links to each other:            
**Web URL:** "https://www.mydomain.com/butik/liste/kadın"   
**Deeplink:** "ty://?Page=Home&SectionId=1"                             
**Shortlink:** "https://ty.gl/shortlink"

## Technology Used

* [Spring Boot 2.3.1](https://spring.io/projects/spring-boot)    
* [Java 8](https://www.oracle.com/tr/java/technologies/javase/javase-jdk8-downloads.html)
* [PostgreSQL](https://www.postgresql.org/download/)
* [Maven](https://maven.apache.org/download.cgi)

## Prerequisites

Before you begin, ensure you have met the following requirements:

If you will run the dockerized app:
* You have installed the [Docker](https://www.docker.com/products/docker-desktop) 
 (Window 8 and higher with enabled Hyper-V is necessity) 

If you will run the app from your local environement:
* You have jdk-8 and maven
* Pls read the technology used part

## Installing link-converter

To install link-converter project, follow these steps:

Windows:
```
git clone https://github.com/DevelopmentHiring/NazanOzcelik.git
```


## Using link-converter


**Swagger** enabled at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


This is a **dockerized** app, to run the project:
```
docker-compose up
```

At this point, if you've already running PostgreSQL on your local at 5432 port, you may encounter error like "connection refused" , since 5432 is already in use.
Make sure that before running the docker compose, stop your running PostgreSQL, docker-compose will create its own PostgreSQL listening port 5432.

**********************************************************************************************

If you just run the docker file:

```
docker run -p 8080:8080 link-converter
```
You may encounter connection refuse error since this is just running the Dockerfile, you can run the below command to make sure PostgreSQL is running on 5432:

```
docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```





To run the spring boot project without docker:

```
mvn spring-boot:run
```
You may encounter PostgreSQL connection problem from localhost, change host.docker.internal to localhost at application.properties and create the link-converter db from [pgAdmin](https://www.pgadmin.org/download/)

