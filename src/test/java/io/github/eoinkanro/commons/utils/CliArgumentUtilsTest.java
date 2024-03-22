package io.github.eoinkanro.commons.utils;

import io.github.eoinkanro.commons.utils.model.CliArgument;
import io.github.eoinkanro.commons.utils.model.CliArgumentCastFunctions;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CliArgumentUtilsTest {

    private static final String ARGUMENT_KEY = "key";
    private static final int DEFAULT_INT = 53;
    private static final String DEFAULT_STR = "someString";

    @BeforeEach
    @SneakyThrows
    void setUp() {
        Field field = CliArgumentUtils.class.getDeclaredField("PARSED_ARGUMENTS");
        field.setAccessible(true);

        Map<?, ?> parsedArguments = (Map<?, ?>) field.get(null);
        parsedArguments.clear();

        field.setAccessible(false);
    }

    @ParameterizedTest
    @MethodSource("provide_getArgument_ok")
    <T> void getArgument_ok(CliArgument<T> argument, T value) {
        try {
            System.setProperty(ARGUMENT_KEY, String.valueOf(value));

            T result = CliArgumentUtils.getArgument(argument);
            assertEquals(value, result);
        } finally {
            System.clearProperty(ARGUMENT_KEY);
        }
    }

    static Stream<Arguments> provide_getArgument_ok() {
        return Stream.of(
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_BYTE, Byte.class), Byte.valueOf((byte) DEFAULT_INT)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_SHORT, Short.class), Short.valueOf((short) DEFAULT_INT)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_INT, Integer.class), Integer.valueOf(DEFAULT_INT)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_LONG, Long.class), Long.valueOf(DEFAULT_INT)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_FLOAT, Float.class), Float.valueOf(DEFAULT_INT)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_DOUBLE, Double.class), Double.valueOf(53.4)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_BOOLEAN, Boolean.class), Boolean.valueOf(true)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_STRING, String.class), DEFAULT_STR)
        );
    }

    @Test
    void getArgument_returnDefault() {
        var cliArgument = new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_STRING, String.class, DEFAULT_STR);

        String result = CliArgumentUtils.getArgument(cliArgument);
        assertEquals(DEFAULT_STR, result);
    }

    @Test
    void getArgument_getTwiceReturnTheSame() {
        var cliArgument = new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_STRING, String.class, DEFAULT_STR);

        String result = CliArgumentUtils.getArgument(cliArgument);
        assertEquals(DEFAULT_STR, result);

        result = CliArgumentUtils.getArgument(cliArgument);
        assertEquals(DEFAULT_STR, result);
    }

    @ParameterizedTest
    @MethodSource("provide_getArgument_returnNull")
    void getArgument_returnNull(CliArgument<?> argument) {
        assertNull(CliArgumentUtils.getArgument(argument));
    }

    static Stream<Arguments> provide_getArgument_returnNull() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(new CliArgument<>(null, CliArgumentCastFunctions.TO_STRING, String.class)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, null, String.class)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_STRING, String.class))
        );
    }

    @Test
    void getArgument_getTwiceReturnNull() {
        var argument = new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_STRING, String.class);
        assertNull(CliArgumentUtils.getArgument(argument));
        assertNull(CliArgumentUtils.getArgument(argument));
    }

}
