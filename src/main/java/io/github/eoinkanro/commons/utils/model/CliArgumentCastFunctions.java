package io.github.eoinkanro.commons.utils.model;

import io.github.eoinkanro.commons.utils.CliArgumentUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CliArgumentCastFunctions {

    public static final Function<String, Byte> TO_BYTE = (Byte::parseByte);
    public static final Function<String, Short> TO_SHORT = (Short::parseShort);
    public static final Function<String, Integer> TO_INT = (Integer::parseInt);
    public static final Function<String, Long> TO_LONG = (Long::parseLong);
    public static final Function<String, Float> TO_FLOAT = (Float::parseFloat);
    public static final Function<String, Double> TO_DOUBLE = (Double::parseDouble);
    public static final Function<String, Boolean> TO_BOOLEAN = (Boolean::parseBoolean);
    public static final Function<String, String> TO_STRING = (s -> s);
    public static final Function<String, List<String>> TO_LIST = (s -> List.of(s.split("\\" + CliArgumentUtils.ARG_DELIMITER)));

}
