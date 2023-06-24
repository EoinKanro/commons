package io.github.eoinkanro.commons.mvc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Controller with {@link View} and logic
 */
@RequiredArgsConstructor
public abstract class Controller {

    /**
     * Id of controller
     */
    @Getter
    protected final Long id;

    /**
     * View of controller
     */
    protected final View view;

    /**
     * Run controller
     *
     * @return {@link Action}
     */
    public final Action perform() {
        Action result = null;
        while (result == null) {
            result = performLogic();
        }

        return result;
    }

    /**
     * Logic of controller
     *
     * @return {@link Action}
     */
    protected abstract Action performLogic();

}
