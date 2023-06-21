package io.github.eoinkanro.commons.mvc;

import io.github.eoinkanro.commons.mvc.impl.ForwardAction;
import jakarta.annotation.Nullable;
import lombok.extern.log4j.Log4j2;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * MVC worker, that can run controllers
 */
@Log4j2
public abstract class MvcWorker {

    public static final Long FIRST_CONTROLLER_ID = 1L;

    /**
     * Stack of controllers
     */
    private final Deque<Controller> controllersStack = new LinkedList<>();

    /**
     * All existing controllers
     * Controller id - Controller
     */
    protected Map<Long, Controller> controllers;

    /**
     * Run MVC worker
     */
    public void perform() {
        Controller firstController = getController(FIRST_CONTROLLER_ID);
        if (firstController == null) {
            return;
        }

        controllersStack.add(firstController);
        while (!controllersStack.isEmpty()) {
            Controller currentController = controllersStack.pollLast();
            Action action = currentController.perform();

            if (action instanceof ForwardAction forwardAction) {
                if (forwardAction.isSaveCurrent()) {
                    controllersStack.add(currentController);
                }

                Controller nextController = getController(forwardAction.getNextControllerId());
                if (nextController != null) {
                    controllersStack.add(nextController);
                } else if(!forwardAction.isSaveCurrent()) {
                    controllersStack.add(currentController);
                }
            }
        }
    }

    /**
     * Get controller by id
     *
     * @param id id
     * @return controller or null
     */
    @Nullable
    private Controller getController(Long id) {
        Controller controller = null;
        if (controllers == null || !controllers.containsKey(id)) {
            log.error("Fatal error: can't find controller with id {}", id);
        } else {
            controller = controllers.get(id);
        }
        return controller;
    }

    public abstract void setControllers(Map<Long, Controller> controllers);

}
