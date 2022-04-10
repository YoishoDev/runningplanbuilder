package de.hirola.runningplanbuilder.model;

import de.hirola.runningplanbuilder.Global;
import javafx.scene.shape.Circle;
import org.jetbrains.annotations.Nullable;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * An object of this class presents a start or stop node in the editor pane.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class PointNode extends Circle implements EditorNode {

    private final boolean isStartNode;
    private EditorNode neighborNode;

    public PointNode(boolean isStartNode) {
        this.isStartNode = isStartNode;
        initialize();
    }

    @Override
    public boolean isRelated() {
        return false;
    }

    public void setNeighborNode(EditorNode neighborNode) {
        this.neighborNode = neighborNode;
    }

    @Override
    public void setPredecessorNode(EditorNode predecessorNode) {
        neighborNode = predecessorNode;
    }

    @Nullable
    @Override
    public EditorNode getPredecessorNode() {
        return neighborNode;
    }

    @Override
    public void setSuccessorNode(EditorNode successorNode) {
        neighborNode = successorNode;
    }

    @Nullable
    @Override
    public EditorNode getSuccessorNode() {
        return neighborNode;
    }

    private void initialize() {
        setRadius(Global.CIRCLE_RADIUS);
        if (isStartNode) {
            setFill(Global.START_CIRCLE_COLOR);
        } else {
            setFill(Global.STOP_CIRCLE_COLOR);
        }
    }
}
