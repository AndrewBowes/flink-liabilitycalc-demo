package com.flutter.liabilitycalc.records;

import com.flutter.gbs.bom.proto.BetOuterClass;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;

import javax.annotation.Nullable;

/**
 * A Kafka {@link KafkaSerializationSchema} to serialize Bets
 */
public class OutboundBetSerde {

    private OutboundBetSerde() {}

    public static KafkaRecordSerializationSchema<BetOuterClass.Bet> serializer(String topic) {
        return KafkaRecordSerializationSchema.builder()
                .setTopic(topic)
                .setValueSerializationSchema(new BetSerializationSchema())
                .build();
    }

    static class BetSerializationSchema implements SerializationSchema<BetOuterClass.Bet> {
        @Override
        public byte[] serialize(BetOuterClass.Bet bet) {
            return bet.toByteArray();
        }
    }

}
