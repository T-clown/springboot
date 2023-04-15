package com.springboot.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import com.springboot.entity.User;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

//@JsonComponent
public class UserCombinedSerializer {

    public static class UserJsonSerializer extends JsonSerializer<User> {

        @Override
        public void serialize(User user, JsonGenerator generator,
                              SerializerProvider provider) throws IOException {

            generator.writeStartObject();
            generator.writeStringField("id", convert(null));
            generator.writeEndObject();
        }

        private static String convert(Enum a) {
            return null;
        }
    }

    public static class UserJsonDeserializer extends JsonDeserializer<User> {

        @Override
        public User deserialize(JsonParser parser,
                                DeserializationContext context) throws IOException {
            TreeNode treeNode = parser.getCodec().readTree(parser);
            TextNode id = (TextNode) treeNode.get("id");
            return new User();
        }
    }
}
