package com.flutter.liabilitycalc.generator;

import com.flutter.gbs.bom.proto.BetOuterClass;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Bet Generator Class
 * This class generates new Bets and places them onto the Kafka Topic
 */
public class BetGenerator {

    private static final long DELAY = 1000;  // Wait 1 second between bets

    public static void main(String[] args) throws InterruptedException {

        final ParameterTool params = ParameterTool.fromArgs(args);

        String topic = params.get("topic", "betstream");

        Properties kafkaProps = createKafkaProperties(params);
        try (KafkaProducer<String, byte[]> producer = new KafkaProducer<>(kafkaProps)) {

            // Generate 10 sets of 10 bets with the same selection IDs
            for (int i = 0; i < 10; i++) {
                BetIterator betIterator = new BetIterator(10, i);
                while (betIterator.hasNext()) {
                    pushBetToProducer(topic, producer, betIterator);
                }
            }

            // This will generate infinite stream of bets with varying ids
//            BetIterator betIterator = new BetIterator();
//            while (betIterator.hasNext()) {
//                pushBetToProducer(topic, producer, betIterator);
//            }
        }
    }

    private static void pushBetToProducer(String topic, KafkaProducer<String, byte[]> producer, BetIterator betIterator) throws InterruptedException {
        BetOuterClass.Bet bet = betIterator.next();
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, bet.toByteArray());
        producer.send(record);
        //noinspection BusyWait
        Thread.sleep(DELAY);
    }

    private static Properties createKafkaProperties(final ParameterTool params) {
        //noinspection DuplicatedCode
        String brokers = params.get("bootstrap.servers", "localhost:9092");
        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
        return kafkaProps;
    }
}
