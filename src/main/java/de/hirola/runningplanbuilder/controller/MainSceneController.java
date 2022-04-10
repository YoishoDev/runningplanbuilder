package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.model.ConnectionNode;
import de.hirola.runningplanbuilder.model.EditorNode;
import de.hirola.runningplanbuilder.model.PointNode;
import de.hirola.runningplanbuilder.model.RunningUnitNode;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the main view (application window) using fxml.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class MainSceneController {

    private final ApplicationResources applicationResources
            = ApplicationResources.getInstance(); // bundle for localization, ...
    private EditorAnchorPaneController editorAnchorPaneController; // handler for editor pane
    private PointNode startNode = null; // there can only be one in editor
    private PointNode stopNode = null; // there can only be one in editor
    private EditorNode lastAddedNode = null;
    private Point2D lastNodePoint = null; // position of last added running unit node
    private int addedNodes = 0; // count oo added nodes
    private int nodeLineCount = 1; // increase while line break

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
    private MenuItem menuItemPreferences;
    @FXML
    // the reference will be injected by the FXML loader
    private Menu menuHelp; // help menu
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemDebug;
    @FXML
    // the reference will be injected by the FXML loader
    private MenuItem menuItemAbout;

    /*
        Tool "menu"
     */
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

    /*
        Split pane elements
     */
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private AnchorPane toolMenuAnchorPane;
    @FXML
    private AnchorPane editorAnchorPane;

    public MainSceneController() {}

    @FXML
    // when the FXML loader is done loading the FXML document, it calls this method of the controller
    private void initialize() {
        // initialize the controller for editor pane
        editorAnchorPaneController = new EditorAnchorPaneController(editorAnchorPane);
        // set nodes to javax default colors
        stopNodeMenuElement.setFill(Global.STOP_CIRCLE_COLOR);
        runningUnitNodeMenuElement.setFill(Global.RUNNING_UNIT_NODE_COLOR);
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
        menuItemPreferences.setText(applicationResources.getString("mainMenuBar.menuEdit.menuItemPreferences"));
        menuHelp.setText(applicationResources.getString("mainMenuBar.menuHelp"));
        menuItemDebug.setText(applicationResources.getString("mainMenuBar.menuHelp.menuItemDebug"));
        menuItemAbout.setText(applicationResources.getString("mainMenuBar.menuHelp.menuItemAbout"));
    }

    private void setToolMenuLabel() {
        runningUnitNodeLabel.setText(applicationResources.getString("mainToolMenu.runningUnit"));
        stopNodeMenuElementLabel.setText(applicationResources.getString("mainToolMenu.stop"));
        toolMenuInfoLabel.setText(applicationResources.getString("mainToolMenu.info"));
    }

    @FXML
    // use for onAction by the FXML loader
    private void onAction(ActionEvent event) {
        if (event.getSource().equals(menuItemAbout)) {
            //TODO: show a info view
            System.out.println("About...");
        }
    }

    @FXML
    // use for onMouseClicked by the FXML loader
    private void onMouseClicked(MouseEvent event) {

        if (event.getSource().equals(stopNodeMenuElement) && stopNode == null) {
            // create a stop node
            stopNode = new PointNode(false);
            // the elements cannot really be dragged and dropped
            stopNode.setCenterX(60);
            stopNode.setCenterY(30);
            // register for editor handler
            editorAnchorPaneController.registerNode(stopNode);
            // add a stop node to editor pane
            editorAnchorPane.getChildren().add(stopNode);
        }

        // create a customized rectangle object in editor pane
        if (event.getSource().equals(runningUnitNodeMenuElement)) {
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
                // register both nodes with the editor controller
                editorAnchorPaneController.registerNode(startNode);
                editorAnchorPaneController.registerNode(runningUnitNode);
                // create the connection between bth node
                createNodeConnection(startNode, runningUnitNode);
                // add both nodes to editor pane
                editorAnchorPane.getChildren().add(startNode);
                editorAnchorPane.getChildren().add(runningUnitNode);
            }
        }

    }

    private Point2D getNodePosition() {
        // get the "correct" position in the editor pane
        if (lastNodePoint == null) {
            // get the first position from Global depending on size of start node
            double posX = Global.EDITOR_PANE_NODE_START_POINT.getX();
            double posY = Global.EDITOR_PANE_NODE_START_POINT.getY()
                    + Global.RUNNING_UNIT_NODE_HEIGHT / 2;
            lastNodePoint = new Point2D(posX,posY);
        } else {
            // calculate the position of next node
            if (addedNodes == 1) {
                // between start node and first running unit node add only the space from Global
                lastNodePoint = new Point2D(lastNodePoint.getX() + Global.SPACE_BETWEEN_NODES,
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
                    nodeLineCount++;
                }
                lastNodePoint = new Point2D(posX, posY);
            }
        }
        addedNodes++;
        return lastNodePoint;
    }

    private void createNodeConnection(EditorNode firstNode, EditorNode secondNode) {

        Line connectionLine = new Line();
        ConnectionNode connectionNode = new ConnectionNode(firstNode, startNode, connectionLine);
        editorAnchorPaneController.registerNode(connectionNode);
    }
}
