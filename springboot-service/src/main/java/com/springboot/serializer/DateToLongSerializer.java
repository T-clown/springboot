package com.springboot.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.springboot.util.LocalDateTimeUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 时间转时间戳
 */
public class DateToLongSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
            jsonGenerator.writeObject(date.getTime());
           // jsonGenerator.writeNumber(date.getTime());
    }
}
