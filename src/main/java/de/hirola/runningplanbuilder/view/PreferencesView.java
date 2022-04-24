package de.hirola.runningplanbuilder.view;

import de.hirola.runningplanbuilder.controller.PreferencesViewController;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportsapplications.SportsLibrary;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * A view to edit a running unit.
 * This dialog is opened modal and waits for the user to close it.
 *
 * The view is created by SceneBuilder and using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since v.0.1
 */
public class PreferencesView {
    private final ApplicationResources applicationResources;

    public PreferencesView() {
        applicationResources = ApplicationResources.getInstance();
    }

    public PreferencesViewController showViewModal(@NotNull Node parent, @NotNull SportsLibrary sportsLibrary)
            throws IOException {
        URL fxmlURL = getClass()
                .getClassLoader()
                .getResource("preferences-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());

        // transfer of parameters to the view controller
        PreferencesViewController preferencesViewController = fxmlLoader.getController();
        preferencesViewController.setSportsLibrary(sportsLibrary);
        stage.setTitle(applicationResources.getString("app.name")
                + " - "
                + applicationResources.getString("preferencesView.title"));
        stage.initOwner(parent.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait(); // wait until user closed the dialog

        return preferencesViewController; // Return the controller back to caller
    }
}
