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
                Arguments.of(CliArgument.<Byte>builder()
                        .key(ARGUMENT_KEY)
                        .castFunction(CliArgumentCastFunctions.TO_BYTE)
                        .referenceClass(Byte.class)
                        .build()
                        , Byte.valueOf((byte) DEFAULT_INT)
                ),
                Arguments.of(CliArgument.<Short>builder()
                        .key(ARGUMENT_KEY)
                        .castFunction(CliArgumentCastFunctions.TO_SHORT)
                        .referenceClass(Short.class)
                        .build()
                        , Short.valueOf((short) DEFAULT_INT)
                ),
                Arguments.of(CliArgument.<Integer>builder()
                        .key(ARGUMENT_KEY)
                        .castFunction(CliArgumentCastFunctions.TO_INT)
                        .referenceClass(Integer.class)
                        .build()
                        , Integer.valueOf(DEFAULT_INT)
                ),
                Arguments.of(CliArgument.<Long>builder()
                        .key(ARGUMENT_KEY)
                        .castFunction(CliArgumentCastFunctions.TO_LONG)
                        .referenceClass(Long.class)
                        .build()
                        , Long.valueOf(DEFAULT_INT)
                ),
                Arguments.of(CliArgument.<Float>builder()
                        .key(ARGUMENT_KEY)
                        .castFunction(CliArgumentCastFunctions.TO_FLOAT)
                        .referenceClass(Float.class)
                        .build()
                        , Float.valueOf(DEFAULT_INT)
                ),
                Arguments.of(CliArgument.<Double>builder()
                        .key(ARGUMENT_KEY)
                        .castFunction(CliArgumentCastFunctions.TO_DOUBLE)
                        .referenceClass(Double.class)
                        .build()
                        , Double.valueOf(53.4)
                ),
                Arguments.of(CliArgument.<Boolean>builder()
                        .key(ARGUMENT_KEY)
                        .castFunction(CliArgumentCastFunctions.TO_BOOLEAN)
                        .referenceClass(Boolean.class)
                        .build()
                        , Boolean.valueOf(true)
                ),
                Arguments.of(CliArgument.<String>builder()
                        .key(ARGUMENT_KEY)
                        .castFunction(CliArgumentCastFunctions.TO_STRING)
                        .referenceClass(String.class)
                        .build()
                        , DEFAULT_STR
                )
        );
    }

    @Test
    void getArgument_returnDefault() {
        var cliArgument = CliArgument.<String>builder()
                .key(ARGUMENT_KEY)
                .castFunction(CliArgumentCastFunctions.TO_STRING)
                .referenceClass(String.class)
                .defaultValue(DEFAULT_STR)
                .build();

        String result = CliArgumentUtils.getArgument(cliArgument);
        assertEquals(DEFAULT_STR, result);
    }

    @Test
    void getArgument_getTwiceReturnTheSame() {
        var cliArgument = CliArgument.<String>builder()
                .key(ARGUMENT_KEY)
                .castFunction(CliArgumentCastFunctions.TO_STRING)
                .referenceClass(String.class)
                .defaultValue(DEFAULT_STR)
                .build();

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
                Arguments.of(new CliArgument<>(null, CliArgumentCastFunctions.TO_STRING, String.class, null, null)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, null, String.class, null, null)),
                Arguments.of(new CliArgument<>(ARGUMENT_KEY, CliArgumentCastFunctions.TO_STRING, String.class, null, null))
        );
    }

    @Test
    void getArgument_getTwiceReturnNull() {
        var argument = CliArgument.<String>builder()
                .key(ARGUMENT_KEY)
                .castFunction(CliArgumentCastFunctions.TO_STRING)
                .referenceClass(String.class)
                .build();

        assertNull(CliArgumentUtils.getArgument(argument));
        assertNull(CliArgumentUtils.getArgument(argument));
    }

    @Test
    void getArgument_BadParsing_getNull() {
        try {
            System.setProperty(ARGUMENT_KEY, DEFAULT_STR);

            CliArgument<Integer> argument = CliArgument.<Integer>builder()
                    .key(ARGUMENT_KEY)
                    .castFunction(CliArgumentCastFunctions.TO_INT)
                    .referenceClass(Integer.class)
                    .build();

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
        CliArgument<List<String>> argument = CliArgument.<List<String>>builder()
                .key("a")
                .castFunction(CliArgumentCastFunctions.TO_LIST)
                .referenceClass((Class<List<String>>) ((Class)List.class))
                .build();

        assertEquals(List.of("b", "c"), CliArgumentUtils.getArgument(argument));
    }

    @ParameterizedTest
    @MethodSource("provide_getHelp_ok")
    void getHelp_ok(CliArgument<?> argument, String expected) {
        assertEquals(expected, argument.getHelp().trim());
    }

    static Stream<Arguments> provide_getHelp_ok() {
        return Stream.of(
                Arguments.of(
                        CliArgument.<String>builder().build(),
                        "Key: null"
                ),
                Arguments.of(
                        CliArgument.<String>builder().key(ARGUMENT_KEY).referenceClass(String.class).defaultValue(DEFAULT_STR).description(DEFAULT_STR).build(),
                        "Key: " + ARGUMENT_KEY + " Type: String Default value: " + DEFAULT_STR + " Description: " + DEFAULT_STR
                )
        );
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
