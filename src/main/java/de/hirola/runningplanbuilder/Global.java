package de.hirola.runningplanbuilder;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Global app settings, can be overwritten by user
 *
 * @author Michael Schmidt (Hirola)
 * @since v.0.1
 */
public final class Global {

    public static final String PACKAGE_NAME = Global.class.getPackageName();

    public static final FileChooser.ExtensionFilter TEMPLATE_FILE_EXTENSION_FILTER
            = new FileChooser.ExtensionFilter("JSON Format", "*.json");

    public static final String TEMPLATE_FILE_EXTENSION = ".json";

    // application parameter - can be overwritten by the user
    public static final int MAX_COUNT_OF_WEEKS = 52;
    public static final int MAX_ORDER_NUMBER = 30;

    // colors
    public static final Color RUNNING_PLAN_TEMPLATE_NODE_COLOR = Color.ALICEBLUE;
    public static final Color RUNNING_UNIT_NODE_COLOR = Color.YELLOW;

    // main view table
    public static final double DAY_COLUMN_PREF_WIDTH = 80.0;
    public static final double WEEK_COLUMN_PREF_WIDTH = 80.0;
    public static final double DURATION_COLUMN_PREF_WIDTH = 120.0;
    public static final double RUNNING_UNIT_COLUMN_PREF_WIDTH = 500.0;
}

