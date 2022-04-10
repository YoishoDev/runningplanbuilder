package de.hirola.runningplanbuilder;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

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

    public static boolean NODES_MOVABLE = false;

    // positions
    public static Point2D EDITOR_PANE_NODE_START_POINT = new Point2D(40,20);
    public static double SPACE_BETWEEN_NODES = 40d;

    // colors
    public static Color START_CIRCLE_COLOR = Color.GREEN;
    public static Color STOP_CIRCLE_COLOR = Color.RED;
    public static Color RUNNING_UNIT_NODE_COLOR = Color.YELLOW;
    public static Color CONNECTION_COLOR = Color.BLACK;

    // start and stop node
    public static double CIRCLE_RADIUS = 10.0;

    // running unit nodes
    public static double RUNNING_UNIT_NODE_WITH = 100;
    public static double RUNNING_UNIT_NODE_HEIGHT = 50;
    public static Color RUNNING_UNIT_NODE_STROKE_COLOR = Color.BLACK;
}
