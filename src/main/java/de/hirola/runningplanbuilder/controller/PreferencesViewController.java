package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.util.ApplicationResources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.prefs.Preferences;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the preferences view using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since v.0.1
 */
public class PreferencesViewController {

    private final ApplicationResources applicationResources
            = ApplicationResources.getInstance(); // bundle for localization, ...
    private Preferences userPreferences;

    // created with SceneBuilder
    // tab common
    @FXML
    private Label debugModeCheckBoxLabel;
    @FXML
    private CheckBox debugModeCheckBox;
    // tab look & feel
    @FXML
    private Label localizationComboBoxLabel;
    @FXML
    private ComboBox<String> localizationComboBox;
    // tab import / export
    @FXML
    private Label useLastImportDirCheckBoxLabel;
    @FXML
    private CheckBox useLastImportDirCheckBox;

    public PreferencesViewController() {}

    public Preferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(@NotNull Preferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() {
        // localisation for label
        setLabel();
        // add available localizations to combo box
        fillLocalizationComboBox();
    }

    @FXML
    // use for onAction by the FXML loader
    private void onAction(ActionEvent event) {
    }

    private void setLabel() {
        debugModeCheckBoxLabel.setText(applicationResources
                .getString("preferencesView.debugModeCheckBoxLabel.text"));
        localizationComboBoxLabel.setText(applicationResources
                .getString("preferencesView.localizationComboBoxLabel.text"));
        useLastImportDirCheckBoxLabel.setText(applicationResources
                .getString("preferencesView.useLastImportDirCheckBoxLabel.text"));
    }

    private void fillLocalizationComboBox() {

    }
}
