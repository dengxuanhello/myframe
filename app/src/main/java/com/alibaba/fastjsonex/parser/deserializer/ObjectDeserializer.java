package com.alibaba.fastjsonex.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjsonex.parser.DefaultJSONParser;

public interface ObjectDeserializer {
    <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName);
    
    int getFastMatchToken();
}
