package de.hirola.runningplanbuilder.view;

import de.hirola.runningplanbuilder.controller.RunningUnitViewController;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportsapplications.SportsLibrary;
import de.hirola.sportsapplications.model.RunningUnit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

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
public class RunningUnitView {

    private final SportsLibrary sportsLibrary;
    private final ApplicationResources applicationResources;

    public RunningUnitView(SportsLibrary sportsLibrary) {
        this.sportsLibrary = sportsLibrary;
        applicationResources = ApplicationResources.getInstance();
    }

    public RunningUnitViewController showViewModal(Node parent, @Nullable RunningUnit runningUnit) throws IOException {
        URL fxmlURL = getClass()
                .getClassLoader()
                .getResource("running-unit-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());

        // transfer of parameters to the view controller
        RunningUnitViewController runningUnitViewController = fxmlLoader.getController();
        runningUnitViewController.setSportsLibrary(sportsLibrary);
        runningUnitViewController.setRunningUnit(runningUnit); // can be null
        stage.setTitle(applicationResources.getString("app.name")
                + " - "
                + applicationResources.getString("runningUnitView.title"));
        stage.initOwner(parent.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait(); // wait until user closed the dialog

        return runningUnitViewController; // Return the controller back to caller
    }
}
