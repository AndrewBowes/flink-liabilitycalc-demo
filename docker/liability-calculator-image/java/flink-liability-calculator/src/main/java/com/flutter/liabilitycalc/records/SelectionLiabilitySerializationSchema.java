package com.flutter.liabilitycalc.records;

import com.flutter.liabilitycalc.model.SelectionLiability;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class SelectionLiabilitySerializationSchema implements SerializationSchema<SelectionLiability> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(SelectionLiability liability) {
        try {
            //if topic is null, default topic will be used
            return objectMapper.writeValueAsBytes(liability);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize record: " + liability, e);
        }
    }
}
