package de.hirola.runningplanbuilder.model;

import de.hirola.runningplanbuilder.Global;
import de.hirola.sportslibrary.model.RunningPlanEntry;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * An object of this class presents a running unit in a running plan.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public class RunningPlanEntryNode extends Rectangle implements EditorNode {

    private final UUID uuid;
    private RunningPlanEntry runningPlanEntry; // the unit of this node
    private EditorNode predecessorNode;
    private EditorNode successorNode;

    public RunningPlanEntryNode(double posX, double posY) {
        uuid = UUID.randomUUID();
        runningPlanEntry = new RunningPlanEntry();
        predecessorNode = null;
        successorNode = null;
        initialize(posX, posY);
    }

    @Nullable
    @Override
    public EditorNode getPredecessorNode() {
        return predecessorNode;
    }

    @Override
    public void setPredecessorNode(EditorNode predecessorNode) {
        this.predecessorNode = predecessorNode;
    }

    @Nullable
    @Override
    public EditorNode getSuccessorNode() {
        return successorNode;
    }

    @Override
    public void setSuccessorNode(EditorNode successorNode) {
        this.successorNode = successorNode;
    }

    public RunningPlanEntry getRunningPlanEntry() {
        return runningPlanEntry;
    }

    public void setRunningPlanEntry(RunningPlanEntry runningPlanEntry) {
        this.runningPlanEntry = runningPlanEntry;
    }

    private void initialize(double posX, double posY) {
        setX(posX);
        setY(posY);
        setStroke(Global.RUNNING_UNIT_NODE_STROKE_COLOR);
        setWidth(Global.RUNNING_UNIT_NODE_WITH);
        setHeight(Global.RUNNING_UNIT_NODE_HEIGHT);
        setFill(Global.RUNNING_UNIT_NODE_COLOR);
    }

    @Override
    public boolean isRelated() {
        return predecessorNode != null && successorNode != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunningPlanEntryNode that = (RunningPlanEntryNode) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "RunningPlanEntryNode{" +
                "uuid=" + uuid +
                '}';
    }
}
