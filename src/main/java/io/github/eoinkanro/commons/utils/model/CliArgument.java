package io.github.eoinkanro.commons.utils.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter
@Builder
@RequiredArgsConstructor
public class CliArgument<T> {

    private static final String SPACE = " ";
    private static final String DELIMITER = ";   ";

    private final List<String> keys;
    private final Function<String, T> castFunction;
    private final Class<T> referenceClass;

    private final T defaultValue;
    private final String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CliArgument<?> that = (CliArgument<?>) o;
        return Objects.equals(keys, that.keys) && Objects.equals(referenceClass, that.referenceClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keys, referenceClass);
    }

    public String getHelp() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Key: ");
        stringBuilder.append(keys);
        stringBuilder.append(DELIMITER);

        if (referenceClass != null) {
            stringBuilder.append("Type: ");
            stringBuilder.append(referenceClass.getSimpleName());
            stringBuilder.append(DELIMITER);
        }

        if (defaultValue != null) {
            stringBuilder.append("Default value: ");
            stringBuilder.append(defaultValue);
            stringBuilder.append(DELIMITER);
        }

        if (description != null) {
            stringBuilder.append("Description: ");
            stringBuilder.append(description);
        }
        return stringBuilder.toString();
    }
}
