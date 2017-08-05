package com.alibaba.fastjsonex.serializer;

public interface NameFilter extends SerializeFilter {

    String process(Object source, String name, Object value);
}
