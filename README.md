# Java Backend Home Project
this project is made by these framework
1. [Dropwizard - web framework](https://www.dropwizard.io/1.3.5/docs/getting-started.html)
2. [Dropwizard-auth-jwt - jwt auth module](https://github.com/ToastShaman/dropwizard-auth-jwt)
3. [JOOQ - sql api](https://www.jooq.org/)
4. [Dagger - dependency injection](https://github.com/google/dagger)
5. [Redisson - redis java client](https://github.com/redisson/redisson)


System component:
1. redis - cache
2. mysql - database

## Important
Before starting to develop, please `Fork` this project to your github account, when you finished please send the `Forked` repo link back



## What you have to implement
This is a simple bank service, let admin manage customer's wallet with transfer/credit/view operation,
the needed schema will generate automatically when you bootstrap your docker mysql image, please to see `docker-compose.yml` for detail configurations.
1. admin login logic
2. save admin login token and needed profile information into redis (something maybe you can write into JWT token)
3. authentication
4. authorization
5. user wallet transaction
    1. transfer (should add transaction log)
    2. credit/debit  (should add transaction log)
    3. view transaction log
    
P.S. All the task you have to do is already comment with 
```
// TODO
```

## Environment setting
1. IntelliJ IDE (recommend)
2. Docker & Docker compose (must)
3. Postman (recommend)
4. JAVA 8 (must)

## How to run
* run docker compose in terminal
```
docker-compose up -d
```
* run project with your run configuration, pass `server` in program argument (please see dropwizard launch configuration)
* call API with postman to verify your code works very well

## Initial Data
Admin

| account  | password |
|----------|----------|
| root     | password |
| operator | password |
| manager  | password |

User

| id | amount |
|----|--------|
| 1  | 10000  |
| 2  | 10000  |

## Hint

* It's suggested to debug with ObjectMapper and Logger (like slf4j)
* If you don't get familiar with the execution way of IntelliJ, you can use CLI to build and execute your code:

```
mvn clean package
java -jar target/bank-1.0-shaded.jar server
```

* Please trust the autocomplete provided by your IDE, you can find many hints.

* As for Jooq, the basic code usage can be: (BIG HINT)

```
DSL.using(configuration). ...
```

Please use this code segment and your IDE autocomplete to find the method you should use. (Of course, please read document if you need.)

* Please finish this project if you can. But don't worry if the project is not completed, you can share the parts which you finished with us.
* Welcome to share your idea and ask questions to us, don't be shy.
