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

### Starting the Playground

Once you built the Docker image, run the following command to start the playground

```bash
docker-compose up -d
```

You can check if the playground was successfully started by accessing the WebUI of the Flink cluster.

Connect to localhost:<port>, where <port> is the port mapped to 8081 in the jobmanager container.
To check which port it is bound to on your local machine, run the following command

```bash
docker-compose ps
```

### Stopping the Playground

To stop the playground, run the following command

```bash
docker-compose down
```

### Scaling the cluster

Bring down the playground using the commands above. Then run the following command to bring it up with multiple jobmanagers and taskmanagers

```bash
docker-compose up -d --scale jobmanager=3 --scale taskmanager=3
```

The cluster should come up, and the WebUI should be accessible via any of the jobmanagers that you created.

### Observing the output

You can observe the input and output kafka topics on [localhost:8080](http://localhost:8080) or by connecting a kafka consumer to topic "output" on localhost:9094.

### Testing high availability

Figure out which of the jobmanagers is the current cluster leader, then kill it using docker desktop UI. You should see that the output continues to produce and no data is lost.