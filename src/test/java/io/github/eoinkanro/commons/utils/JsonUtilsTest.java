package io.github.eoinkanro.commons.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.eoinkanro.commons.utils.model.BadJson;
import io.github.eoinkanro.commons.utils.model.BigJson;
import io.github.eoinkanro.commons.utils.model.SimpleJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    private static final String NAME = "name";
    private static final String NAME_VALUE = "someName";
    private static final String NUMBER = "number";
    private static final int NUMBER_VALUE = 123;
    private static final String LIST = "list";
    private static final String LIST_VALUE_FIRST = "str1";
    private static final String LIST_VALUE_SECOND = "str2";
    private static final String BAD_OBJECT_STRING = "{\"name:}";
    private static final String JSON = "json";

    @Test
    void createJson_ok() {
        ObjectNode json = JsonUtils.createJson();
        assertNotNull(json);
        assertTrue(json.isEmpty());
    }

    @Test
    void createArray_ok() {
        ArrayNode arrayJson = JsonUtils.createArray();
        assertNotNull(arrayJson);
        assertTrue(arrayJson.isEmpty());
    }

    @Test
    void toObject_ok() {
        SimpleJson testJson = createTestObject();
        SimpleJson parsedJson = JsonUtils.toObject(testJson.toString(), SimpleJson.class);

        assertEquals(testJson, parsedJson);
    }

    @Test
    void toJsonNode_ok() {
        SimpleJson testJson = createTestObject();
        JsonNode parsedJson = JsonUtils.toJsonNode(testJson.toString());

        compare(testJson, (ObjectNode) parsedJson);
    }

    @Test
    void toObjectNode_string_ok() {
        SimpleJson testJson = createTestObject();
        ObjectNode parsedJson = JsonUtils.toObjectNode(testJson.toString());

        compare(testJson, parsedJson);
    }

    @ParameterizedTest
    @MethodSource("provide_toObject_returnNull")
    void toObject_returnNull(String json, Class<?> type) {
        assertNull(JsonUtils.toObject(json, type));
    }

    static Stream<Arguments> provide_toObject_returnNull() {
        return Stream.of(
                Arguments.of(null, SimpleJson.class),
                Arguments.of("", SimpleJson.class),
                Arguments.of(createTestObject().toString(), null),
                Arguments.of(BAD_OBJECT_STRING, SimpleJson.class)
        );
    }

    @ParameterizedTest
    @MethodSource("provide_toJsonObjectNode_string_returnNull")
    void toJsonNode_returnNull(String json) {
        assertNull(JsonUtils.toJsonNode(json));
    }

    @ParameterizedTest
    @MethodSource("provide_toJsonObjectNode_string_returnNull")
    void toObjectNode_string_returnNull(String json) {
        assertNull(JsonUtils.toObjectNode(json));
    }

    static Stream<Arguments> provide_toJsonObjectNode_string_returnNull() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of(BAD_OBJECT_STRING)
        );
    }

    @Test
    void toObjectNode_object_ok() {
        SimpleJson testJson = createTestObject();
        ObjectNode parsedObject = JsonUtils.toObjectNode(testJson);

        compare(testJson, parsedObject);
    }

    @ParameterizedTest
    @MethodSource("provide_toObjectNode_object_returnNull")
    void toObjectNode_object_returnNull(Object object) {
        assertNull(JsonUtils.toObjectNode(object));
    }

    static Stream<Arguments> provide_toObjectNode_object_returnNull() {
        return Stream.of(
               Arguments.of((Object) null),
               Arguments.of(new BadJson(NAME_VALUE))
        );
    }

    @Test
    void getString_ok() {
        SimpleJson testJson = createTestObject();
        JsonNode json = JsonUtils.toJsonNode(testJson.toString());

        assertEquals(testJson.getName(), JsonUtils.getString(json, NAME));
    }

    @ParameterizedTest
    @MethodSource("provide_getString_returnNull")
    void getString_returnNull(JsonNode json, String key) {
        assertNull(JsonUtils.getString(json, key));
    }

    static Stream<Arguments> provide_getString_returnNull() {
        ObjectNode jsonNode = JsonUtils.createJson();
        jsonNode.put(NAME, (String) null);

        return Stream.of(
                Arguments.of(null, NAME),
                Arguments.of(JsonUtils.toJsonNode(createTestObject().toString()), null),
                Arguments.of(JsonUtils.createJson(), NAME),
                Arguments.of(jsonNode, NAME)
        );
    }

    @Test
    void getJson_ok() {
        BigJson testJson = new BigJson(createTestObject());
        ObjectNode json = JsonUtils.toObjectNode(testJson.toString());

        ObjectNode result = JsonUtils.getJson(json, JSON);
        assertNotNull(result);
        assertFalse(result.isNull());
    }

    @ParameterizedTest
    @MethodSource("provide_getJson_returnNull")
    void getJson_returnNull(ObjectNode json, String key) {
        assertNull(JsonUtils.getJson(json, key));
    }

    static Stream<Arguments> provide_getJson_returnNull() {
        return Stream.of(
               Arguments.of(null, JSON),
               Arguments.of(JsonUtils.createJson(), null),
               Arguments.of(JsonUtils.createJson(), JSON),
               Arguments.of(JsonUtils.toObjectNode(createTestObject().toString()), NAME)
        );
    }

    @Test
    void getArray_ok() {
        SimpleJson testJson = createTestObject();
        ObjectNode json = JsonUtils.toObjectNode(testJson.toString());

        ArrayNode result = JsonUtils.getArray(json, LIST);
        assertNotNull(result);
        assertFalse(result.isNull());
    }

    @ParameterizedTest
    @MethodSource("provide_getArray_returnNull")
    void getArray_returnNull(ObjectNode json, String key) {
        assertNull(JsonUtils.getArray(json, key));
    }

    static Stream<Arguments> provide_getArray_returnNull() {
        return Stream.of(
                Arguments.of(null, LIST),
                Arguments.of(JsonUtils.createJson(), null),
                Arguments.of(JsonUtils.createJson(), LIST),
                Arguments.of(JsonUtils.toObjectNode(createTestObject().toString()), NAME)
        );
    }

    @ParameterizedTest
    @MethodSource("provide_putString_ok")
    void putString_ok(String value) {
        ObjectNode json = JsonUtils.createJson();
        JsonUtils.putString(json, NAME, value);
        assertTrue(json.has(NAME));
    }

    static Stream<Arguments> provide_putString_ok() {
        return Stream.of(
                Arguments.of(NAME_VALUE),
                Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("provide_putString_notPut")
    void putString_notPut(ObjectNode json, String key) {
        Exception exception = null;
        try {
            JsonUtils.putString(json, key, NAME_VALUE);
        } catch (Exception e) {
            exception = e;
        }

        assertNull(exception);
        if (json != null) {
            assertFalse(json.has(key));
        }
    }

    static Stream<Arguments> provide_putString_notPut() {
        return Stream.of(
                Arguments.of(null, NAME),
                Arguments.of(JsonUtils.createJson(), null),
                Arguments.of(JsonUtils.createJson(), "")
        );
    }

    @ParameterizedTest
    @MethodSource("provide_putJson_ok")
    void putJson_ok(JsonNode value) {
        ObjectNode json = JsonUtils.createJson();
        JsonUtils.putJson(json, NAME, value);

        assertTrue(json.has(NAME));
    }

    static Stream<Arguments> provide_putJson_ok() {
        return Stream.of(
               Arguments.of((Object) null),
               Arguments.of(JsonUtils.createJson())
        );
    }

    @ParameterizedTest
    @MethodSource("provide_putJson_notPut")
    void putJson_notPut(ObjectNode json, String key) {
        Exception exception = null;
        try {
            JsonUtils.putJson(json, key, JsonUtils.createJson());
        } catch (Exception e) {
            exception = e;
        }

        assertNull(exception);
        if (json != null) {
            assertFalse(json.has(key));
        }
    }

    static Stream<Arguments> provide_putJson_notPut() {
        return Stream.of(
                Arguments.of(null, NAME),
                Arguments.of(JsonUtils.createJson(), null),
                Arguments.of(JsonUtils.createJson(), "")
        );
    }

    private static SimpleJson createTestObject() {
        return new SimpleJson(NAME_VALUE, NUMBER_VALUE, List.of(LIST_VALUE_FIRST, LIST_VALUE_SECOND));
    }

    private void compare(SimpleJson testJson, ObjectNode objectNode) {
        assertNotNull(objectNode);
        assertEquals(3, objectNode.size());
        assertEquals(testJson.getName(), objectNode.get(NAME).asText());
        assertEquals(testJson.getNumber(), objectNode.get(NUMBER).asInt());

        ArrayNode arrayNode = (ArrayNode) objectNode.get(LIST);
        assertEquals(testJson.getList().size(), arrayNode.size());
        for (int i = 0; i < testJson.getList().size(); i++) {
            assertEquals(testJson.getList().get(i), arrayNode.get(i).asText());
        }
    }

}
