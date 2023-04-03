package com.flutter.liabilitycalc.records;

import com.flutter.gbs.bom.proto.BetOuterClass.Bet;
import com.flutter.liabilitycalc.model.MessageMeta;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Outbound bet serdes to be used in kafka source/sink methods.
 */
public final class OutboundBetSerde {

    @VisibleForTesting
    protected static Logger log = LoggerFactory.getLogger(OutboundBetSerde.class);

    private OutboundBetSerde() {}

    public static KafkaRecordDeserializationSchema<Tuple2<Bet, MessageMeta>> deserializer() {
        return new BetDeserializer();
    }

    /**
     * Outbound bet deserializer.
     */
    static class BetDeserializer implements KafkaRecordDeserializationSchema<Tuple2<Bet, MessageMeta>> {

        /**
         * Gets the outbound bet data type (as a {@link TypeInformation}) produced by this function or input format.
         *
         * @return The bet data type.
         */
        @Override
        public TypeInformation<Tuple2<Bet, MessageMeta>> getProducedType() {
            return Types.TUPLE(Types.GENERIC(Bet.class), Types.POJO(MessageMeta.class));
        }

        @Override
        public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<Tuple2<Bet, MessageMeta>> out) {

            try {
                out.collect(Tuple2.of(Bet.parseFrom(record.value()),
                        MessageMeta.builder()
                                .kafkaTimestamp(record.timestamp())
                                .kafkaTopic(record.topic())
                                .kafkaPartition(record.partition())
                                .kafkaOffset(record.offset())
                                .correlationId(UUID.randomUUID().toString())
                                .inboundTimestamp(System.currentTimeMillis())
                                .build()
                ));
            } catch (Exception e) {
                if (record != null) {
                    log.error("Cannot deserialize message. {}:{}:{}", record.topic(), record.partition(), record.offset(), e);
                } else {
                    log.error("Cannot deserialize null message.", e);
                }
            }
        }
    }
}
