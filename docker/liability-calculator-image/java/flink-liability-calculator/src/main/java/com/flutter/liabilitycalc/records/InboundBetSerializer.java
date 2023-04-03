/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flutter.liabilitycalc.records;

import com.flutter.gbs.bom.proto.BetOuterClass;
import org.apache.flink.playgrounds.ops.clickcount.records.ClickEvent;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

/**
 * A Kafka {@link KafkaSerializationSchema} to serialize {@link ClickEvent}s as JSON.
 *
 */
public class InboundBetSerializer implements KafkaSerializationSchema<BetOuterClass.Bet> {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private String topic;

	public InboundBetSerializer(String topic) {
		objectMapper.addMixIn(BetOuterClass.Bet.class, BetSerializerMixIn.class);
//		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//		objectMapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
		this.topic = topic;
	}

	@Override
	public ProducerRecord<byte[], byte[]> serialize(
			final BetOuterClass.Bet message, @Nullable final Long timestamp) {
		Class<?> mixInClassFor = objectMapper.getSerializationConfig().findMixInClassFor(BetOuterClass.Bet.class);
		System.out.println("mixInClassFor = " + mixInClassFor);
		try {
			//if topic is null, default topic will be used
			return new ProducerRecord<>(topic, objectMapper.writeValueAsBytes(message));
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Could not serialize record: " + message, e);
		}
	}
}
