# TAAS 2021 COURSE (BackEnd)

This Github project contains the backend developed with Java Spring for the university course TAAS (Advanced software development techniques and architectures) followed at University of Turin in 2021.

## Objectives

This backend is organized in two major microservices, User-microservice and Product-microservice, they manage the two most important parts of the project, they are interconnected using Netflix Eureka and use Netflix Zuul for api-gateway, we also use rabbitMQ as our message broker between the two services.
The project is fully deployable trough doker containers and can also be deployed on a kubernetes network, that was one of the goals of this course

## Project Overview
The project we developed trough Front-end and Back-end was a system for online second-hand trading with an approach similar to dating apps where you swipe to comunicate likes and dislikes and the app tries to find a match.
## License
[AGPL-3.0](https://choosealicense.com/licenses/agpl-3.0/)
