package de.hirola.runningplanbuilder;

import de.hirola.runningplanbuilder.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * The start class of the application.
 *
 * @author Michael Schmidt (Hirola)
 * @since v0.1
 */
public class RunningPlanBuilder extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainView mainView = new MainView();
        mainView.showView(stage, this);
    }

    public static void main(String[] args) {
        launch();
    }
}