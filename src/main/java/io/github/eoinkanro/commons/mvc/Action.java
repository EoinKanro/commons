package io.github.eoinkanro.commons.mvc;

import lombok.Getter;

/**
 * Action of {@link Controller}
 */
@Getter
public abstract class Action {

    private final ActionData actionData = new ActionData();

}
