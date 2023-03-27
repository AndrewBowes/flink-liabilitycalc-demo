# Foundation

This demo is adapted from the "Flink Operations Playground" demo that can be found [here](https://nightlies.apache.org/flink/flink-docs-master/docs/try-flink/flink-operations-playground/)

## Setup

This demo is based around a custom Docker image, as well as public images for Flink, Kafka, and ZooKeeper.

### Building the custom Docker image

Build the Docker image by running

```bash
docker-compose build
```

### Preparing the Checkpoint and Savepoint Directories

Create the checkpoint and savepoint directories on the Docker host machine (these volumes are mounted by the jobmanager and taskmanager, as specified in docker-compose.yaml):

```bash
mkdir -p /tmp/flink-checkpoints-directory
mkdir -p /tmp/flink-savepoints-directory
```

### Starting the Demo

Once you've built the Docker image, run the following command to start the demo

```bash
docker-compose up -d
```

You can check if the demo was successfully started by accessing the WebUI of the Flink cluster.

Connect to [localhost:8081](http://localhost:8081) and [localhost:8082](http://localhost:8082)
You should see the flink UI on both of them. They should show the same information. This is the expected behaviour for the HA cluster.

You can observe the input and output kafka topics on [localhost:8080](http://localhost:8080) or by connecting a kafka consumer to topic "output" on localhost:9094.

### Testing high availability

Figure out which of the jobmanagers is the current cluster leader, then kill it using docker desktop UI. You should see that the output continues to produce and no data is lost.

The easiest way to determine the cluster leader is to look at the jobmanager logs.

```bash
docker logs -f flink-ha-demo-jobmanager-1-1
docker logs -f flink-ha-demo-jobmanager-2-1
```

### Stopping the Demo

To stop the demo, run the following command

```bash
docker-compose down -v
```

