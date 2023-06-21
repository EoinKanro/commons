package io.github.eoinkanro.commons.mvc;

/**
 * Controller with {@link View} and logic
 */
public abstract class Controller {

    /**
     * ID of current controller
     *
     * @return id
     */
    public abstract Long getId();

    /**
     * View of controller
     *
     * @param view view
     */
    public abstract void setView(View view);

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
