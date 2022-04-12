package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.model.EditorNode;
import de.hirola.runningplanbuilder.model.PointNode;
import de.hirola.runningplanbuilder.model.RunningUnitNode;
import de.hirola.runningplanbuilder.util.ApplicationResources;
import de.hirola.runningplanbuilder.view.UnitNodeView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the editor view.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class EditorViewController {

    private final ApplicationResources applicationResources  // bundle for localisation, ...
            = ApplicationResources.getInstance();
    private final MainViewController mainViewController;
    private UnitNodeView unitNodeView;
    private AnchorPane editorAnchorPane; // editor pane for all elements
    private final List<EditorNode> registeredNodes = new ArrayList<>(); // all nodes
    private ContextMenu nodeContextMenu;
    private MenuItem nodeContextMenuItemEdit;
    private MenuItem nodeContextMenuItemDelete;
    private double onMousePressedPosX, onMousePressedPosY; // position of an element when drag begins
    private double translatedPosX, translatedPosY; // ???
    private EditorNode connectionStartNode;
    private boolean connectionShouldBeCreated;

    // different event handlers
    // mouse click
    final EventHandler<MouseEvent> onMouseClickedEventHandler =
            event -> {

                // open the edit view on double click
                if (event.getClickCount() == 2) {
                    if (unitNodeView == null) {
                        unitNodeView = new UnitNodeView();
                    }
                    try {
                        unitNodeView.showView();
                    } catch (IOException exception) {
                        //TODO: Alert
                        exception.printStackTrace();
                    }
                    return;
                }
                if (connectionShouldBeCreated) {
                    // a new connection should be created or finished
                    if (connectionStartNode == null) {
                        // only if the node is not connected
                        if (!((EditorNode) event.getSource()).isRelated()) {
                            connectionStartNode = (EditorNode) event.getSource();
                            ((Shape) connectionStartNode).setCursor(Cursor.CROSSHAIR);
                            editorAnchorPane.setCursor(Cursor.CROSSHAIR);
                            return;
                        }
                    }
                    if (connectionStartNode != null) {
                        if (!event.getSource().equals(connectionStartNode)) {
                            // only if the node is not connected
                            if (!((EditorNode) event.getSource()).isRelated()) {
                                EditorNode connectionEndNode = (EditorNode) event.getSource();
                                // create a line object
                                Bounds start = ((Shape) connectionEndNode).getBoundsInParent();
                                Bounds end = ((Shape) connectionEndNode).getBoundsInParent();
                                Line connectionLine = new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());
                                editorAnchorPane.getChildren().add(connectionLine);
                                // create a new connection object

                                // add
                                Optional<EditorNode> r = registeredNodes.stream().filter(p -> p.equals(connectionStartNode)).findFirst();
                                r.ifPresent(runningUnitNode -> runningUnitNode.setSuccessorNode(connectionEndNode));
                                //
                                Optional<EditorNode> r2 = registeredNodes.stream().filter(p -> p.equals(connectionEndNode)).findFirst();
                                r2.ifPresent(runningUnitNode -> runningUnitNode.setPredecessorNode(connectionStartNode));
                                editorAnchorPane.setCursor(Cursor.DEFAULT);
                                connectionShouldBeCreated = false;
                                connectionStartNode = null;
                            }
                        } else {
                            // user clicked on the same node
                            // cancel creating connection
                            connectionStartNode = null;
                            connectionShouldBeCreated = false;
                        }
                    }
                }
            };

    // action events
    final EventHandler<ActionEvent> onActionEventHandler =
            event -> {
                if (event.getSource() instanceof MenuItem) {
                    // get the source of the context menu
                    ContextMenu contextMenu = ((MenuItem) event.getSource()).getParentPopup();
                    Node sourceOfContextMenu = contextMenu.getOwnerNode();
                    // context menu action from a running unit element
                    if (event.getSource().equals(nodeContextMenuItemEdit)
                            && sourceOfContextMenu instanceof RunningUnitNode) {
                        if (unitNodeView == null) {
                            unitNodeView = new UnitNodeView();
                        }
                        try {
                            unitNodeView.showView();
                        } catch (IOException exception) {
                            //TODO: Alert
                            exception.printStackTrace();
                        }

                    }
                    // context menu action from a running unit element
                    if (event.getSource().equals(nodeContextMenuItemDelete)) {
                        deleteNode(sourceOfContextMenu);
                    }
                }
            };

    // right click for context menus
    final EventHandler<ContextMenuEvent> onContextMenuRequestedEventHandler =
            event -> {
                // reset menu items
                nodeContextMenuItemDelete.setDisable(false);
                nodeContextMenuItemEdit.setDisable(false);

                if (event.getSource() instanceof RunningUnitNode) {
                    // connected nodes can not be delete
                    RunningUnitNode runningUnitNode = (RunningUnitNode) event.getSource();
                    if (runningUnitNode.isRelated()) {
                        nodeContextMenuItemDelete.setDisable(true);
                    }
                    nodeContextMenu.show(runningUnitNode, Side.RIGHT, 5, 5);
                }
                if (event.getSource() instanceof PointNode) {
                    // stop node can only be deleted
                    PointNode pointNode = (PointNode) event.getSource();
                    if (!pointNode.isStartNode() || editorAnchorPane.getChildren().size() == 1) {
                        nodeContextMenuItemEdit.setDisable(true);
                        nodeContextMenu.show(pointNode, Side.RIGHT, 5, 5);
                    }
                }
            };

    // drag and drop
    // drag starts
    final EventHandler<MouseEvent> onMousePressedEventHandler =
            event ->  {
        if (Global.NODES_MOVABLE) {
            if (event.getSource() instanceof EditorNode) {
                onMousePressedPosX = event.getSceneX();
                onMousePressedPosY = event.getSceneY();
                translatedPosX = ((Shape) (event.getSource())).getTranslateX();
                translatedPosY = ((Shape) (event.getSource())).getTranslateY();

                // a node should be moved
                ((Shape) event.getSource()).setCursor(Cursor.HAND);
            }
        }
        };

    // moving the node
    final EventHandler<MouseEvent> onMouseDraggedEventHandler =
            event -> {
                if (Global.NODES_MOVABLE) {
                    if (event.getSource() instanceof EditorNode) {
                        // get the "correct" position in anchor pane
                        double offsetX = event.getSceneX() - onMousePressedPosX;
                        double offsetY = event.getSceneY() - onMousePressedPosY;
                        double newTranslateX = translatedPosX + offsetX;
                        double newTranslateY = translatedPosY + offsetY;

                        // move only if you will not be created a connection
                        if (!connectionShouldBeCreated) {
                            ((Shape) (event.getSource())).setTranslateX(newTranslateX);
                            ((Shape) (event.getSource())).setTranslateY(newTranslateY);
                        }
                    }
                }
            };

    // mouse released
    final EventHandler<MouseEvent> onMouseReleasedEventHandler =
            event -> {
                if (event.getSource() instanceof  EditorNode) {
                    // node moving finished
                    ((Shape) event.getSource()).setCursor(Cursor.DEFAULT);
                }

            };

    public EditorViewController(@NotNull MainViewController mainViewController,
                                @NotNull AnchorPane editorAnchorPane) {
        this.mainViewController = mainViewController;
        this.editorAnchorPane = editorAnchorPane;
        connectionShouldBeCreated = false;
        createContextMenuForNodes();
    }

    public void registerNode(EditorNode node) {
        if (!registeredNodes.contains(node)) {
            // add event handler for drag and drop
            ((Shape) node).setOnMousePressed(onMousePressedEventHandler);
            ((Shape) node).setOnMouseDragged(onMouseDraggedEventHandler);
            ((Shape) node).setOnMouseReleased(onMouseReleasedEventHandler);
            ((Shape) node).setOnMouseClicked(onMouseClickedEventHandler);
            // add event handler for action
            ((Shape) node).setOnContextMenuRequested(onContextMenuRequestedEventHandler);
            // register with editor
            registeredNodes.add(node);
            if (node instanceof RunningUnitNode) {
                if (!registeredNodes.contains(node)) {
                    // register for template
                    registeredNodes.add(node);
                }
            }
        }
    }

    public void unregisterNode(EditorNode node) {
        registeredNodes.remove(node);
    }

    private void createContextMenuForNodes() {
        // creating a context menu
        nodeContextMenu = new ContextMenu();
        // creating the menu Items for the context menu
        nodeContextMenuItemEdit = new MenuItem(applicationResources.getString("action.edit"));
        nodeContextMenuItemEdit.setOnAction(onActionEventHandler);
        nodeContextMenuItemDelete = new MenuItem(applicationResources.getString("action.delete"));
        nodeContextMenuItemDelete.setOnAction(onActionEventHandler);
        nodeContextMenu.getItems().addAll(nodeContextMenuItemEdit, nodeContextMenuItemDelete);
    }

    @Nullable
    private EditorNode getConnectionNode(EditorNode predecessorNode, EditorNode successorNode) {
        for (EditorNode editorNode : registeredNodes) {
            if (editorNode.getPredecessorNode().equals(predecessorNode)
                    && editorNode.getSuccessorNode().equals(successorNode)) {
                return editorNode;
            }
        }
        return null;
    }

    private void deleteNode(Node node) {
        // remove the element from editor pane and the list
        // TODO: ask the user inform main controller for position changed
        if (node instanceof PointNode) {
            PointNode pointNode = (PointNode) node;
            if (!pointNode.isStartNode() || editorAnchorPane.getChildren().size() == 1) {
                // remove from editor pane
                editorAnchorPane.getChildren().remove(node);
                // unregister the node
                unregisterNode((EditorNode) node);
            }
        }
        if (node instanceof RunningUnitNode) {
            // node cannot be deleted
            if (((EditorNode) node).isRelated()) {
                return;
            }
        }
        // get the connection node for this relation
        EditorNode editorNode = (EditorNode) node;
        EditorNode connectionNode = getConnectionNode(editorNode.getPredecessorNode(), editorNode);
        if (connectionNode != null) {
            // remove from editor pane
            editorAnchorPane.getChildren().remove((Node) connectionNode);
            editorAnchorPane.getChildren().remove(node);
            // unregister both nodes
            unregisterNode(connectionNode);
            unregisterNode(editorNode);
            // remove the successor node from predecessor node
            editorNode.getPredecessorNode().setSuccessorNode(null);
            // inform the main controller
        }
        mainViewController.nodeWasDeleted(editorNode);
    }
}
