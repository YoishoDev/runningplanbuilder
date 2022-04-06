package de.hirola.runningplanbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RunningPlanBuilderController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}