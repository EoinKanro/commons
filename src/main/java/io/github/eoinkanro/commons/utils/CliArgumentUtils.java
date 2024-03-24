package io.github.eoinkanro.commons.utils;

import io.github.eoinkanro.commons.utils.model.CliArgument;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility for parsing and receiving cli or jvm arguments
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CliArgumentUtils {

    private static final String ARG_STARTER = "-";
    public static final String ARG_DELIMITER = "\\,";

    private static final Map<CliArgument<?>, Optional<?>> PARSED_ARGUMENTS = new ConcurrentHashMap<>();
    private static final Map<String, String> CLI_ARGUMENTS = new ConcurrentHashMap<>();

    private static boolean isInit = false;

    /**
     * Init args from main method
     *
     * @param args from main method
     */
    public static void init(String[] args) {
        if (isInit) {
            log.warn("Cli arguments are already initialized. Skipping input {}", Arrays.toString(args));
            return;
        } else if (args == null) {
            log.warn("Skipping null cli arguments");
            return;
        }

        String key = null;
        StringBuilder value = null;
        for (String arg : args) {
            if (arg.startsWith(ARG_STARTER)) {
                putToCliArguments(key, value);

                key = arg.substring(ARG_STARTER.length());
                value = new StringBuilder();
            } else if (value != null) {
                if (!value.isEmpty()) {
                    value.append(ARG_DELIMITER);
                }
                value.append(arg);
            }
        }

        putToCliArguments(key, value);
        isInit = true;
    }

    private static void putToCliArguments(String key, StringBuilder value) {
        if (key != null && !value.isEmpty()) {
            CLI_ARGUMENTS.put(key, value.toString());
        }
    }

    /**
     * Get value of argument from cli or jvm arguments
     *
     * @param argument key
     * @return value of key or null
     * @param <T> type of key
     */
    @Nullable
    public static <T> T getArgument(CliArgument<T> argument) {
        if (!isArgumentFine(argument)) {
            return null;
        }

        if (PARSED_ARGUMENTS.containsKey(argument)) {
            Object value = PARSED_ARGUMENTS.get(argument).orElse(null);
            return value == null ? null : (T) value;
        }

        String argumentValue = getArgumentValue(argument.getKey());
        T result;
        if (argumentValue == null) {
            result = argument.getDefaultValue() == null ? null : argument.getDefaultValue();
        } else {
            result = argument.getCastFunction().apply(argumentValue);
        }

        PARSED_ARGUMENTS.put(argument, Optional.ofNullable(result));
        return result;
    }

    private static String getArgumentValue(String key) {
        String result = CLI_ARGUMENTS.get(key);
        if (result == null) {
            result = System.getProperty(key);
        }
        return result;
    }

    private static boolean isArgumentFine(CliArgument<?> argument) {
        return argument != null && argument.getKey() != null && argument.getCastFunction() != null;
    }
}
