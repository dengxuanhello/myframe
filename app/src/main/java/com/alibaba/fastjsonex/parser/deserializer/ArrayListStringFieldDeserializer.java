package com.alibaba.fastjsonex.parser.deserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import com.alibaba.fastjsonex.parser.DefaultJSONParser;
import com.alibaba.fastjsonex.parser.JSONLexer;
import com.alibaba.fastjsonex.parser.JSONToken;
import com.alibaba.fastjsonex.parser.ParserConfig;
import com.alibaba.fastjsonex.util.FieldInfo;

public class ArrayListStringFieldDeserializer extends FieldDeserializer {

    public ArrayListStringFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);

    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        ArrayList<Object> list;

        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            list = null;
        } else {
            list = new ArrayList<Object>();

            ArrayListStringDeserializer.parseArray(parser, list);
        }
        if (object == null) {
            fieldValues.put(fieldInfo.getName(), list);
        } else {
            setValue(object, list);
        }
    }
}
