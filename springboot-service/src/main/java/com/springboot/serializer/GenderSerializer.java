package com.springboot.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.springboot.enums.GenderType;

/**
 * 性别：英文转中文
 */
public class GenderSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String genderType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeString(GenderType.getEnum(genderType).getDesc());
    }
}
