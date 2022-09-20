package net.foundation.mcrypto.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper OM;

    static {
        OM = new ObjectMapper();
        OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static final <T> T parseObject(String content, Class<T> valueType) {
        try {
            return OM.readValue(content, valueType);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final String toJSONString(Object value) {
        try {
            return OM.writeValueAsString(value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
