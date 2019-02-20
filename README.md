# Java Backend Home Project
this project is made by these framework
1. [Dropwizard - web framework](https://www.dropwizard.io/1.3.5/docs/getting-started.html)
2. [JOOQ - sql api](https://www.jooq.org/)


## Important
Before starting to develop, please `Fork` this project to your github account, when you finished please send the `Forked` repo link back


## What you have to implement
This is a simple bank service, let admin manage customer's wallet with transfer operation,
the needed schema will generate automatically when you bootstrap your docker mysql image, please to see `docker-compose.yml` for detail configurations.
1. admin authentication
2. user wallet transfer
    
P.S. All the task you have to do is already comment with 
```
// TODO
```

## Environment setting
1. IntelliJ IDE (recommend)
2. Docker & Docker compose (must) - help you set up dependency system component
3. Postman (recommend) - help you test your restful api endpoint
4. JAVA 8 Development Kit(must)

## How to run
* install JDK 8
* install docker & docker composer, then run docker compose in CLI
```
docker-compose up -d
```
* run project with CLI in working_directory
```
mvn clean package
java -jar target/bank-1.0-shaded.jar server
```
* Import Postman setting in working_directory/postman, then call API with postman to verify your code works very well

## Initial Data
Admin

| account  | password |
|----------|----------|
| root     | password |

User

| id | amount |
|----|--------|
| 1  | 10000  |
| 2  | 10000  |

## Hint

* Please trust the autocomplete provided by your IDE, you can find many hints.

* As for Jooq, the basic code usage can be: (BIG HINT)

```
DSL.using(configuration). ...
```

Please use this code segment and your IDE autocomplete to find the method you should use. (Of course, please read document if you need.)

* Please finish this project if you can. But don't worry if the project is not completed, you can share the parts which you finished with us.
* Welcome to share your idea and ask questions to us, don't be shy.
* api flow diagram

![api flow](https://github.com/kensomanpow/java-backend-home-project/blob/master/api_flow.png)
