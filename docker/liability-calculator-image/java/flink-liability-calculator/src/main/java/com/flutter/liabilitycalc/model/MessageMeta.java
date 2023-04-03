package com.flutter.liabilitycalc.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MessageMeta {

    private Long kafkaTimestamp;
    private String kafkaTopic;
    private Integer kafkaPartition;
    private Long kafkaOffset;
    private Long inboundTimestamp;
    private String correlationId;
}
