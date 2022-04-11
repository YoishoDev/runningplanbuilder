package de.hirola.runningplanbuilder.model;

import de.hirola.runningplanbuilder.Global;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * An object of this class presents a connection between two
 * running unit nodes over two lines.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class LineBreakConnectionNode extends Polyline implements EditorNode {

    private EditorNode startNode;
    private EditorNode endNode;
    private final Point2D startPoint;
    private final Point2D endPoint;

    public LineBreakConnectionNode(EditorNode startNode, EditorNode endNode, Point2D startPoint, Point2D endPoint) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        initialize();
        addPoints();
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
        setStrokeWidth(Global.CONNECTION_STROKE_WIDTH);
    }

    private void addPoints() {
        // list of XY coordinates separated by commas
        // start point
        getPoints().addAll(startPoint.getX(), startPoint.getY());
        // first line break point
        getPoints().addAll(startPoint.getX() + (Global.SPACE_BETWEEN_NODES / 2), startPoint.getY());
        // second line break point
        getPoints().addAll(startPoint.getX() + (Global.SPACE_BETWEEN_NODES / 2),
                endPoint.getY() - (Global.SPACE_BETWEEN_NODES * Global.CONNECTION_LINE_BREAK_FACTOR));
        // first new line point
        getPoints().addAll(endPoint.getX() - (Global.SPACE_BETWEEN_NODES / 2),
                endPoint.getY() - (Global.SPACE_BETWEEN_NODES * Global.CONNECTION_LINE_BREAK_FACTOR));
        // second new line point
        getPoints().addAll(endPoint.getX() - (Global.SPACE_BETWEEN_NODES / 2), endPoint.getY());
        // end point
        getPoints().addAll(endPoint.getX(), endPoint.getY());
    }
}
