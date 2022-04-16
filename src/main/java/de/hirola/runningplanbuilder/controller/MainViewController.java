package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.model.*;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.runningplanbuilder.view.TemplateView;
import de.hirola.sportslibrary.SportsLibrary;
import de.hirola.sportslibrary.SportsLibraryException;
import de.hirola.sportslibrary.model.MovementType;
import de.hirola.sportslibrary.util.RunningPlanTemplate;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the main view (application window) using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class MainViewController {

    private SportsLibrary sportsLibrary;
    private RunningPlanTemplate runningPlanTemplate; // actual template for the application
    private final ApplicationResources applicationResources
            = ApplicationResources.getInstance(); // bundle for localization, ...
    private HostServices hostServices;
    private TemplateView templateView;
    private EditorViewController editorViewController; // handler for editor pane
    private PointNode startNode = null; // there can only be one in editor
    private PointNode stopNode = null; // there can only be one in editor
    private EditorNode lastAddedNode = null;
    private Point2D lastNodePoint = null; // position of last added running unit node
    private int addedNodes = 0; // count oo added nodes
    private boolean nodesCanBeAdded = false;
    private int nodeLineCount = 1; // increase while line break
    private boolean isNewLine = false;

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
    private Rectangle runningUnitNodeMenuElement;
    @FXML
    private Circle stopNodeMenuElement;
    @FXML
    private Label runningUnitNodeLabel;
    @FXML
    private Label stopNodeMenuElementLabel;
    @FXML
    private Label toolMenuInfoLabel;

    // split pane elements
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private AnchorPane toolMenuAnchorPane;
    @FXML
    private AnchorPane editorAnchorPane;

    public MainViewController() {}

    public SportsLibrary getSportsLibrary() {
        return sportsLibrary;
    }

    public void setHostServices(@NotNull HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @Nullable
    public RunningPlanTemplate getRunningPlanTemplate() {
        return runningPlanTemplate;
    }

    public void setRunningPlanTemplate(@Nullable RunningPlanTemplate runningPlanTemplate) {
        this.runningPlanTemplate = runningPlanTemplate;
        nodesCanBeAdded = runningPlanTemplate != null;
        if (nodesCanBeAdded) {
            // enable different menu item
            menuItemSave.setDisable(false);
            menuItemEditTemplate.setDisable(false);
        }
    }

    public List<MovementType> getMovementTypes() {
        return null;
    }

    public void nodeWasDeleted(EditorNode node) {
        addedNodes--;
        // new nodes can be added if the stop node has been deleted
        if (node instanceof PointNode) {
            if (!((PointNode) node).isStartNode()) {
                stopNode = null;
                nodesCanBeAdded = true;
            } else {
                // start point was deleted
                // reset all values
                addedNodes = 0;
                nodeLineCount = 0;
                startNode = null;
                lastNodePoint = null;
            }
        } else {
            // set the new node position
            lastAddedNode = node.getPredecessorNode();
        }
    }

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() throws InstantiationException, SportsLibraryException {
        //TODO: get debug mode from preferences
        // initialize sports library
        File appDirectory = SportsLibrary.initializeAppDirectory(Global.PACKAGE_NAME);
        sportsLibrary = SportsLibrary.getInstance(true, appDirectory, null);
        // initialize the controller for editor pane
        editorViewController = new EditorViewController(this, editorAnchorPane);
        // set nodes to javax default colors
        runningPlanTemplateNodeMenuElement.setFill(Global.RUNNING_PLAN_TEMPLATE_NODE_COLOR);
        stopNodeMenuElement.setFill(Global.STOP_CIRCLE_COLOR);
        runningUnitNodeMenuElement.setFill(Global.RUNNING_UNIT_NODE_COLOR);
        // disable different menu items
        menuItemSave.setDisable(true);
        menuItemEditTemplate.setDisable(true);
        // localisation the menu (item) labels
        setMenuLabel();
        // localisation the tool "menu" item labels
        setToolMenuLabel();
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
        runningUnitNodeLabel.setText(applicationResources.getString("mainToolMenu.runningUnit"));
        stopNodeMenuElementLabel.setText(applicationResources.getString("mainToolMenu.stop"));
        toolMenuInfoLabel.setText(applicationResources.getString("mainToolMenu.info"));
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

        // create a customized rectangle object in editor pane
        if (event.getSource().equals(runningUnitNodeMenuElement) && nodesCanBeAdded) {
            // add the start node first
            if (startNode == null) {
                // create the start node
                startNode = new PointNode(true);
                // set the position in pane
                Point2D nodePos = getNodePosition();
                startNode.setCenterX(nodePos.getX());
                startNode.setCenterY(nodePos.getY());
                // create a new running node
                // get the position for the next node
                nodePos = getNodePosition();
                RunningUnitNode runningUnitNode = new RunningUnitNode(nodePos.getX(), nodePos.getY());
                // set the start node as predecessor
                runningUnitNode.setPredecessorNode(startNode);
                // set the running unit node as neighbour
                startNode.setNeighborNode(runningUnitNode);
                // create the connection between both nodes
                createNodeConnection(startNode, runningUnitNode);
                // add both nodes to editor pane
                editorAnchorPane.getChildren().add(startNode);
                editorAnchorPane.getChildren().add(runningUnitNode);
                // remember the last added node
                lastAddedNode = runningUnitNode;
                // register both nodes with the editor controller
                editorViewController.registerNode(startNode);
                editorViewController.registerNode(runningUnitNode);
                return;
            }
            // add the next running unit node
            Point2D nodePos = getNodePosition();
            RunningUnitNode runningUnitNode = new RunningUnitNode(nodePos.getX(), nodePos.getY());
            // set the previous node as predecessor
            runningUnitNode.setPredecessorNode(lastAddedNode);
            // create the connection between both nodes
            createNodeConnection(lastAddedNode, runningUnitNode);
            // add the node to editor pane
            editorAnchorPane.getChildren().add(runningUnitNode);
            // remember the last added node
            lastAddedNode = runningUnitNode;
            // register the node with the editor controller
            editorViewController.registerNode(runningUnitNode);
        }

        if (event.getSource().equals(stopNodeMenuElement) && stopNode == null) {
            // create a stop node
            stopNode = new PointNode(false);
            // set the position in pane
            Point2D nodePos = getNodePosition();
            stopNode.setCenterX(nodePos.getX());
            stopNode.setCenterY(nodePos.getY() + Global.RUNNING_UNIT_NODE_HEIGHT / 2);
            // create the connection between both nodes
            createNodeConnection(lastAddedNode, stopNode);
            // add both nodes to editor pane
            editorAnchorPane.getChildren().add(stopNode);
            // nodes cannot be added now
            nodesCanBeAdded = false;
            // remember the last added node
            lastAddedNode = stopNode;
            // register both nodes with the editor controller
            editorViewController.registerNode(stopNode);
        }
    }

    private Point2D getNodePosition() {
        // get the "correct" position in the editor pane
        Point2D nodePos;
        if (lastNodePoint == null) {
            // get the first position from Global depending on size of start node
            double posX = Global.EDITOR_PANE_NODE_START_POINT.getX();
            double posY = Global.EDITOR_PANE_NODE_START_POINT.getY()
                    + Global.RUNNING_UNIT_NODE_HEIGHT / 2;
            nodePos = new Point2D(posX,posY);
        } else {
            // calculate the position of next node
            if (addedNodes == 1) {
                // between start node and first running unit node add only the space from Global
                nodePos = new Point2D(lastNodePoint.getX() + Global.SPACE_BETWEEN_NODES,
                        Global.EDITOR_PANE_NODE_START_POINT.getY());
            } else {
                double posX = lastNodePoint.getX() + Global.RUNNING_UNIT_NODE_WITH + Global.SPACE_BETWEEN_NODES;
                double posY = lastNodePoint.getY();
                if (editorAnchorPane.getChildren().size() == 2) {
                    // correct the y pos from start node
                    posY = Global.EDITOR_PANE_NODE_START_POINT.getY() - Global.RUNNING_UNIT_NODE_HEIGHT / 2 + Global.CIRCLE_RADIUS / 2
                            + Global.EDITOR_PANE_NODE_START_POINT.getY();
                }
                // at the "end" of editor pane let's do a line break
                double maxX = editorAnchorPane.getLayoutBounds().getMaxX();
                if (posX >= maxX - Global.RUNNING_UNIT_NODE_WITH) {
                    posX = Global.EDITOR_PANE_NODE_START_POINT.getX();
                    posY = Global.EDITOR_PANE_NODE_START_POINT.getY()
                            + (nodeLineCount * Global.RUNNING_UNIT_NODE_HEIGHT)
                            + (nodeLineCount * Global.SPACE_BETWEEN_NODES);
                    // increase the line count
                    nodeLineCount++;
                    // set the flag for new line
                    isNewLine = true;
                }
                nodePos = new Point2D(posX,posY);
            }
        }
        addedNodes++;
        lastNodePoint = nodePos;
        return nodePos;
    }

    private void createNodeConnection(EditorNode firstNode, EditorNode secondNode) {
        // get the center of both nodes
        Bounds firstNodeBounds = ((Shape) firstNode).getBoundsInLocal();
        Bounds secondNodeBounds = ((Shape) secondNode).getBoundsInLocal();
        Point2D startPoint;
        if (firstNode instanceof PointNode) {
            // setting the starting point to the right of the center of the circle
            startPoint = new Point2D(firstNodeBounds.getCenterX() + (Global.CIRCLE_RADIUS / 2),
                    firstNodeBounds.getCenterY());
        } else {
            // setting the start point of line at middle of the right edge of the first node
            startPoint = new Point2D(firstNodeBounds.getCenterX() + (Global.RUNNING_UNIT_NODE_WITH / 2),
                    firstNodeBounds.getCenterY());
        }
        if (isNewLine) {
            // draw the connection to the new line
            Point2D endPoint = new Point2D(secondNodeBounds.getCenterX() - (Global.RUNNING_UNIT_NODE_WITH / 2),
                    secondNodeBounds.getCenterY());
            LineBreakConnectionNode connectionNode = new LineBreakConnectionNode(firstNode, secondNode, startPoint, endPoint);
            // add all nodes to editor pane
            editorAnchorPane.getChildren().add(connectionNode);
            // register all nodes with the editor controller
            editorViewController.registerNode(connectionNode);
            // reset the flag
            isNewLine = false;
        } else {
            if (secondNode instanceof PointNode) {
                // setting the end point of line at middle of the left edge of the second node
                Point2D endPoint = new Point2D(secondNodeBounds.getCenterX() - (Global.CIRCLE_RADIUS / 2),
                        firstNodeBounds.getCenterY());
                SimpleLineConnectionNode simpleLineConnectionNode = new SimpleLineConnectionNode(firstNode, secondNode, startPoint, endPoint);
                // add the node to editor pane
                editorAnchorPane.getChildren().add(simpleLineConnectionNode);
                // register the node with the editor controller
                editorViewController.registerNode(simpleLineConnectionNode);
            } else {
                // setting the end point of line at middle of the left edge of the second node
                Point2D endPoint = new Point2D(secondNodeBounds.getCenterX() - (Global.RUNNING_UNIT_NODE_WITH / 2),
                        firstNodeBounds.getCenterY());
                SimpleLineConnectionNode simpleLineConnectionNode = new SimpleLineConnectionNode(firstNode, secondNode, startPoint, endPoint);
                // add the node to editor pane
                editorAnchorPane.getChildren().add(simpleLineConnectionNode);
                // register the node with the editor controller
                editorViewController.registerNode(simpleLineConnectionNode);
            }
        }
        // save connection info in nodes
        secondNode.setPredecessorNode(firstNode);
        firstNode.setSuccessorNode(secondNode);
    }

    private void showTemplateView() {
        // show dialog
        if (templateView == null) {
            templateView = new TemplateView(this, applicationResources);
        }
        try {
            templateView.showView();
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
        // export to json
    }
}
