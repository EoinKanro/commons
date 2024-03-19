package io.github.eoinkanro.commons.mvc;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ActionDataKey<T> {

    private final Class<T> valueType;

}
