package io.github.eoinkanro.commons.mvc;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ActionDataKey<T> {

    private final String key;
    private final Class<T> valueType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionDataKey<?> that = (ActionDataKey<?>) o;
        return Objects.equals(key, that.key) && Objects.equals(valueType, that.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, valueType);
    }
}
