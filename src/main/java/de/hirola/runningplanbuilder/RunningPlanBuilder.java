package de.hirola.runningplanbuilder;

import de.hirola.runningplanbuilder.controller.MainViewController;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportslibrary.SportsLibrary;
import de.hirola.sportslibrary.SportsLibraryException;
import de.hirola.sportslibrary.database.DataRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * The start class of the application.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class RunningPlanBuilder extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ApplicationResources applicationResources = ApplicationResources.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(RunningPlanBuilder.class.getResource("main-view.fxml"));
        // TODO: use last size
        // Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        // transfer of parameters to the view controller
        MainViewController mainViewController = fxmlLoader.getController();
        // host services are needed to open url in web browser of the user
        mainViewController.setHostServices(getHostServices());
        stage.setTitle(applicationResources.getString("app.name")
                + " "
                + applicationResources.getString("app.version"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}