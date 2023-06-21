package io.github.eoinkanro.commons.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectNode createJson() {
        return mapper.createObjectNode();
    }

    public static ArrayNode createArray() {
        return mapper.createArrayNode();
    }

    @Nullable
    public static <T> T toObject(String json, Class<T> type) {
        T result = null;
        if (json != null && !json.isEmpty() && type != null) {
            try {
                result = mapper.readValue(json, type);
            } catch (Exception e) {
                log.error("Error while converting string json to object.\n" +
                                   "Json: {}\n" +
                                   "Object type: {}", json, type, e);
            }
        } else {
            if (log.isDebugEnabled()) log.debug("Can't create object {} from json {}", type, json);
        }
        return result;
    }

    @Nullable
    public static JsonNode toJsonNode(String json) {
        return toObject(json, JsonNode.class);
    }

    @Nullable
    public static ObjectNode toObjectNode(String json) {
        return toObject(json, ObjectNode.class);
    }

    @Nullable
    public static <T> ObjectNode toObjectNode(T obj) {
        ObjectNode result = null;
        if (obj != null) {
            try {
                result = mapper.convertValue(obj, ObjectNode.class);
            } catch (Exception e) {
                log.error("Error while converting object to json.\n" +
                                  "Object class: {}\n" +
                                  "Object: {}", obj.getClass(), obj, e);
            }
        } else {
            if(log.isDebugEnabled()) log.debug("Can't create json from null object");
        }
        return result;
    }

    @Nullable
    public static String getString(JsonNode json, String key) {
        String result = null;
        if (json != null && json.has(key)) {
            JsonNode value = json.get(key);
            if (!value.isNull()) {
                result = value.asText();
            }
        }
        return result;
    }

    @Nullable
    public static ObjectNode getJson(ObjectNode json, String key) {
        return getJsonObject(json, key);
    }

    @Nullable
    public static ArrayNode getArray(ObjectNode json, String key) {
        return getJsonObject(json, key);
    }

    @Nullable
    private static <T> T getJsonObject(ObjectNode json, String key) {
        T result = null;
        if (json != null && json.has(key)) {
            try {
                result = (T) json.get(key);
            } catch (Exception e) {
                log.error("Error while getting json", e);
            }
        }
        return result;
    }

    public static void putString(ObjectNode json, String key, String value) {
        if (json != null && key != null && !key.isEmpty()) {
            json.put(key, value);
        } else {
            if (log.isDebugEnabled()) log.debug("Data [{}] with key [{}] wasn't put into json [{}]", value, key, json);
        }
    }

    public static void putJson(ObjectNode json, String key, JsonNode value) {
        if (json != null && key != null && !key.isEmpty()) {
            json.replace(key, value);
        } else {
            if (log.isTraceEnabled()) log.trace("Data [{}] with key [{}] wasn't put into json [{}]", value, key, json);
        }
    }
}
