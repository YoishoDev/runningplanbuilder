package de.hirola.runningplanbuilder;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.util.Locale;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Global app settings, can be overwritten by user
 *
 * @author Michael Schmidt (Hirola)
 * @since v0.1
 */
public final class Global {

    public static final String PACKAGE_NAME =  RunningPlanBuilder.class.getPackageName();
    public static final String ROOT_RESOURCE_BUNDLE_BASE_NAME = RunningPlanBuilder.class.getSimpleName();

    public static final String APP_ICON = "/appicon.png";

    public static final Locale DEFAULT_LOCALE = new Locale("en");

    // JSON import and export
    public static final FileChooser.ExtensionFilter JSON_FILE_EXTENSION_FILTER
            = new FileChooser.ExtensionFilter("JSON Format", "*.json");
    public static final String JSON_FILE_EXTENSION = ".json";

    // JSON import and export
    public static final FileChooser.ExtensionFilter ICAL_FILE_EXTENSION_FILTER
            = new FileChooser.ExtensionFilter("iCAL Format", "*.ics");
    public static final String ICAL_FILE_EXTENSION = ".ics";

    // localizations
    public static final String LOCALIZATION_PROPERTIES = "/localizations.properties";

    // application parameter - can be overwritten by the user
    public static final int MAX_COUNT_OF_WEEKS = 52;
    public static final int MAX_ORDER_NUMBER = 30;

    // colors
    public static final Color RUNNING_PLAN_TEMPLATE_NODE_COLOR = Color.ALICEBLUE;
    public static final Color RUNNING_UNIT_NODE_COLOR = Color.YELLOW;

    // main view parameter
    public static class MainViewViewPreferences {
        public static final double DEFAULT_MAIN_VIEW_WIDTH = 1050.0;
        public static final double DEFAULT_MAIN_VIEW_HEIGHT = 800.0;
    }

    // main view table
    public static class MainViewTableViewPreferences {
        public static final double DAY_COLUMN_PREF_WIDTH = 100.0;
        public static final double WEEK_COLUMN_PREF_WIDTH = 80.0;
        public static final double DURATION_COLUMN_PREF_WIDTH = 120.0;
        public static final double DISTANCE_COLUMN_PREF_WIDTH = 120.0;
        public static final double RUNNING_UNIT_COLUMN_PREF_WIDTH = 400.0;
        public static final double REMARKS_COLUMN_PREF_WIDTH = 800.0;
        public static final double MIN_WITH = DAY_COLUMN_PREF_WIDTH
                + WEEK_COLUMN_PREF_WIDTH
                + DURATION_COLUMN_PREF_WIDTH
                + DISTANCE_COLUMN_PREF_WIDTH
                + RUNNING_UNIT_COLUMN_PREF_WIDTH
                + REMARKS_COLUMN_PREF_WIDTH
                + 10.0;

    }

    public static class UserPreferencesKeys {
        public static final String USER_ROOT_NODE = RunningPlanBuilder.class.getName();
        public static final String LOCALE = "locale";
        public static final String USE_DEBUG_MODE = "debug.mode";
        public static final String USE_LAST_VIEW_VALUES = "mainview.use.last.size";
        public static final String LAST_MAIN_VIEW_WIDTH = "mainview.last.width";
        public static final String LAST_MAIN_VIEW_HEIGHT = "mainview.last.height";
        public static final String LAST_MAIN_VIEW_POS_X = "mainview.last.posX";
        public static final String LAST_MAIN_VIEW_POS_Y = "mainview.last.posY";
        public static final String USE_LAST_DIRECTORY = "json.use.last.directory";
        public static final String LAST_DIRECTORY = "json.last.directory";
    }
}

