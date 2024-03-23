package io.github.eoinkanro.commons.mvc;

import jakarta.annotation.Nullable;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class ActionData {

    private final Map<ActionDataKey<?>, Object> data = new ConcurrentHashMap<>();

    public <T> void put(ActionDataKey<T> key, T value) {
        if (value != null) {
            data.put(key, value);
        } else {
            log.trace("Can't put key {} with null value", key);
        }
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
