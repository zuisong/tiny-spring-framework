package com.xilidou.framework.ioc.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.core.json.JsonReadFeature.*;


/**
 * @author chen
 */
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//enable
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .enable(ALLOW_SINGLE_QUOTES.mappedFeature())
            .enable(ALLOW_YAML_COMMENTS.mappedFeature())
            .enable(ALLOW_JAVA_COMMENTS.mappedFeature())
            .enable(ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature())
            .enable(ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
//disable
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
//date format
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private JsonUtils() {
    }

    @SneakyThrows
    public static <T> T readValue(InputStream is, TypeReference<T> valueTypeRef) {
        return mapper.readValue(is, valueTypeRef);
    }

}
