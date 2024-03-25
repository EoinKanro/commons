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
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CliArgumentUtilsTest {

    private static final String ARGUMENT_KEY = "key";
    private static final int DEFAULT_INT = 53;
    private static final String DEFAULT_STR = "someString";

    @BeforeEach
    @SneakyThrows
    void clear() {
        getParsedArguments().clear();
        getCliArguments().clear();

        Field field = CliArgumentUtils.class.getDeclaredField("isInit");
        field.setAccessible(true);
        field.set(null, false);
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

    @Test
    void getArgument_BadParsing_getNull() {
        try {
            System.setProperty(ARGUMENT_KEY, DEFAULT_STR);

            CliArgument<Integer> argument = new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_INT, Integer.class);
            assertNull(CliArgumentUtils.getArgument(argument));
        } finally {
            System.clearProperty(ARGUMENT_KEY);
        }
    }

    @ParameterizedTest
    @MethodSource("provide_init_ok")
    void init_ok(String[] args, Map<String, String> expected) {
        CliArgumentUtils.init(args);
        assertEquals(expected, getCliArguments());
    }

    static Stream<Arguments> provide_init_ok() {
        return Stream.of(
                Arguments.of(new String[]{}, Map.of()),
                Arguments.of(new String[]{"a", "-b"}, Map.of()),
                Arguments.of(new String[]{"-a", "b"}, Map.of("a", "b")),
                Arguments.of(new String[]{"-a", "b", "c"}, Map.of("a", "b" + CliArgumentUtils.ARG_DELIMITER + "c")),
                Arguments.of(new String[]{"-a", "b", "-c", "d"}, Map.of("a", "b", "c", "d"))
        );
    }

    @Test
    void init_callTwice_skipSecond() {
        CliArgumentUtils.init(new String[]{"-a", "b"});
        CliArgumentUtils.init(new String[]{"-c", "d"});

        assertEquals(Map.of("a", "b"), getCliArguments());
    }

    @Test
    @SneakyThrows
    void init_callWithNull_notInit() {
        CliArgumentUtils.init(null);

        Field field = CliArgumentUtils.class.getDeclaredField("isInit");
        field.setAccessible(true);
        boolean isInit = field.getBoolean(null);
        field.setAccessible(false);

        assertFalse(isInit);
    }

    @Test
    void initGetArgument_parseList_ok() {
        CliArgumentUtils.init(new String[]{"-a", "b", "c"});
        CliArgument<List<String>> argument = new CliArgument<>("a", CliArgumentCastFunctions.TO_LIST, (Class<List<String>>) ((Class)List.class));
        assertEquals(List.of("b", "c"), CliArgumentUtils.getArgument(argument));
    }

    private Map<?, ?> getParsedArguments() {
        return getMap("PARSED_ARGUMENTS");
    }

    private Map<?, ?> getCliArguments() {
        return getMap("CLI_ARGUMENTS");
    }

    @SneakyThrows
    private Map<?, ?> getMap(String fieldName) {
        Field field = CliArgumentUtils.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        Map<?, ?> result = (Map<?, ?>) field.get(null);
        field.setAccessible(false);
        return result;
    }

}
