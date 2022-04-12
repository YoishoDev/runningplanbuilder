package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.sportslibrary.util.RunningPlanTemplate;
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
public class TemplateViewController {

    private RunningPlanTemplate runningPlanTemplate;
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
    private TextField remarksTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;

    public TemplateViewController() {}

    public void setMainViewController(@NotNull MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        runningPlanTemplate = mainViewController.getRunningPlanTemplate(); // can be null
        showRunningPlanTemplateInView(); // show the data from template (if not null)
    }

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() {
        setLabel();
        fillOrderNumberComboBox();
        showRunningPlanTemplateInView(); // if unit not null, given from node
    }

    @FXML
    // use for onAction by the FXML loader
    private void onAction(ActionEvent event) {
        if (event.getSource().equals(saveButton)) {
            saveRunningPlanTemplate();
        }
        if (event.getSource().equals(closeButton)) {
            // get a handle to the stage
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
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

    private void showRunningPlanTemplateInView() {
        if (runningPlanTemplate != null) {
            nameTextField.setText(runningPlanTemplate.getName());
            remarksTextField.setText(runningPlanTemplate.getRemarks());
            orderNumber = runningPlanTemplate.getOrderNumber();
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

    private void saveRunningPlanTemplate() {
        boolean nameTextFieldIsEmpty = nameTextField.getText().isEmpty();
        boolean remarksTextFieldIsEmpty = remarksTextField.getText().isEmpty();
        if (runningPlanTemplate == null) {
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
            runningPlanTemplate = new RunningPlanTemplate();
            runningPlanTemplate.setName(nameTextField.getText());
            runningPlanTemplate.setRemarks(remarksTextField.getText());
        } else {
            // a existing template should be updated
           if (!nameTextFieldIsEmpty) {
               // update the name only if not empty
               runningPlanTemplate.setName(nameTextField.getText());
           }
           if (remarksTextFieldIsEmpty && !runningPlanTemplate.getRemarks().isEmpty()) {
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
                       runningPlanTemplate.setRemarks(remarksTextField.getText());
                   } else {
                       alert.close();
                   }
               });
           }
        }
        runningPlanTemplate.setOrderNumber(orderNumber);
        mainViewController.setRunningPlanTemplate(runningPlanTemplate);
    }
}
