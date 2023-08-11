package com.springboot.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal序列化
 */
public class BigDecimalSerializer extends StdSerializer<BigDecimal> implements ContextualSerializer {
    private int scale = 1;

    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    public BigDecimalSerializer() {
        super(BigDecimal.class);
    }

    public BigDecimalSerializer(int scale, RoundingMode roundingMode) {
        super(BigDecimal.class);
        this.scale = scale;
        this.roundingMode = roundingMode;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty property) {
        BigDecimalScale scaleAnnotation = property.getAnnotation(BigDecimalScale.class);
        if (scaleAnnotation != null) {
            return new BigDecimalSerializer(scaleAnnotation.scale(),scaleAnnotation.roundingMode());
        }
        return new BigDecimalSerializer();
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            BigDecimal bigDecimal = value.setScale(scale, roundingMode);
            jsonGenerator.writeObject(bigDecimal);
        }
    }


}