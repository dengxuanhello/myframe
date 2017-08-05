package com.alibaba.fastjsonex.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import com.alibaba.fastjsonex.parser.DefaultJSONParser;
import com.alibaba.fastjsonex.parser.JSONLexer;
import com.alibaba.fastjsonex.parser.JSONToken;
import com.alibaba.fastjsonex.parser.ParserConfig;
import com.alibaba.fastjsonex.util.FieldInfo;
import com.alibaba.fastjsonex.util.TypeUtils;

public class DoubleFieldDeserializer extends FieldDeserializer {

    private final ObjectDeserializer fieldValueDeserilizer;

    public DoubleFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        super(clazz, fieldInfo);

        fieldValueDeserilizer = mapping.getDeserializer(fieldInfo);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        Double value;

        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_INT) {
            long val = lexer.longValue();
            lexer.nextToken(JSONToken.COMMA);
            if (object == null) {
                fieldValues.put(fieldInfo.getName(), val);
            } else {
                setValue(object, val);
            }
            return;
        } else if (lexer.token() == JSONToken.NULL) {
            value = null;
            lexer.nextToken(JSONToken.COMMA);

        } else {
            Object obj = parser.parse();

            value = TypeUtils.castToDouble(obj);
        }

        if (value == null && getFieldClass() == double.class) {
            // skip
            return;
        }

        if (object == null) {
            fieldValues.put(fieldInfo.getName(), value);
        } else {
            setValue(object, value);
        }
    }

    @Override
    public int getFastMatchToken() {
        return fieldValueDeserilizer.getFastMatchToken();
    }
}
