package com.flutter.liabilitycalc.generator;

import com.flutter.gbs.bom.proto.*;
import com.google.protobuf.*;

import java.util.Iterator;

public class BetIterator implements Iterator<BetOuterClass.Bet> {

    private long base = 10000000L;
    private long eventId = base + 1000L;
    private long marketId = base + 1001L;
    private long selectionId = base + 1003l;

    private int numberToGenerate = -1;

    public BetIterator() {
        // use for infinite
    }

    public BetIterator(int numberToGenerate, int selectionId) {
        this.numberToGenerate = numberToGenerate;
        this.selectionId = base + selectionId;
    }

    @Override
    public boolean hasNext() {
        return numberToGenerate == -1 || numberToGenerate > 0;
    }

    @Override
    public BetOuterClass.Bet next() {
        if (numberToGenerate != -1) {
            numberToGenerate--;
        }
        return getBet();
    }

    private BetOuterClass.Bet getBet() {
        final long betTimestamp = System.currentTimeMillis();
        final long now = System.nanoTime();
        final String betId = "PP_" + now + "1234";

        //noinspection UnnecessaryLocalVariable
        final BetOuterClass.Bet bet = BetOuterClass.Bet.newBuilder()
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
