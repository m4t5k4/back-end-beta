# Tweede microservice met MongoDB

![Test, Build and Upload artifact](https://github.com/m4t5k4/back-end-beta/workflows/Test,%20Build%20and%20Upload%20artifact/badge.svg)

## Docker setup

docker run -d -p 27017-27019:27017-27019 --name mongodb mongo

troubleshooting:

- cli: https://docs.mongodb.com/manual/mongo/
- Compass: mongodb://localhost:27017/

## Spring application properties

- spring.data.mongodb.port= ${MONGODB_PORT:27017}
- server.port=8052
