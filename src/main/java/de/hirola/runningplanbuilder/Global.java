package de.hirola.runningplanbuilder;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.io.File;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Global app settings, can be overwritten by user
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public final class Global {

    public static final String PACKAGE_NAME = Global.class.getPackageName();

    public static final boolean NODES_MOVABLE = false;

    // application parameter - can be overwritten by the user
    public static final int MAX_COUNT_OF_WEEKS = 52;
    public static final int MAX_ORDER_NUMBER = 30;

    // positions
    public static final Point2D EDITOR_PANE_NODE_START_POINT = new Point2D(40,20);
    public static final double SPACE_BETWEEN_NODES = 40d;

    // colors
    public static final Color RUNNING_PLAN_TEMPLATE_NODE_COLOR = Color.ALICEBLUE;
    public static final Color START_CIRCLE_COLOR = Color.GREEN;
    public static final Color STOP_CIRCLE_COLOR = Color.RED;
    public static final Color RUNNING_UNIT_NODE_COLOR = Color.YELLOW;

    // start and stop node
    public static final double CIRCLE_RADIUS = 10.0;

    // running unit nodes
    public static final double RUNNING_UNIT_NODE_WITH = 100;
    public static final double RUNNING_UNIT_NODE_HEIGHT = 50;
    public static final Color RUNNING_UNIT_NODE_STROKE_COLOR = Color.BLACK;

    // connection notes
    public static final double CONNECTION_STROKE_WIDTH = 3.0;
    public static final double CONNECTION_LINE_BREAK_FACTOR = 1.3;
}
