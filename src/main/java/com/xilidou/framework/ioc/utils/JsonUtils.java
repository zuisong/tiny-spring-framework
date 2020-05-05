package com.xilidou.framework.ioc.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.core.json.JsonReadFeature.*;


public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    @SneakyThrows
    public static <T> T readValue(String json, Class<T> cls) {
        return mapper.readValue(json, cls);
    }

    @SneakyThrows
    public static <T> T readValue(InputStream is, Class<T> cls) {
        return mapper.readValue(is, cls);
    }

    @SneakyThrows
    public static <T> T readValue(byte[] bytes, Class<T> cls) {
        return mapper.readValue(bytes, cls);
    }

    @SneakyThrows
    public static <T> T readValue(String json, TypeReference<T> valueTypeRef) {
        return mapper.readValue(json, valueTypeRef);
    }

    @SneakyThrows
    public static <T> T readValue(byte[] bytes, TypeReference<T> valueTypeRef) {
        return mapper.readValue(bytes, valueTypeRef);
    }

    @SneakyThrows
    public static <T> T readValue(InputStream is, TypeReference<T> valueTypeRef) {
        return mapper.readValue(is, valueTypeRef);
    }

    @SneakyThrows
    public static String writeValueAsString(Object entity) {
        return mapper.writeValueAsString(entity);
    }

    @SneakyThrows
    public static byte[] writeValueAsBytes(Object entity) {
        return mapper.writeValueAsBytes(entity);
    }

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .enable(ALLOW_SINGLE_QUOTES.mappedFeature())
                .enable(ALLOW_YAML_COMMENTS.mappedFeature())
                .enable(ALLOW_JAVA_COMMENTS.mappedFeature())
                .enable(ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature())
                .enable(ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())

                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
        ;
    }
}
