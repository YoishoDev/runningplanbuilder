package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.model.EditorNode;
import de.hirola.runningplanbuilder.model.RunningUnitNode;
import de.hirola.runningplanbuilder.util.ApplicationResources;
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
import java.awt.geom.Area;
import org.jetbrains.annotations.NotNull;

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
public class EditorAnchorPaneController {

    private final ApplicationResources applicationResources  // bundle for localisation, ...
            = ApplicationResources.getInstance();
    private AnchorPane editorAnchorPane; // editor pane for all elements
    private final List<EditorNode> registeredNodes = new ArrayList<>(); // all nodes
    private MenuItem runningUnitElementContextMenuItemEdit;
    private MenuItem runningUnitElementContextMenuItemDelete;
    private double onMousePressedPosX, onMousePressedPosY; // position of an element when drag begins
    private double translatedPosX, translatedPosY; // ???
    private EditorNode connectionStartNode;
    private boolean connectionShouldBeCreated;

    // different event handlers
    // mouse click
    EventHandler<MouseEvent> onMouseClickedEventHandler =
            event -> {

                // open the edit view on double click
                if (event.getClickCount() == 2) {
                    System.out.println("Doppelklick");
                    return;
                }
                if (connectionShouldBeCreated) {
                    // a new connection should be created or finished
                    if (connectionStartNode == null) {
                        // only if the node is not connected
                        if (!((EditorNode) event.getSource()).isRelated()) {
                            connectionStartNode = (EditorNode) event.getSource();
                            ((Shape) connectionStartNode).setCursor(Cursor.CROSSHAIR);
                            if (editorAnchorPane != null) {
                                editorAnchorPane.setCursor(Cursor.CROSSHAIR);
                            }
                            return;
                        }
                    }
                    if (connectionStartNode != null) {
                        if (!event.getSource().equals(connectionStartNode)) {
                            // only if the node is not connected
                            if (!((EditorNode) event.getSource()).isRelated()) {
                                EditorNode connectionEndNode = (EditorNode) event.getSource();
                                // create a line object
                                if (editorAnchorPane != null) {
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
    EventHandler<ActionEvent> onActionEventHandler =
            event -> {
                if (event.getSource() instanceof MenuItem) {
                    // get the source of the context menu
                    ContextMenu contextMenu = ((MenuItem) event.getSource()).getParentPopup();
                    Node sourceOfContextMenu = contextMenu.getOwnerNode();
                    // context menu action from a running unit element
                    if (event.getSource().equals(runningUnitElementContextMenuItemEdit)
                            && sourceOfContextMenu instanceof RunningUnitNode) {
                        System.out.println("edit");

                    }
                    // context menu action from a running unit element
                    if (event.getSource().equals(runningUnitElementContextMenuItemDelete)
                            && sourceOfContextMenu instanceof RunningUnitNode) {
                        // remove the element from editor pane and the list
                        // TODO: ask the user inform main controller for position changed
                        if (editorAnchorPane != null) {
                            editorAnchorPane.getChildren().remove(sourceOfContextMenu);
                            unregisterNode((RunningUnitNode) sourceOfContextMenu);
                        }
                    }
                }
            };

    // right click for context menus
    EventHandler<ContextMenuEvent> onContextMenuRequestedEventHandler =
            event -> {
                if (event.getSource() instanceof RunningUnitNode) {
                    RunningUnitNode runningUnitNode = (RunningUnitNode) event.getSource();
                    // connected nodes can not be delete
                    if (runningUnitNode.isRelated()) {
                        runningUnitElementContextMenuItemDelete.setDisable(true);
                    }
                    getContextMenuForRunningUnitElement()
                            .show(runningUnitNode, Side.RIGHT, 5, 5);
                }
            };

    // drag and drop
    // drag starts
    EventHandler<MouseEvent> onMousePressedEventHandler =
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
    EventHandler<MouseEvent> onMouseDraggedEventHandler =
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
    EventHandler<MouseEvent> onMouseReleasedEventHandler =
            event -> {
                if (event.getSource() instanceof  EditorNode) {
                    // node moving finished
                    ((Shape) event.getSource()).setCursor(Cursor.DEFAULT);
                }

            };

    public EditorAnchorPaneController(@NotNull AnchorPane editorAnchorPane) {
        this.editorAnchorPane = editorAnchorPane;
        connectionShouldBeCreated = false;
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

    public void connectionShouldBeCreated() {
        connectionShouldBeCreated = true;
    }

    private @NotNull ContextMenu getContextMenuForRunningUnitElement() {
        // creating a context menu
        ContextMenu contextMenu = new ContextMenu();
        // creating the menu Items for the context menu
        runningUnitElementContextMenuItemEdit = new MenuItem(applicationResources.getString("action.edit"));
        runningUnitElementContextMenuItemEdit.setOnAction(onActionEventHandler);
        runningUnitElementContextMenuItemDelete = new MenuItem(applicationResources.getString("action.delete"));
        runningUnitElementContextMenuItemDelete.setOnAction(onActionEventHandler);
        contextMenu.getItems().addAll(runningUnitElementContextMenuItemEdit, runningUnitElementContextMenuItemDelete);
        return contextMenu;
    }

    private static boolean isOverlapping(Shape shapeA, Shape shapeB) {
        Area areaA = new Area((java.awt.Shape) shapeA);
        areaA.intersect(new Area((java.awt.Shape) shapeB));
        return !areaA.isEmpty();
    }

}
