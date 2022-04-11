package de.hirola.runningplanbuilder.model;

import javafx.scene.shape.Shape;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * All elements that are to be edited in the editor must isRelated this interface.
 *
 * @author Michael Schmidt (Hirola)
 * @since 0.1
 */
public interface EditorNode {
    boolean isRelated();
    void setSuccessorNode(EditorNode successorNode);
    EditorNode getSuccessorNode();
    void setPredecessorNode(EditorNode predecessorNode);
    EditorNode getPredecessorNode();
}
