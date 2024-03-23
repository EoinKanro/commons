package io.github.eoinkanro.commons.mvc;

import io.github.eoinkanro.commons.mvc.impl.ForwardAction;
import io.github.eoinkanro.commons.mvc.impl.StayAction;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.*;

/**
 * Runs MVC logic
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
    private final Deque<Controller<?>> controllersStack = new LinkedList<>();

    /**
     * All existing controllers
     * Controller id - Controller
     */
    private final Map<Long, Controller<?>> controllers = new HashMap<>();

    @Getter
    private ActionData controllerData = null;

    /**
     * Run MVC worker
     */
    public final void perform() {
        Controller<?> firstController = getController(FIRST_CONTROLLER_ID);
        if (firstController == null) {
            return;
        }

        controllersStack.add(firstController);
        while (!controllersStack.isEmpty()) {
            Controller<?> currentController = controllersStack.pollLast();
            Action action = currentController.perform(controllerData);
            controllerData = action.getActionData();

            if (action instanceof ForwardAction forwardAction) {
                if (forwardAction.isSaveCurrent()) {
                    controllersStack.add(currentController);
                }

                Controller<?> nextController = getController(forwardAction.getNextControllerId());
                if (nextController != null) {
                    controllersStack.add(nextController);
                } else if(!forwardAction.isSaveCurrent()) {
                    controllersStack.add(currentController);
                }
            } else if (action instanceof StayAction) {
                controllersStack.add(currentController);
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
    private Controller<?> getController(Long id) {
        Controller<?> controller = controllers.get(id);
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
    public final void addControllers(List<Controller<?>> newControllers) {
        Optional.ofNullable(newControllers).ifPresent(cc -> cc.forEach(this::addController));
    }

    /**
     * Add controller to worker
     *
     * @param controller controller
     */
    public final void addController(Controller<?> controller) {
        Optional.ofNullable(controller).ifPresent(c -> {
            if (!controllers.containsKey(c.getId())) {
                controllers.put(c.getId(), c);
                log.debug("{} was added", c);
            } else {
                log.warn("{} wasn't added, the id is already registered", c);
            }
        });
    }

}
