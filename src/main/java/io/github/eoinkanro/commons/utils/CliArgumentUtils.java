package io.github.eoinkanro.commons.utils;

import io.github.eoinkanro.commons.utils.model.CliArgument;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CliArgumentUtils {

    private static final Map<CliArgument<?>, Optional<?>> PARSED_ARGUMENTS = new ConcurrentHashMap<>();

    @Nullable
    public static <T> T getArgument(CliArgument<T> argument) throws ClassCastException {
        if (!isArgumentFine(argument)) {
            return null;
        }

        if (PARSED_ARGUMENTS.containsKey(argument)) {
            Object value = PARSED_ARGUMENTS.get(argument).orElse(null);
            return value == null ? null : (T) value;
        }

        String argumentValue = System.getProperty(argument.getKey());
        T result;
        if (argumentValue == null) {
            result = argument.getDefaultValue() == null ? null : argument.getDefaultValue();
        } else {
            result = argument.getCastFunction().apply(argumentValue);
        }

        PARSED_ARGUMENTS.put(argument, Optional.ofNullable(result));
        return result;
    }

    private static boolean isArgumentFine(CliArgument<?> argument) {
        return argument != null && argument.getKey() != null && argument.getCastFunction() != null;
    }
}
