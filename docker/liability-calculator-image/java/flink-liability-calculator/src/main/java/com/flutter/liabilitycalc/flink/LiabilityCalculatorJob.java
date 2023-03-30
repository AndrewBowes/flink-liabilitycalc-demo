package com.flutter.liabilitycalc.flink;

import com.flutter.gbs.bom.proto.BetOuterClass;
import com.flutter.liabilitycalc.records.BetDeserializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.playgrounds.ops.clickcount.records.ClickEvent;
import org.apache.flink.playgrounds.ops.clickcount.records.ClickEventDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * Flink Job which will process all Bets from an incoming Kafka Stream.
 */
public class LiabilityCalculatorJob {


    public static void main(String[] args) {
        final ParameterTool params = ParameterTool.fromArgs(args);

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        configureEnvironment(params, env);

        String inputTopic = params.get("input-topic", "betstream");
        String outputTopic = params.get("output-topic", "liabilities");
        String brokers = params.get("bootstrap.servers", "localhost:9092");
        Properties kafkaProps = new Properties();
        kafkaProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "click-event-count");

        KafkaSource<BetOuterClass.Bet> source = KafkaSource.<BetOuterClass.Bet>builder()
                .setTopics(inputTopic)
                .setValueOnlyDeserializer(new BetDeserializationSchema())
                .setProperties(kafkaProps)
                .build();

//        DataStream<BetOuterClass.Bet> bets = env.fromSource(source, ,"BetStream");

    }




}
