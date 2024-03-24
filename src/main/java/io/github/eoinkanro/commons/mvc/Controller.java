package io.github.eoinkanro.commons.mvc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Controller with {@link View} and logic
 */
@RequiredArgsConstructor
public abstract class Controller<V extends View> {

    @Getter
    protected final Long id;
    protected final V view;

    /**
     * Run controller
     *
     * @return {@link Action}
     */
    public final Action perform(ActionData actionData) {
        Action result = null;
        while (result == null) {
            result = performLogic(actionData);
        }

        return result;
    }

    /**
     * Logic of controller
     *
     * @return {@link Action}
     */
    protected abstract Action performLogic(ActionData actionData);

}
