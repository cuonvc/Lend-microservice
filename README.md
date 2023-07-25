# Lender-microservice

## Build project with docker-compose
- Download [Docker-compose-share](https://github.com/cuonvc/Lender-microservice/blob/main/docker-compose.share.yml) file
- Edit file name to `docker-compose.yml`
- Open terminal with folder contain file
- Run the command `docker-compose up`

## Init docker compose
```sh
docker compose -f docker-compose.yml up -d
```
## Kafka command uses - kafka-server
### Move into Kafka container
```sh
docker exec -it kafka /bin/sh
``` 
### Go inside kafka installation folder
```sh
cd /opt/kafka_version/bin
``` 
### Create topic
```sh
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic topic_name
``` 
### Send messages with producer
```sh
kafka-console-producer.sh --topic topic_name --bootstrap-server localhost:9092
``` 
### Read messages with consumer
```sh
kafka-console-consumer.sh --topic topic_name --from-beginning --bootstrap-server localhost:9092
``` 
