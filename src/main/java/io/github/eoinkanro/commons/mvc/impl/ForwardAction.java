package io.github.eoinkanro.commons.mvc.impl;

import io.github.eoinkanro.commons.mvc.Action;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Perform forward to next controller using {@link #nextControllerId}
 */
@Getter
@RequiredArgsConstructor
public class ForwardAction implements Action {

    /**
     * Id of next controller
     */
    private final long nextControllerId;

    /**
     * If it's necessary to save current controller to stack
     */
    private final boolean saveCurrent;

}
