package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportsapplications.SportsLibrary;
import de.hirola.sportsapplications.model.MovementType;
import de.hirola.sportsapplications.model.RunningUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the main view (application window) using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since v0.1
 */
public class RunningUnitViewController {
    private SportsLibrary sportsLibrary;
    private RunningUnit runningUnit; // the running unit for the view
    private List<MovementType> movementTypes; // list of all movement types
    private MovementType movementType; // selected movement type
    private final ApplicationResources applicationResources
            = ApplicationResources.getInstance(); // bundle for localization, ...

    // created with SceneBuilder
    @FXML
    private Label infoLabel;
    @FXML
    private Label movementTypeComboBoxLabel;
    @FXML
    private ComboBox<String> movementTypeComboBox;
    @FXML
    private Label movementTypePaceLabel;
    @FXML
    private Label durationTextFieldLabel;
    @FXML
    private TextField durationTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;

    public RunningUnitViewController() {}

    public void setSportsLibrary(@NotNull SportsLibrary sportsLibrary) {
        this.sportsLibrary = sportsLibrary;
        movementTypes = sportsLibrary.getMovementTypes();
        // on initialize, the library is null
        // we fill the box here
        fillMovementTypeComboBox();
    }

    @Nullable
    public RunningUnit getRunningUnit() {
        return runningUnit;
    }

    public void setRunningUnit(@Nullable RunningUnit runningUnit) {
        this.runningUnit = runningUnit;
        showRunningUnitInView();
    }

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() {
        // localisation for texte
        setLabel();
        // only numbers allowed in text field, thanks to https://stackoverflow.com/a/53876601
        durationTextField.setTextFormatter(new TextFormatter<>(c -> {
            if (!c.getControlNewText().matches("\\d*"))
                return null;
            else
                return c;
        }
        ));
    }

    @FXML
    // use for onAction by the FXML loader
    private void onAction(ActionEvent event) {
        if (event.getSource().equals(movementTypeComboBox)) {
            int index = movementTypeComboBox.getSelectionModel().getSelectedIndex();
            setMovementType(index); // set the selected movement type and show the pace for the type
        }
        if (event.getSource().equals(saveButton)) {
            saveRunningUnit();
        }
        if (event.getSource().equals(closeButton)) {
            close();
        }
    }

    private void setLabel() {
        infoLabel.setText(applicationResources.getString("runningUnitView.infoText"));
        movementTypeComboBoxLabel.setText(applicationResources.getString("runningUnitView.movementTypeLabelText"));
        durationTextFieldLabel.setText(applicationResources.getString("runningUnitView.durationLabelText"));
        saveButton.setText(applicationResources.getString("action.save"));
        closeButton.setText(applicationResources.getString("action.cancel"));
    }

    private void fillMovementTypeComboBox() {
        int index = 0;
        for (MovementType movementType: movementTypes) {
           movementTypeComboBox.getItems().add(index, movementType.getName());
           index++;
        }
        movementTypeComboBox.getSelectionModel().select(0);
        setMovementType(0);

    }

    private void setMovementType(int index) {
        if (movementTypes.size() > index) {
            movementType = movementTypes.get(index);
            double pace = movementType.getPace();
            if (pace > 0.0) {
                String labelText = applicationResources
                        .getString("runningUnitView.movementTypePaceLabelText.prefix")
                        + " "
                        + pace;
                movementTypePaceLabel.setText(labelText);
            } else {
                movementTypePaceLabel.setText("");
            }
        }
    }

    private void showRunningUnitInView() {
        if (runningUnit != null) {
            // select the movement types
            int index  = movementTypes.indexOf(runningUnit.getMovementType());
            if (index > -1 && index < movementTypes.size()) {
                movementTypeComboBox.getSelectionModel().select(index);
                setMovementType(index);
            } else {
                if (sportsLibrary.isDebugMode()) {
                    sportsLibrary.debug("Movement typ of running unit is not in sports library.");
                }
                //TODO: alert
            }
            durationTextField.setText(String.valueOf(runningUnit.getDuration()));
        }
    }

    private void saveRunningUnit() {
        // save values ton running unit
        String durationString = durationTextField.getText();
        if (durationString.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(applicationResources.getString("app.name")
                    + " "
                    + applicationResources.getString("app.version"));
            alert.setHeaderText(applicationResources.getString("alert.template.emptyDuration"));
            alert.showAndWait();
            return;
        }
        // the dialog is open to add a new running unit to entry
        if (runningUnit == null) {
            runningUnit = new RunningUnit();
        }
        try {
            runningUnit.setDuration(Long.parseLong(durationString));
        } catch (NumberFormatException exception) {
            //TODO: alert to user
            runningUnit.setDuration(0);
        }
        runningUnit.setMovementType(movementType);
        close();
    }

    private void close() {
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
