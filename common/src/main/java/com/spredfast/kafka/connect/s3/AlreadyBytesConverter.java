package com.spredfast.kafka.connect.s3;

import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.storage.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * No conversion. Bytes from Kafka are preserved as is and we validate that only bytes go out.
 */
public class AlreadyBytesConverter implements Converter {
        private boolean isKey;

	private static final Logger log = LoggerFactory.getLogger(AlreadyBytesConverter.class);

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
                isKey = isKey;
	}

	@Override
	public byte[] fromConnectData(String topic, Schema schema, Object value) {
		if (schema != null && schema != Schema.BYTES_SCHEMA && schema != Schema.OPTIONAL_BYTES_SCHEMA) {
			throw new DataException(topic + " error: Not a byte array! " + value);
		}
                if (isKey) {
                        log.warn("key: " + value);
                } else {
                        log.warn("value: " + value);
                }
		if (value == null) {
			return null;
		}
		return (byte[]) value;
	}

	@Override
	public SchemaAndValue toConnectData(String topic, byte[] value) {
		return new SchemaAndValue(Schema.BYTES_SCHEMA, value);
	}
}
