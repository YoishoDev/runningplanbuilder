module de.hirola.runningplanbuilder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.jetbrains.annotations;
    requires sportslibrary;
    requires com.fasterxml.jackson.core;

    opens de.hirola.runningplanbuilder to javafx.fxml;
    opens de.hirola.runningplanbuilder.controller to javafx.fxml;

    exports de.hirola.runningplanbuilder;
}