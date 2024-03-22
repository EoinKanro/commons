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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CliArgument<?> arg) {
            return Objects.equals(arg.getReferenceClass(), this.referenceClass) &&
                   Objects.equals(arg.getKey(), this.key);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (referenceClass != null ? referenceClass.hashCode() : 0);
        return result;
    }

}
