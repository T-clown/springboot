package com.springboot.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ImageURLSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private String prefix;

    public ImageURLSerializer() {
        this("");
    }

    public ImageURLSerializer(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String url = prefix + value;
        jsonGenerator.writeString(url);
    }

    /**
     * 只会在第一次序列化字段时调用
     *
     * @param serializerProvider
     * @param beanProperty
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                ImageURL imageUrl = beanProperty.getAnnotation(ImageURL.class);
                if (imageUrl == null) {
                    imageUrl = beanProperty.getContextAnnotation(ImageURL.class);
                }
                if (imageUrl != null) {
                    return new ImageURLSerializer(imageUrl.value());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
