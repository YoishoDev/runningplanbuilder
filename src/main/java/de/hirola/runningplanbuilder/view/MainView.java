package de.hirola.runningplanbuilder.view;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.RunningPlanBuilder;
import de.hirola.runningplanbuilder.controller.MainViewController;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * The main view of the application.
 *
 * The view is created by SceneBuilder and using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since v0.1
 */
public class MainView {
    private final ApplicationResources applicationResources;
    private final Preferences userPreferences;

    public MainView() {
        applicationResources = ApplicationResources.getInstance();
        userPreferences = Preferences.userRoot().node(RunningPlanBuilder.class.getName());
    }

    public void showView(Stage stage, Application application) throws IOException {
        URL fxmlURL = getClass()
                .getClassLoader()
                .getResource("main-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
        Scene scene = new Scene(fxmlLoader.load());
        // transfer of parameters to the view controller
        MainViewController mainViewController = fxmlLoader.getController();
        // host services are needed to open url in web browser of the user
        mainViewController.setMainWindow(stage);
        mainViewController.setHostServices(application.getHostServices());
        // use last view parameter
        if (userPreferences.getBoolean(Global.UserPreferencesKeys.USE_LAST_VIEW_VALUES, false)) {
            stage.setWidth(userPreferences.getDouble(Global.UserPreferencesKeys.LAST_MAIN_VIEW_WIDTH,
                    Global.MainViewViewPreferences.DEFAULT_MAIN_VIEW_WIDTH));
            stage.setHeight(userPreferences.getDouble(Global.UserPreferencesKeys.LAST_MAIN_VIEW_HEIGHT,
                    Global.MainViewViewPreferences.DEFAULT_MAIN_VIEW_HEIGHT));
            stage.setX(userPreferences.getDouble(Global.UserPreferencesKeys.LAST_MAIN_VIEW_POS_X,
                    Global.MainViewViewPreferences.DEFAULT_MAIN_VIEW_HEIGHT));
            stage.setY(userPreferences.getDouble(Global.UserPreferencesKeys.LAST_MAIN_VIEW_POS_Y,
                    Global.MainViewViewPreferences.DEFAULT_MAIN_VIEW_HEIGHT));
        } else {
            // use default
            stage.setWidth(Global.MainViewViewPreferences.DEFAULT_MAIN_VIEW_WIDTH);
            stage.setHeight(Global.MainViewViewPreferences.DEFAULT_MAIN_VIEW_HEIGHT);
            // place the view in the center of the screen
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        }
        stage.setTitle(applicationResources.getString("app.name")
                + " "
                + applicationResources.getString("app.version"));
        stage.setScene(scene);
        stage.show();
    }
}
