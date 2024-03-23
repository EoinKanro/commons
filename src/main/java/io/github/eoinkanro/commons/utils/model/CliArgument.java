package io.github.eoinkanro.commons.utils.model;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class CliArgument<T> {

    private final String key;
    private final Function<String, T> castFunction;
    private final Class<T> referenceClass;

    @Nullable
    private final T defaultValue;

    public CliArgument(String key, Function<String, T> castFunction, Class<T> referenceClass) {
        this.key = key;
        this.defaultValue = null;
        this.castFunction = castFunction;
        this.referenceClass = referenceClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CliArgument<?> that = (CliArgument<?>) o;
        return Objects.equals(key, that.key) && Objects.equals(referenceClass, that.referenceClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, referenceClass);
    }
}
