module de.hirola.runningplanbuilder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.jetbrains.annotations;
    requires java.desktop;
    //requires sports.library;

    opens de.hirola.runningplanbuilder to javafx.fxml;
    opens de.hirola.runningplanbuilder.controller to javafx.fxml;

    exports de.hirola.runningplanbuilder;
}