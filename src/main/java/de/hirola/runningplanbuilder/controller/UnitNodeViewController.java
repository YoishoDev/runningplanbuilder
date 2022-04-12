package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportslibrary.model.MovementType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the main view (application window) using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class UnitNodeViewController {

    private final ApplicationResources applicationResources
            = ApplicationResources.getInstance(); // bundle for localization, ...

    // created with SceneBuilder
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label infoLabel;
    @FXML
    private Label weekDayComboBoxLabel;
    @FXML
    private ComboBox<String> weekDayComboBox;
    @FXML
    private Label weekComboBoxLabel;
    @FXML
    private ComboBox<String> weekComboBox;
    @FXML
    private Label movementTypeComboBoxLabel;
    @FXML
    private ComboBox<MovementType> movementTypeComboBox;
    @FXML
    private Label durationTextFieldLabel;
    @FXML
    private TextField durationTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;

    public UnitNodeViewController() {}

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() {
        // localisation for texte
        setLabel();
        // fill combo boxes
        fillWeekDayComboBox();
        fillWeekComboBox();
        fillMovementTypeComboBox();
        // only numbers allowed in text field, thanks to https://stackoverflow.com/a/53876601
        durationTextField.setTextFormatter(new TextFormatter<>(c -> {
            if (!c.getControlNewText().matches("\\d*"))
                return null;
            else
                return c;
        }
        ));
        showRunningUnitInView(); // if unit not null, given from node
    }

    @FXML
    // use for onAction by the FXML loader
    private void onAction(ActionEvent event) {
        if (event.getSource().equals(saveButton)) {
            saveRunningUnit();
        }
        if (event.getSource().equals(closeButton)) {
            // get a handle to the stage
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
        if (event.getSource().equals(weekDayComboBox)) {
            int weekday = weekDayComboBox.getSelectionModel().getSelectedIndex();
            System.out.println(weekday + 1);
        }
    }

    private void setLabel() {
        infoLabel.setText(applicationResources.getString("unitNodeView.infoText"));
        weekDayComboBoxLabel.setText(applicationResources.getString("unitNodeView.weekDayLabelText"));
        weekComboBoxLabel.setText(applicationResources.getString("unitNodeView.weekLabelText"));
        movementTypeComboBoxLabel.setText(applicationResources.getString("unitNodeView.movementTypeLabelText"));
        durationTextFieldLabel.setText(applicationResources.getString("unitNodeView.durationLabelText"));
        saveButton.setText(applicationResources.getString("unitNodeView.saveButtonText"));
        closeButton.setText(applicationResources.getString("unitNodeView.closeButtonText"));
    }

    private void fillWeekDayComboBox() {
        weekDayComboBox.getItems().add(0, applicationResources.getString("monday"));
        weekDayComboBox.getItems().add(1, applicationResources.getString("tuesday"));
        weekDayComboBox.getItems().add(2, applicationResources.getString("wednesday"));
        weekDayComboBox.getItems().add(3, applicationResources.getString("thursday"));
        weekDayComboBox.getItems().add(4, applicationResources.getString("friday"));
        weekDayComboBox.getItems().add(5, applicationResources.getString("saturday"));
        weekDayComboBox.getItems().add(6, applicationResources.getString("sunday"));
        // select the monday
        weekDayComboBox.getSelectionModel().select(0);
    }

    private void fillWeekComboBox() {
        for (int i = 0; i < Global.MAX_COUNT_OF_WEEKS; i++) {
            weekComboBox.getItems().add(i,
                    applicationResources.getString("misc.week") + " " + (i + 1));
        }
        // select the first week
        weekComboBox.getSelectionModel().select(0);
    }

    private void fillMovementTypeComboBox() {

        weekDayComboBox.getItems().add(6, applicationResources.getString("sunday"));
        // select the monday
        weekDayComboBox.getSelectionModel().select(0);
    }

    private void showRunningUnitInView() {

    }

    private void saveRunningUnit() {

    }
}
