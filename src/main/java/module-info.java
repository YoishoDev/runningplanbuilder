module de.hirola.runningplanbuilder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.jetbrains.annotations;
    requires sportsapplications;
    requires com.fasterxml.jackson.core;
    requires org.tinylog.api;

    opens de.hirola.runningplanbuilder.model to javafx.base;
    opens de.hirola.runningplanbuilder to javafx.fxml;
    opens de.hirola.runningplanbuilder.controller to javafx.fxml;

    exports de.hirola.runningplanbuilder;
}