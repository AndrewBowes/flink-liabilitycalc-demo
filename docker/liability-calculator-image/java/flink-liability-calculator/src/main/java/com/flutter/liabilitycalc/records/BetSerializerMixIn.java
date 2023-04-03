package com.flutter.liabilitycalc.records;

import com.flutter.gbs.bom.proto.BetOuterClass;
import com.google.protobuf.UnknownFieldSet;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BetSerializerMixIn {

    @JsonIgnore abstract UnknownFieldSet getUnknownFields();

    @JsonIgnore abstract BetOuterClass.Bet getDefaultInstanceForType();

}
