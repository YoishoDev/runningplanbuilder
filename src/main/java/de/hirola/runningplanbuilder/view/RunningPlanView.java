package de.hirola.runningplanbuilder.view;

import de.hirola.runningplanbuilder.controller.MainViewController;
import de.hirola.runningplanbuilder.controller.RunningEntryViewController;
import de.hirola.runningplanbuilder.controller.RunningPlanViewController;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportslibrary.model.RunningPlan;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * A view to create and edit a running plan template.
 * The view is created by SceneBuilder and using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since v.0.1
 */
public class RunningPlanView {

    private final ApplicationResources applicationResources;

    public RunningPlanView() {
        applicationResources = ApplicationResources.getInstance();
    }

    public RunningPlanViewController showView(Node parent, @Nullable RunningPlan runningPlan) throws IOException {
        URL fxmlURL = getClass()
                .getClassLoader()
                .getResource("running-plan-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        // transfer of parameters to the main view controller
        RunningPlanViewController runningPlanViewController = fxmlLoader.getController();
        // needed to get data from main view controller
        runningPlanViewController.setRunningPlan(runningPlan);
        stage.setTitle(applicationResources.getString("app.name")
                + " - "
                + applicationResources.getString("templateView.title"));
        stage.initOwner(parent.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait(); // wait until user closed the dialog

        return runningPlanViewController; // return the controller back to caller

    }
}
