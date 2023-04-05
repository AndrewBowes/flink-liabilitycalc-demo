package com.flutter.liabilitycalc.flink;

import com.flutter.gbs.bom.proto.BetOuterClass;
import com.flutter.liabilitycalc.model.MessageMeta;
import com.flutter.liabilitycalc.model.SelectionLiability;
import com.flutter.liabilitycalc.records.OutboundBetSerde;
import com.flutter.liabilitycalc.records.UnmodifiableCollectionAdapter;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;


/**
 * Flink Job which will process all Bets from an incoming Kafka Stream.
 */
public class LiabilityCalculatorJob {


    public static void main(String[] args) throws Exception {
        final ParameterTool params = ParameterTool.fromArgs(args);

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.getConfig().addDefaultKryoSerializer(
                Class.forName("java.util.Collections$UnmodifiableCollection"), UnmodifiableCollectionAdapter.class);

        String inputTopic = params.get("input-topic", "betstream");
        String outputTopic = params.get("output-topic", "liabilities");
        String brokers = params.get("bootstrap.servers", "localhost:9092");
        Properties kafkaProps = new Properties();
        kafkaProps.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "liability-calculation");

        KafkaSource<Tuple2<BetOuterClass.Bet, MessageMeta>> source = KafkaSource.<Tuple2<BetOuterClass.Bet, MessageMeta>>builder()
                .setTopics(inputTopic)
                .setDeserializer(OutboundBetSerde.deserializer())
                .setProperties(kafkaProps)
                .build();

        // TODO - Define appropriate Watermark Strategy
        DataStream<Tuple2<BetOuterClass.Bet, MessageMeta>> betMessages = env.fromSource(source, WatermarkStrategy.noWatermarks(), "BetStream");
        DataStream<BetOuterClass.Bet> bets = betMessages.map(m -> m.f0);

        // TODO - Add steps to convert the Bet to a Liability & maintain running totals.

        DataStream<SelectionLiability> liabilityDataStream = bets.map(m -> getLiability(m))
                .keyBy(v -> v.getSelectionId())
                .reduce(new SelectionLiabilityReducer());

        liabilityDataStream.print();
//        liabilityDataStream.sinkTo(KafkaSink.<SelectionLiability>builder()
//                .setKafkaProducerConfig(kafkaProps)
//                .setRecordSerializer(
//                        KafkaRecordSerializationSchema.<SelectionLiability>builder()
//                                .setValueSerializationSchema(new SelectionLiabilitySerializationSchema())
//                                .setTopic(outputTopic)
//                                .build())
//                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
//                .build());

        env.execute("Process Bets");
    }

    private static SelectionLiability getLiability(BetOuterClass.Bet bet) {
//        bet.getLe
//        Map<String, Double> mappedLiabilities = bet.getLegsList().stream()
//                .collect(Collectors.toMap(
//                        k -> k.getSelection().getId().getValue(),
//                        v -> v.getPrice().getFinal().getValue(),
//                        (m1, m2) -> m1 + m2));

        // only takes the first leg for now
        String selectionId = bet.getLegs(0).getSelection().getId().getValue();
        Double liability = bet.getLegs(0).getPrice().getFinal().getValue();

        return new SelectionLiability(selectionId, liability);
    }


}
