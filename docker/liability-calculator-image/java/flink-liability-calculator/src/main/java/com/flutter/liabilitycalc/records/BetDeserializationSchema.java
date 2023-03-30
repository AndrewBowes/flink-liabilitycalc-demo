package com.flutter.liabilitycalc.records;

import com.flutter.gbs.bom.proto.BetOuterClass;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class BetDeserializationSchema implements DeserializationSchema<BetOuterClass.Bet> {
    @Override
    public BetOuterClass.Bet deserialize(byte[] bytes) throws IOException {
        return BetOuterClass.Bet.parseFrom(bytes);
    }

    @Override
    public boolean isEndOfStream(BetOuterClass.Bet bet) {
        return false;
    }

    @Override
    public TypeInformation<BetOuterClass.Bet> getProducedType() {
        return TypeInformation.of(BetOuterClass.Bet.class);
    }
}
