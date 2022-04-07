package de.hirola.runningplanbuilder.model;

import de.hirola.runningplanbuilder.Global;
import javafx.scene.shape.Rectangle;

/**
 * Copyright 2021 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * An object of this class presents a running unit in a running plan.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class RunningUnitRectangle extends Rectangle {

    public RunningUnitRectangle() {
        initialize();
    }

    private void initialize() {
        setX(50);
        setY(50);
        setStroke(Global.RUNNING_UNIT_NODE_STROKE_COLOR);
        setWidth(Global.RUNNING_UNIT_NODE_WITH);
        setHeight(Global.RUNNING_UNIT_NODE_HEIGHT);
        setFill(Global.RUNNING_UNIT_NODE_COLOR);
    }
}
