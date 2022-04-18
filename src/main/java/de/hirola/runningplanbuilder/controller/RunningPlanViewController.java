package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportslibrary.model.RunningPlan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the main view (application window) using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class RunningPlanViewController {

    private RunningPlan runningPlan;
    private int orderNumber = 1;
    private final ApplicationResources applicationResources
            = ApplicationResources.getInstance(); // bundle for localization, ...
    private MainViewController mainViewController;

    // created with SceneBuilder
    @FXML
    private Label infoLabel;
    @FXML
    private Label nameTextFieldLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label orderNumberComboBoxLabel;
    @FXML
    private ComboBox<String> orderNumberComboBox;
    @FXML
    private Label remarksTextFieldLabel;
    @FXML
    private TextArea remarksTextArea;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;

    public RunningPlanViewController() {}

    public void setMainViewController(@NotNull MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        runningPlan = mainViewController.getRunningPlan(); // can be null
        showRunningPlanInView(); // show the data from template (if not null)
    }

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() {
        setLabel();
        fillOrderNumberComboBox();
        showRunningPlanInView(); // if unit not null, given from node
    }

    @FXML
    // use for onAction by the FXML loader
    private void onAction(ActionEvent event) {
        if (event.getSource().equals(saveButton)) {
            saveRunningPlan();
        }
        if (event.getSource().equals(closeButton)) {
            close();
        }
        if (event.getSource().equals(orderNumberComboBox)) {
            // combo box index begins with 0
            orderNumber = orderNumberComboBox.getSelectionModel().getSelectedIndex() + 1;
        }
    }

    private void setLabel() {
        infoLabel.setText(applicationResources.getString("templateView.infoText"));
        nameTextFieldLabel.setText(applicationResources.getString("templateView.nameLabelText"));
        orderNumberComboBoxLabel.setText(applicationResources.getString("templateView.orderNumberLabelText"));
        remarksTextFieldLabel.setText(applicationResources.getString("templateView.remarksLabelText"));
        saveButton.setText(applicationResources.getString("templateView.saveButtonText"));
        closeButton.setText(applicationResources.getString("templateView.closeButtonText"));
    }

    private void fillOrderNumberComboBox() {
        for (int i = 0; i < Global.MAX_ORDER_NUMBER; i++) {
            orderNumberComboBox.getItems().add(i, String.valueOf(i + 1));
        }
        // select the monday
        orderNumberComboBox.getSelectionModel().select(0);
    }

    private void showRunningPlanInView() {
        if (runningPlan != null) {
            nameTextField.setText(runningPlan.getName());
            remarksTextArea.setText(runningPlan.getRemarks());
            orderNumber = runningPlan.getOrderNumber();
            // select the order number in combo box
            if (orderNumberComboBox.getItems().size() < orderNumber
                    && Global.MAX_ORDER_NUMBER >= orderNumber) {
                // the index of the combo box begins with 0
                int index = orderNumber - 1;
                orderNumberComboBox.getSelectionModel().select(index);
            } else {
                //TODO info to the user
                orderNumberComboBox.getSelectionModel().select(0);
            }
        }
    }

    private void saveRunningPlan() {
        boolean nameTextFieldIsEmpty = nameTextField.getText().isEmpty();
        boolean remarksTextFieldIsEmpty = remarksTextArea.getText().isEmpty();
        if (runningPlan == null) {
            // a new template should be created
            if (nameTextFieldIsEmpty) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(applicationResources.getString("app.name")
                        + " "
                        + applicationResources.getString("app.version"));
                alert.setHeaderText(applicationResources.getString("alert.template.emptyName"));
                alert.showAndWait();
                return;
            }
            runningPlan = new RunningPlan();
            runningPlan.setName(nameTextField.getText());
            runningPlan.setRemarks(remarksTextArea.getText());
        } else {
            // an existing template should be updated
           if (!nameTextFieldIsEmpty) {
               // update the name only if not empty
               runningPlan.setName(nameTextField.getText());
           }
           if (remarksTextFieldIsEmpty && !runningPlan.getRemarks().isEmpty()) {
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.setTitle(applicationResources.getString("app.name")
                       + " "
                       + applicationResources.getString("app.version"));
               alert.setHeaderText(applicationResources.getString("alert.template.clearRemarks"));
               ButtonType okButton = new ButtonType(applicationResources
                       .getString("action.ok"), ButtonBar.ButtonData.YES);
               ButtonType cancelButton = new ButtonType(applicationResources
                       .getString("action.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
               alert.getButtonTypes().setAll(okButton, cancelButton);
               alert.showAndWait().ifPresent(type -> {
                   if (type == ButtonType.OK) {
                       runningPlan.setRemarks(remarksTextArea.getText());
                   } else {
                       alert.close();
                   }
               });
           } else {
               runningPlan.setRemarks(remarksTextArea.getText());
           }
        }
        runningPlan.setOrderNumber(orderNumber);
        mainViewController.setRunningPlan(runningPlan);
        close();
    }

    private void close() {
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}