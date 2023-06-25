package io.github.eoinkanro.commons.mvc;

/**
 * Controller with {@link View} and logic
 */
public abstract class Controller {

    /**
     * Id of controller
     */
    public abstract Long getId();

    /**
     * View of controller
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

    public String toString() {
        return "Controller [Name: " + this.getClass().getSimpleName() + " Id: " + getId() +"]";
    }

}
