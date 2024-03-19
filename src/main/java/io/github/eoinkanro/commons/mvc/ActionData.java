package io.github.eoinkanro.commons.mvc;

import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ActionData {

    private final Map<ActionDataKey<?>, Object> data = new HashMap<>();

    public <T> void put(ActionDataKey<T> key, T value) {
        data.put(key, value);
    }

    public void putAll(ActionData actionData) {
        data.putAll(actionData.data);
    }

    @Nullable
    public <T> T get(ActionDataKey<T> key) {
        Object value = data.get(key);
        if (value != null) {
            return (T) value;
        }
        return null;
    }

}
