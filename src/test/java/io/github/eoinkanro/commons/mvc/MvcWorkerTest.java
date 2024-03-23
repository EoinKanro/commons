package io.github.eoinkanro.commons.mvc;

import io.github.eoinkanro.commons.mvc.impl.BackwardAction;
import io.github.eoinkanro.commons.mvc.impl.ForwardAction;
import io.github.eoinkanro.commons.mvc.impl.StayAction;
import io.github.eoinkanro.commons.mvc.model.SimpleView;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class MvcWorkerTest {

    private static final Long SECOND_ID = 2L;

    private static final ActionDataKey<String> FIRST_KEY = new ActionDataKey<>("key1", String.class);
    private static final String FIRST_VALUE = "str1";
    private static final ActionDataKey<Integer> SECOND_KEY = new ActionDataKey<>("key2", Integer.class);
    private static final Integer SECOND_VALUE = 123;

    @Test
    void perform_noFirstController_noActions() {
        MvcWorker mvcWorker = new MvcWorker();
        var controller = createForwardController(Long.MIN_VALUE, Long.MAX_VALUE);

        mvcWorker.addController(controller);
        mvcWorker.perform();

        Mockito.verify(controller, Mockito.times(0)).perform(any());
    }

    @Test
    void perform_firstController_called() {
        MvcWorker mvcWorker = new MvcWorker();
        var controller = createBackwardController(MvcWorker.FIRST_CONTROLLER_ID);

        mvcWorker.addController(controller);
        mvcWorker.perform();

        Mockito.verify(controller, Mockito.times(1)).perform(any());
    }

    @Test
    void perform_twoControllers_calledBoth() {
        MvcWorker mvcWorker = new MvcWorker();
        var firstController = createForwardController(MvcWorker.FIRST_CONTROLLER_ID, SECOND_ID);
        var secondController = createBackwardController(SECOND_ID);

        mvcWorker.addControllers(List.of(firstController, secondController));
        mvcWorker.perform();

        Mockito.verify(firstController, Mockito.times(1)).perform(any());
        Mockito.verify(secondController, Mockito.times(1)).perform(any());
    }

    private Controller<SimpleView> createForwardController(Long id, Long toId) {
        return Mockito.spy(new Controller<>(id, new SimpleView()) {
            @Override
            protected Action performLogic(ActionData actionData) {
                return new ForwardAction(toId, false);
            }
        });
    }

    private Controller<SimpleView> createBackwardController(Long id) {
        return Mockito.spy(new Controller<SimpleView>(id, new SimpleView()) {
            @Override
            protected Action performLogic(ActionData actionData) {
                return new BackwardAction();
            }
        });
    }

    @Test
    void perform_twoControllersAndData_calledBothAndDataExists() {
        MvcWorker mvcWorker = new MvcWorker();

        var firstController = new Controller<>(MvcWorker.FIRST_CONTROLLER_ID, new SimpleView()) {
            @Override
            protected Action performLogic(ActionData actionData) {
                var forward = new ForwardAction(SECOND_ID, false);
                forward.getActionData().put(FIRST_KEY, FIRST_VALUE);
                return forward;
            }
        };

        var secondController = new Controller<>(SECOND_ID, new SimpleView()) {
            @Override
            protected Action performLogic(ActionData actionData) {
                var backward = new BackwardAction();
                backward.getActionData().putAll(actionData);
                backward.getActionData().put(SECOND_KEY, SECOND_VALUE);
                return backward;
            }
        };

        mvcWorker.addControllers(List.of(firstController, secondController));
        mvcWorker.perform();

        assertEquals(FIRST_VALUE, mvcWorker.getControllerData().get(FIRST_KEY));
        assertEquals(SECOND_VALUE, mvcWorker.getControllerData().get(SECOND_KEY));
    }

    @Test
    void perform_oneControllerStayed_calledTwice() {
        MvcWorker mvcWorker = new MvcWorker();

        var firstController = Mockito.spy(new Controller<>(MvcWorker.FIRST_CONTROLLER_ID, new SimpleView()) {

            private boolean isCalled = false;

            @Override
            protected Action performLogic(ActionData actionData) {
                if (!isCalled) {
                    isCalled = true;
                    return new StayAction();
                }
                return new BackwardAction();
            }
        });

        mvcWorker.addController(firstController);
        mvcWorker.perform();
        Mockito.verify(firstController, Mockito.times(2)).perform(any());
    }

    @Test
    void perform_twoControllersAndSaveForward_calledBothAndFirstTwice() {
        MvcWorker mvcWorker = new MvcWorker();

        var firstController = Mockito.spy(new Controller<>(MvcWorker.FIRST_CONTROLLER_ID, new SimpleView()) {

            private boolean isCalled = false;

            @Override
            protected Action performLogic(ActionData actionData) {
                if (!isCalled) {
                    isCalled = true;
                    return new ForwardAction(SECOND_ID, true);
                }
                return new BackwardAction();
            }
        });

        var secondController = createBackwardController(SECOND_ID);

        mvcWorker.addControllers(List.of(firstController, secondController));
        mvcWorker.perform();

        Mockito.verify(firstController, Mockito.times(2)).perform(any());
        Mockito.verify(secondController, Mockito.times(1)).perform(any());
    }

    @Test
    void perform_oneControllerForward_calledTwice() {
        MvcWorker mvcWorker = new MvcWorker();

        var firstController = Mockito.spy(new Controller<>(MvcWorker.FIRST_CONTROLLER_ID, new SimpleView()) {

            private boolean isCalled = false;

            @Override
            protected Action performLogic(ActionData actionData) {
                if (!isCalled) {
                    isCalled = true;
                    return new ForwardAction(SECOND_ID, true);
                }
                return new BackwardAction();
            }
        });

        mvcWorker.addController(firstController);
        mvcWorker.perform();

        Mockito.verify(firstController, Mockito.times(2)).perform(any());
    }

}
