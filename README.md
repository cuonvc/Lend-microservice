# Lender-microservice
_______________________________________________

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
