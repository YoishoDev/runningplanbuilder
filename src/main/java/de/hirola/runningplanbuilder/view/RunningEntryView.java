package de.hirola.runningplanbuilder.view;

import de.hirola.runningplanbuilder.controller.RunningEntryViewController;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportslibrary.SportsLibrary;
import de.hirola.sportslibrary.model.RunningPlanEntry;
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
 * A view to edit a running entry.
 * This dialog is opened modal and waits for the user to close it.
 *
 * The view is created by SceneBuilder and using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since v.0.1
 */
public class RunningEntryView {

    private final SportsLibrary sportsLibrary;
    private final ApplicationResources applicationResources;

    public RunningEntryView(SportsLibrary sportsLibrary) {
        this.sportsLibrary = sportsLibrary;
        applicationResources = ApplicationResources.getInstance();
    }

    public RunningEntryViewController showView(Node parent, @NotNull RunningPlanEntry runningPlanEntry) throws IOException {
        URL fxmlURL = getClass()
                .getClassLoader()
                .getResource("entry-node-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        // transfer of parameters to the view controller
        RunningEntryViewController runningEntryViewController = fxmlLoader.getController();
        runningEntryViewController.setSportsLibrary(sportsLibrary);
        runningEntryViewController.setRunningPlanEntry(runningPlanEntry);
        stage.setTitle(applicationResources.getString("app.name")
                + " - "
                + applicationResources.getString("entryNodeView.title"));
        stage.initOwner(parent.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait(); // wait until user closed the dialog

        return runningEntryViewController; // return the controller back to caller
    }
}
