package de.hirola.runningplanbuilder.controller;

import de.hirola.runningplanbuilder.model.RunningUnitRectangle;
import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Controller for the editor view.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class EditorSceneController {

    private final List<Shape> registeredShapes; // all shapes
    private final List<RunningUnitRectangle> runningUnitRectangles; // all running units
    private double actualPosX, actualPosY; // position when drag begins
    private double newPosX, newPosY; // position when drag ends

    public EditorSceneController() {
        registeredShapes = new ArrayList<>();
        runningUnitRectangles = new ArrayList<>();
    }

    public void registerShape(Shape shape) {
        if (!registeredShapes.contains(shape)) {
            // add listener fro drag and drop
            shape.setOnMousePressed(onMousePressedEventHandler);
            shape.setOnMouseDragged(onMouseDraggedEventHandler);
            shape.setOnContextMenuRequested(onContextMenuRequestedEventHandler);
            // register for event handler
            registeredShapes.add(shape);
            if (shape instanceof RunningUnitRectangle) {
                if (!runningUnitRectangles.contains(shape)) {
                    runningUnitRectangles.add((RunningUnitRectangle) shape);
                }
            }
        }
    }

    public void unregisterShape(Shape shape) {
        registeredShapes.remove(shape);
    }

    // different event handlers

    // right click for actions
    EventHandler<ContextMenuEvent> onContextMenuRequestedEventHandler =
            new EventHandler<>() {

                @Override
                public void handle(ContextMenuEvent event) {
                    if (event.getSource() instanceof RunningUnitRectangle) {
                        System.out.println("Context");
                    }
                }
            };

    // drag and drop
    EventHandler<MouseEvent> onMousePressedEventHandler =
            new EventHandler<>() {

                @Override
                public void handle(MouseEvent event) {
                    actualPosX = event.getSceneX();
                    actualPosY = event.getSceneY();
                    newPosX = ((Shape) (event.getSource())).getTranslateX();
                    newPosY = ((Shape) (event.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> onMouseDraggedEventHandler =
            new EventHandler<>() {

                @Override
                public void handle(MouseEvent event) {
                    double offsetX = event.getSceneX() - actualPosX;
                    double offsetY = event.getSceneY() - actualPosY;
                    double newTranslateX = newPosX + offsetX;
                    double newTranslateY = newPosY + offsetY;

                    ((Shape) (event.getSource())).setTranslateX(newTranslateX);
                    ((Shape) (event.getSource())).setTranslateY(newTranslateY);
                }
            };
}
