package de.hirola.runningplanbuilder.model;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

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
public class ConnectionNode extends Line implements EditorNode {

    private EditorNode startNode;
    private EditorNode endNode;

    public ConnectionNode(EditorNode startNode, EditorNode endNode, Line connectionLine) {
        this.startNode = startNode;
        this.endNode = endNode;
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

}
