package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.model.*;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.runningplanbuilder.view.RunningPlanView;
import de.hirola.sportsapplications.SportsLibrary;
import de.hirola.sportsapplications.SportsLibraryException;
import de.hirola.sportsapplications.model.RunningPlan;
import de.hirola.sportsapplications.model.RunningPlanEntry;
import de.hirola.sportsapplications.util.TemplateLoader;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the main view (application window) using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since v.0.1
 */
public class MainViewController {

    private SportsLibrary sportsLibrary;
    private RunningPlan runningPlan; // actual template for the application
    private List<RunningPlanEntry> runningPlanEntries;
    private ObservableList<RunningPlanEntryTableObject> runningPlanEntryTableObjects; // list for the table view
    private final ApplicationResources applicationResources
            = ApplicationResources.getInstance(); // bundle for localization, ...
    private HostServices hostServices;
    private RunningPlanView runningPlanView;


    // main app menu
    // created with SceneBuilder
    @FXML
    // the reference will be injected by the FXML loader
    private MenuBar mainMenuBar;
    @FXML
    // the reference will be injected by the FXML loader
    private Menu menuFile;  // file menu
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemNew;
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemOpen;
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemSave;
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemQuit;
    @FXML
    // the reference will be injected by the FXML loader
    private Menu menuEdit; // edit menu
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemEditTemplate;
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemEditPreferences;
    @FXML
    // the reference will be injected by the FXML loader
    private Menu menuHelp; // help menu
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemDebug;
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemAbout;

    // tool "menu"
    @FXML
    private Arc runningPlanTemplateNodeMenuElement;
    @FXML
    private Label runningPlanTemplateNodeLabel;
    @FXML
    private Rectangle runningEntryNodeMenuElement;
    @FXML
    private Label runningEntryNodeLabel;
    @FXML
    private Label toolMenuInfoLabel;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private VBox runningPlanEntryTableViewVBox;
    @FXML
    private TableView<RunningPlanEntryTableObject> runningPlanEntryTableView;

    public MainViewController() {}

    public SportsLibrary getSportsLibrary() {
        return sportsLibrary;
    }

    public void setHostServices(@NotNull HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() throws InstantiationException, SportsLibraryException {
        runningPlanEntries = new ArrayList<>();
        runningPlanEntryTableObjects = FXCollections.observableArrayList();
        //TODO: get debug mode from preferences
        // initialize sports library
        File appDirectory = SportsLibrary.initializeAppDirectory(Global.PACKAGE_NAME);
        sportsLibrary = SportsLibrary.getInstance(true, appDirectory, null);
        // set nodes to javax default colors
        runningPlanTemplateNodeMenuElement.setFill(Global.RUNNING_PLAN_TEMPLATE_NODE_COLOR);
        runningEntryNodeMenuElement.setFill(Global.RUNNING_UNIT_NODE_COLOR);
        // disable different menu items
        menuItemSave.setDisable(true);
        menuItemEditTemplate.setDisable(true);
        setMenuLabel();  // localisation the menu (item) labels
        setToolMenuLabel(); // localisation the tool "menu" item labels
        initializeTableView();
        mainSplitPane.prefHeightProperty().bind(runningPlanEntryTableView.heightProperty());
    }

    @FXML
    // use for onAction by the FXML loader
    private void onAction(ActionEvent event) {
        if (event.getSource().equals(menuItemNew)) {
            //TODO unsaved values - ask user
            showTemplateView();
        }
        if (event.getSource().equals(menuItemOpen)) {
            //TODO unsaved values - ask user
            importJSONFromFile();
        }
        if (event.getSource().equals(menuItemSave)) {
            exportToJSONFile();
        }
        if (event.getSource().equals(menuItemQuit)) {
            //TODO: unsaved values - ask user
            Stage stage = (Stage) mainSplitPane.getScene().getWindow();
            stage.close();
        }
        if (event.getSource().equals(menuItemEditTemplate)) {
            showTemplateView();
        }
        if (event.getSource().equals(menuItemDebug)) {
            showDebugDialog();
        }
        if (event.getSource().equals(menuItemAbout)) {
            showAboutDialog();
        }
    }

    @FXML
    // use for onMouseClicked by the FXML loader
    private void onMouseClicked(MouseEvent event) {

        if (event.getSource().equals(runningPlanTemplateNodeMenuElement)) {
            // create a new running plan template
            showTemplateView();
        }
    }

    private void setMenuLabel() {
        menuFile.setText(applicationResources.getString("mainMenuBar.menuFile"));
        menuItemNew.setText(applicationResources.getString("mainMenuBar.menuFile.menuItemNew"));
        menuItemOpen.setText(applicationResources.getString("mainMenuBar.menuFile.menuItemOpen"));
        menuItemSave.setText(applicationResources.getString("mainMenuBar.menuFile.menuItemSave"));
        menuItemQuit.setText(applicationResources.getString("mainMenuBar.menuFile.menuItemQuit"));
        menuEdit.setText(applicationResources.getString("mainMenuBar.menuEdit"));
        menuItemEditTemplate.setText(applicationResources.getString("mainMenuBar.menuEdit.menuItemTemplate"));
        menuItemEditPreferences.setText(applicationResources.getString("mainMenuBar.menuEdit.menuItemPreferences"));
        menuHelp.setText(applicationResources.getString("mainMenuBar.menuHelp"));
        menuItemDebug.setText(applicationResources.getString("mainMenuBar.menuHelp.menuItemDebug"));
        menuItemAbout.setText(applicationResources.getString("mainMenuBar.menuHelp.menuItemAbout"));
    }

    private void setToolMenuLabel() {
        runningPlanTemplateNodeLabel.setText(applicationResources.getString("mainToolMenu.newTemplate"));
        runningEntryNodeLabel.setText(applicationResources.getString("mainToolMenu.runningUnit"));
        toolMenuInfoLabel.setText(applicationResources.getString("mainToolMenu.info"));
    }

    private void initializeTableView() {
        // a placeholder, if no running entries in plan exists
        runningPlanEntryTableView.setPlaceholder(
                new Label(applicationResources.getString("mainView.table.defaultLabelText")));
        runningPlanEntryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // TODO: in this version only a single row can be selected
        runningPlanEntryTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // the table column header
        TableColumn<RunningPlanEntryTableObject, String> dayColumn
                = new TableColumn<>(applicationResources.getString("mainView.table.column.1.headerText"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("dayString"));
        TableColumn<RunningPlanEntryTableObject, String> weekColumn
                = new TableColumn<>(applicationResources.getString("mainView.table.column.2.headerText"));
        weekColumn.setCellValueFactory(new PropertyValueFactory<>("weekString"));
        TableColumn<RunningPlanEntryTableObject, String> durationColumn
                = new TableColumn<>(applicationResources.getString("mainView.table.column.3.headerText"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationString"));
        TableColumn<RunningPlanEntryTableObject, String> runningUnitsColumn
                = new TableColumn<>(applicationResources.getString("mainView.table.column.4.headerText"));
        runningUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("runningUnitsString"));
        runningPlanEntryTableView.getColumns().add(dayColumn);
        runningPlanEntryTableView.getColumns().add(weekColumn);
        runningPlanEntryTableView.getColumns().add(durationColumn);
        runningPlanEntryTableView.getColumns().add(runningUnitsColumn);
    }

    private void showTemplateView() {
        // show dialog
        if (runningPlanView == null) {
            runningPlanView = new RunningPlanView();
        }
        try {
            RunningPlanViewController viewController = runningPlanView.showView(mainSplitPane, runningPlan);
            runningPlan = viewController.getRunningPlan();
            if (runningPlan != null) {
                // enable different menu item
                menuItemSave.setDisable(false);
                menuItemEditTemplate.setDisable(false);
            }
        } catch (IOException exception) {
            //TODO: Alert
            exception.printStackTrace();
        }
    }

    private void showDebugDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(applicationResources.getString("app.name")
                + " "
                + applicationResources.getString("app.version"));
        alert.setHeaderText(applicationResources.getString("debug.alert.header"));
        VBox vBox = new VBox();
        Label label = new Label(applicationResources.getString("debug.alert.info"));
        Hyperlink hyperlink = new Hyperlink(applicationResources.getString("debug.alert.info.url"));
        hyperlink.setOnAction((event) -> hostServices.showDocument(hyperlink.getText()));
        vBox.getChildren().addAll(label, hyperlink);
        alert.getDialogPane().contentProperty().set(vBox);
        alert.showAndWait();
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(applicationResources.getString("app.name")
                + " "
                + applicationResources.getString("app.version"));
        alert.setHeaderText(applicationResources.getString("about.alert.header"));
        VBox vBox = new VBox();
        Label label = new Label(applicationResources.getString("about.alert.info"));
        Hyperlink hyperlink = new Hyperlink(applicationResources.getString("about.alert.info.url"));
        hyperlink.setOnAction((event) -> hostServices.showDocument(hyperlink.getText()));
        vBox.getChildren().addAll(label, hyperlink);
        alert.getDialogPane().contentProperty().set(vBox);
        alert.showAndWait();
    }

    private void importJSONFromFile() {
        // open system file dialog
    }

    private void exportToJSONFile() {
        if (runningPlan != null) {
            // get all running entries from editor
            //runningPlan.setEntries(editorViewController.getRunningPlanEntries());
            // set the home as initial directory
            //TODO: set last used dir
            String initialDirectoryPathString;
            try {
                initialDirectoryPathString = System.getProperty("user.home");
            } catch (SecurityException exception) {
                initialDirectoryPathString = "/"; // can be used on linux, macOS and Windows
            }
            // get the file name from running plan name
            String fileName = runningPlan.getName();
            if (fileName.isEmpty()) {
                applicationResources.getString("export.file.name");
            }
            // get the export directory with file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(initialDirectoryPathString));
            fileChooser.setSelectedExtensionFilter(Global.TEMPLATE_FILE_EXTENSION_FILTER);
            fileChooser.setInitialFileName(fileName + Global.TEMPLATE_FILE_EXTENSION);
            File jsonFile = fileChooser.showSaveDialog(mainSplitPane.getScene().getWindow());
            try {
                TemplateLoader templateLoader = new TemplateLoader(sportsLibrary);
                templateLoader.exportRunningPlanToJSON(runningPlan, jsonFile);
            } catch (SportsLibraryException exception) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(applicationResources.getString("app.name")
                        + " "
                        + applicationResources.getString("app.version"));
                alert.setHeaderText(applicationResources.getString("alert.export.failed"));
                alert.showAndWait();
                if (sportsLibrary.isDebugMode()) {
                    sportsLibrary.debug(exception, "Export to JSON failed.");
                }
            }
        }
    }
}
