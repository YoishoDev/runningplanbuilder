package de.hirola.runningplanbuilder.view;

import de.hirola.runningplanbuilder.RunningPlanBuilder;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * A view to edit a running unit node.
 * The view is created by SceneBuilder and using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class UnitNodeView {

    private final ApplicationResources applicationResources;

    public UnitNodeView() {
        applicationResources = ApplicationResources.getInstance();
    }

    public void showView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunningPlanBuilder.class.getResource("unit-node-view.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(applicationResources.getString("app.name")
                + " - "
                + applicationResources.getString("unitNodeView.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }
}
