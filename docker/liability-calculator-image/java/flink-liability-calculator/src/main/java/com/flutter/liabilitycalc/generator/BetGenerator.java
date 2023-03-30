package com.flutter.liabilitycalc.generator;

import com.flutter.gbs.bom.proto.*;
import org.apache.flink.api.java.utils.ParameterTool;
import com.google.protobuf.*;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ser.std.ByteArraySerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Iterator;
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
            BetIterator betIterator = new BetIterator();

            while (betIterator.hasNext()) {
                BetOuterClass.Bet bet = betIterator.next();
                ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, bet.toByteArray());
                producer.send(record);
                Thread.sleep(DELAY);
            }
        }
    }

    private static Properties createKafkaProperties(final ParameterTool params) {
        //noinspection DuplicatedCode
        String brokers = params.get("bootstrap.servers", "localhost:9092");
        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());
        return kafkaProps;
    }

    static class BetIterator implements Iterator<BetOuterClass.Bet> {

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public BetOuterClass.Bet next() {
            /*
             TODO - Add method to generate a new bet
             Need to supply a limited list of Events, Markets & Selections so that they can be grouped by Liability Calculator
             */
            long base = 10000000L;
            long eventId = base + 1000L;
            long marketId = base + 1001L;
            long selectionId = base + 1003L;
            return generateBet(eventId, marketId, selectionId);
        }

        /**
         * Generate a simple Win Only Bet on a given Event, Market & Selection
         *
         * @param eventId - The Event that the Market is part of
         * @param marketId - The Market within the Event that contains the Selection
         * @param selectionId - The ID of the Selection which is being bet on.
         *
         * @return A Bet instance which will be placed on the Kafka Topic.
         */
        private BetOuterClass.Bet generateBet(final long eventId, final long marketId, final long selectionId){

            long betTimestamp = System.currentTimeMillis();
            long now = System.nanoTime();
            String betId = "PP_" + now + "1234";

            BetOuterClass.Bet bet = BetOuterClass.Bet.newBuilder()
                    .setBetId(StringValue.of(betId))
                    .setDestination(DestinationOuterClass.Destination.newBuilder()
                            .setName(DestinationOuterClass.Destination.BetPlatform.PADDY_POWER)
                            .build())
                    .setNonCumulativePercentageOfCumulativeMaxBet(DoubleValue.of(3.72))
                    .setCustomer(CustomerOuterClass.Customer.newBuilder()
                            .setCountryCode(CountryOuterClass.Country.GB)
                            .setStakeFactor(FloatValue.of(2.0f))
                            .setLiabilityGroup(StringValue.of("VIP"))
                            .build())
                    .setBetAction(BetOuterClass.Bet.BetAction.PLACEMENT)
                    .setBetType(BetOuterClass.Bet.BetType.SINGLE)
                    .setCurrency(CurrencyOuterClass.Currency.GBP)
                    .setBetTime(Int64Value.of(betTimestamp))
                    .addLegs(LegOuterClass.Leg.newBuilder()
                            .setIsRamp(BoolValue.of(true))
                            .setSubclass(LegOuterClass.Leg.Subclass.newBuilder()
                                    .setId(StringValue.of("123"))
                                    .build())
                            .setEvent(LegOuterClass.Leg.Event.newBuilder()
                                    .setId(StringValue.of(String.valueOf(eventId)))
                                    .build())
                            .setMarket(LegOuterClass.Leg.Market.newBuilder()
                                    .setId(StringValue.of(String.valueOf(marketId)))
                                    .setIsInPlay(BoolValue.of(false))
                                    .build())
                            .setSelection(LegOuterClass.Leg.Selection.newBuilder()
                                    .setId(StringValue.of(String.valueOf(selectionId)))
                                    .build())
                            .setPrice(LegOuterClass.Leg.Price.newBuilder()
                                    .setType(LegOuterClass.Leg.Price.PriceType.LIVE_PRICE)
                                    .setFinal(DoubleValue.of(2.0))
                                    .build())
                            .setType(LegOuterClass.Leg.LegType.WIN_ONLY)
                            .build())
                    .setStakePerLine(DoubleValue.of(10.0))
                    .build();

            return bet;
        }
    }
}
