package io.github.eoinkanro.commons.mvc;

import io.github.eoinkanro.commons.mvc.impl.ForwardAction;
import jakarta.annotation.Nullable;
import lombok.extern.log4j.Log4j2;

import java.util.*;

/**
 * MVC worker, that can run controllers
 */
@Log4j2
public class MvcWorker {

    /**
     * Id of first controller
     */
    public static final Long FIRST_CONTROLLER_ID = 1L;

    /**
     * Stack of controllers
     */
    protected final Deque<Controller> controllersStack = new LinkedList<>();

    /**
     * All existing controllers
     * Controller id - Controller
     */
    protected final Map<Long, Controller> controllers = new HashMap<>();

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
    protected Controller getController(Long id) {
        Controller controller = controllers.get(id);
        if (controller == null) {
            log.error("Fatal error: can't find controller with id {}", id);
        }
        return controller;
    }

    /**
     * Add controllers to worker
     *
     * @param newControllers controllers
     */
    public void addControllers(List<Controller> newControllers) {
        Optional.ofNullable(newControllers).ifPresent(cc -> cc.forEach(this::addController));
    }

    /**
     * Add controller to worker
     *
     * @param controller controller
     */
    public void addController(Controller controller) {
        if (controller != null && !controllers.containsKey(controller.getId())) {
            controllers.put(controller.getId(), controller);
        }
    }

}
