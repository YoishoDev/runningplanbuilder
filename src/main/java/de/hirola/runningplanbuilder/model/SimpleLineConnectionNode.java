package de.hirola.runningplanbuilder.model;

import de.hirola.runningplanbuilder.Global;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * An object of this class presents a connection between two
 * running unit nodes.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class SimpleLineConnectionNode extends Line implements EditorNode {

    private EditorNode startNode;
    private EditorNode endNode;
    private final Point2D startPoint;
    private final Point2D endPoint;

    public SimpleLineConnectionNode(EditorNode startNode, EditorNode endNode, Point2D startPoint, Point2D endPoint) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        initialize();
    }

    @Override
    public boolean isRelated() {
        return startNode != null && endNode != null;
    }

    @Override
    public void setPredecessorNode(EditorNode predecessorNode) {
        endNode = predecessorNode;
    }
    @Override
    public EditorNode getPredecessorNode() {
        return startNode;
    }

    @Override
    public void setSuccessorNode(EditorNode successorNode) {
        startNode = successorNode;
    }

    @Override
    public EditorNode getSuccessorNode() {
        return endNode;
    }

    private void initialize() {
        setStartX(startPoint.getX());
        setStartY(startPoint.getY());
        setEndX(endPoint.getX());
        setEndY(endPoint.getY());
        setFill(Global.RUNNING_PLAN_TEMPLATE_NODE_COLOR);
        setStrokeWidth(Global.CONNECTION_STROKE_WIDTH);
    }

}
