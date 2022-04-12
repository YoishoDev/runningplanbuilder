package de.hirola.runningplanbuilder.view;

import de.hirola.runningplanbuilder.RunningPlanBuilder;
import de.hirola.runningplanbuilder.controller.MainViewController;
import de.hirola.runningplanbuilder.controller.TemplateViewController;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportslibrary.util.RunningPlanTemplate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * A view to create and edit a running plan template.
 * The view is created by SceneBuilder and using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class TemplateView {

    private final ApplicationResources applicationResources;
    private final MainViewController mainViewController;

    public TemplateView(@NotNull MainViewController mainViewController,
                        @NotNull ApplicationResources applicationResources) {
        this.applicationResources = applicationResources;
        this.mainViewController = mainViewController;
    }

    public void showView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunningPlanBuilder.class.getResource("template-view.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        // transfer of parameters to the main view controller
        TemplateViewController templateViewController = fxmlLoader.getController();
        // needed to get data from main view controller
        templateViewController.setMainViewController(mainViewController);
        stage.setTitle(applicationResources.getString("app.name")
                + " - "
                + applicationResources.getString("templateView.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
