package com.springboot.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.springboot.util.LocalDateTimeUtil;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 性别：英文转中文
 */
public class LocalDateTimeToLongSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeString(String.valueOf(LocalDateTimeUtil.toEpochMilli(localDateTime)));
    }
}
