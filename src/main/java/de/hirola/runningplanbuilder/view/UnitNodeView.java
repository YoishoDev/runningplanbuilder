package de.hirola.runningplanbuilder.view;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.controller.UnitNodeViewController;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportslibrary.SportsLibrary;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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

    private final SportsLibrary sportsLibrary;
    private final ApplicationResources applicationResources;

    public UnitNodeView(SportsLibrary sportsLibrary) {
        this.sportsLibrary = sportsLibrary;
        applicationResources = ApplicationResources.getInstance();
    }

    public void showView() throws IOException {
        URL fxmlURL = getClass()
                .getClassLoader()
                .getResource("unit-node-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        // transfer of parameters to the view controller
        UnitNodeViewController unitNodeViewController = fxmlLoader.getController();
        unitNodeViewController.setSportsLibrary(sportsLibrary);
        stage.setTitle(applicationResources.getString("app.name")
                + " - "
                + applicationResources.getString("unitNodeView.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
