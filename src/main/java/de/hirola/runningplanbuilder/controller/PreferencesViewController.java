package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.RunningPlanBuilder;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportsapplications.SportsLibrary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
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
    private SportsLibrary sportsLibrary;
    private Preferences userPreferences = null;
    private List<String> localizationKeys;

    // created with SceneBuilder
    // tab common
    @FXML
    private Label debugModeCheckBoxLabel;
    @FXML
    private CheckBox debugModeCheckBox;
    @FXML
    private Label debugModeCheckBoxInfoLabel;

    // tab look & feel
    @FXML
    private Label localizationComboBoxLabel;
    @FXML
    private ComboBox<String> localizationComboBox;
    @FXML
    private Label localizationComboBoxInfoLabel;

    // tab import / export
    @FXML
    private Label useLastImportDirCheckBoxLabel;
    @FXML
    private CheckBox useLastImportDirCheckBox;

    public PreferencesViewController() {}

    public void setSportsLibrary(@NotNull SportsLibrary sportsLibrary) {
        this.sportsLibrary = sportsLibrary;
    }

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() {
        localizationKeys = new ArrayList<>();
        try {
            userPreferences = Preferences.userRoot().node(RunningPlanBuilder.class.getName());
        } catch (SecurityException exception) {
            if (sportsLibrary.isDebugMode()) {
                sportsLibrary.debug(exception, "Error while loading the user preferences.");
            }
        }
        if (userPreferences != null) {
            debugModeCheckBox.setSelected(userPreferences.getBoolean(Global.UserPreferencesKeys.USE_DEBUG_MODE,
                    false));
            useLastImportDirCheckBox.setSelected(userPreferences.getBoolean(Global.UserPreferencesKeys.USE_LAST_DIRECTORY,
                    false));
        }
        // localisation for label
        setLabel();
        // add available localizations to combo box
        fillLocalizationComboBox();
    }

    @FXML
    // use for onAction by the FXML loader
    private void onAction(ActionEvent event) {
        if (userPreferences != null) {
            if (event.getSource().equals(localizationComboBox)) {
                // get the selected locale
                int index = localizationComboBox.getSelectionModel().getSelectedIndex();
                if (index > -1 && index < localizationKeys.size()) {
                    String localizationKey = localizationKeys.get(index);
                    // save to user preferences
                    userPreferences.put(Global.UserPreferencesKeys.LOCALE, localizationKey);
                    // info to user
                    localizationComboBoxInfoLabel.setText(applicationResources
                            .getString("preferencesView.localizationComboBoxInfoLabel.text"));
                }
            }
            if (event.getSource().equals(debugModeCheckBox)) {
                userPreferences.putBoolean(Global.UserPreferencesKeys.USE_DEBUG_MODE,
                        debugModeCheckBox.isSelected());
            }
            if (event.getSource().equals(useLastImportDirCheckBox)) {
                userPreferences.putBoolean(Global.UserPreferencesKeys.USE_LAST_DIRECTORY,
                        useLastImportDirCheckBox.isSelected());
            }
        }
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
        // load available localizations from properties
        Properties localizationProperties = new Properties();
        try {
            InputStream in = RunningPlanBuilder.class.getResourceAsStream(Global.LOCALIZATION_PROPERTIES);
            if (in != null) {
                localizationProperties.load(in);
                Enumeration<Object> enumeration = localizationProperties.keys();
                while (enumeration.hasMoreElements()) {
                    // add the key to the list
                    String key = (String) enumeration.nextElement();
                    localizationKeys.add(key);
                    // add the value to the combo box
                    localizationComboBox.getItems().add((String) localizationProperties.get(key));

                }
                // select the actual locale
                String actualLocaleString = "";
                if (userPreferences != null) {
                    actualLocaleString = userPreferences.get(Global.UserPreferencesKeys.LOCALE,"en");
                }
                int index = localizationKeys.indexOf(actualLocaleString);
                if (index > -1) {
                    if (index < localizationComboBox.getItems().size()) {
                        localizationComboBox.getSelectionModel().select(index);
                    }
                } else {
                    if (localizationComboBox.getItems().size() > 0) {
                        localizationComboBox.getSelectionModel().select(0);
                    }
                }
            }
        } catch(IOException exception){
            // try to get the actual used locale
            exception.printStackTrace();
        }
    }
}
