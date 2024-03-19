package io.github.eoinkanro.commons.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Helps with jackson json
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Create json
     *
     * @return json
     */
    public static ObjectNode createJson() {
        return mapper.createObjectNode();
    }

    /**
     * Create json array
     *
     * @return json array
     */
    public static ArrayNode createArray() {
        return mapper.createArrayNode();
    }

    /**
     * Convert string with json to provided type
     *
     * @param json json
     * @param type type of object
     * @return object or null
     * @param <T> type of object
     */
    @Nullable
    public static <T> T toObject(String json, Class<T> type) {
        T result = null;
        if (json != null && !json.isEmpty() && type != null) {
            try {
                result = mapper.readValue(json, type);
            } catch (Exception e) {
                log.error("""
                                   Error while converting string json to object.
                                   Json: {}
                                   Object type: {}""", json, type, e);
            }
        } else {
            log.debug("Can't create object {} from json {}", type, json);
        }
        return result;
    }

    /**
     * Convert json string to JsonNode
     *
     * @param json json
     * @return JsonNode or null
     */
    @Nullable
    public static JsonNode toJsonNode(String json) {
        return toObject(json, JsonNode.class);
    }

    /**
     * Convert json string to ObjectNode
     *
     * @param json json
     * @return ObjectNode or null
     */
    @Nullable
    public static ObjectNode toObjectNode(String json) {
        return toObject(json, ObjectNode.class);
    }

    /**
     * Convert object to ObjectNode
     *
     * @param obj object
     * @return ObjectNode or null
     * @param <T> type of object
     */
    @Nullable
    public static <T> ObjectNode toObjectNode(T obj) {
        ObjectNode result = null;
        if (obj != null) {
            try {
                result = mapper.convertValue(obj, ObjectNode.class);
            } catch (Exception e) {
                log.error("""
                                  Error while converting object to json.
                                  Object class: {}
                                  Object: {}""", obj.getClass(), obj, e);
            }
        } else {
            log.debug("Can't create json from null object");
        }
        return result;
    }

    /**
     * Get string value from json by key
     *
     * @param json json
     * @param key key
     * @return string value of key or null
     */
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

    /**
     * Get ObjectNode by key
     *
     * @param json json
     * @param key key
     * @return ObjectNode or null
     */
    @Nullable
    public static ObjectNode getJson(ObjectNode json, String key) {
        return getJsonObject(json, key);
    }

    /**
     * Get ArrayNode by key
     *
     * @param json json
     * @param key key
     * @return ArrayNode or null
     */
    @Nullable
    public static ArrayNode getArray(ObjectNode json, String key) {
        return getJsonObject(json, key);
    }

    /**
     * Get jackson json object from json by key
     *
     * @param json json
     * @param key key
     * @return json object
     * @param <T> type of json object
     */
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

    /**
     * Put string into json using key
     *
     * @param json json
     * @param key key
     * @param value value
     */
    public static void putString(ObjectNode json, String key, String value) {
        if (json != null && key != null && !key.isEmpty()) {
            json.put(key, value);
        } else {
            log.debug("Data [{}] with key [{}] wasn't put into json [{}]", value, key, json);
        }
    }

    /**
     * Put json object into json using key
     *
     * @param json json
     * @param key key
     * @param value value
     */
    public static void putJson(ObjectNode json, String key, JsonNode value) {
        if (json != null && key != null && !key.isEmpty()) {
            json.replace(key, value);
        } else {
            log.trace("Data [{}] with key [{}] wasn't put into json [{}]", value, key, json);
        }
    }
}
