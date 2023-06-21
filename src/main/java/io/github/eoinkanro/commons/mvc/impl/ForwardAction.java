package io.github.eoinkanro.commons.mvc.impl;

import io.github.eoinkanro.commons.mvc.Action;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ForwardAction implements Action {

    private final long nextControllerId;

    private final boolean saveCurrent;

}
