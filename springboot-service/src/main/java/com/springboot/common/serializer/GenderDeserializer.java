package com.springboot.common.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.springboot.domain.enums.GenderType;

/**
 * 性别：英文转枚举
 */
public class GenderDeserializer extends JsonDeserializer<GenderType> {
    @Override
    public GenderType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        return GenderType.getEnum(jsonParser.getText());
    }
}
