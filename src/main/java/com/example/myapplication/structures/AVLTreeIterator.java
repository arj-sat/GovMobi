package com.example.myapplication.structures;

import com.example.myapplication.model.Vehicle;

import java.util.Stack;

/**
 * AVLTreeIterator provides an in-order traversal iterator for {@link AVLTree}.
 * Allows sequential access to all vehicles sorted by VIN.
 *
 * Usage:
 * AVLTreeIterator it = new AVLTreeIterator(tree);
 * while (it.hasNext()) { Vehicle v = it.next(); ... }
 *
 * This is read-only and does not modify the tree.
 *
 * @author
 * u7763790 - Vrushabh Vijay Dhoke
 * u8030355 - Shane George Shibu
 */
public class AVLTreeIterator {
    private final Stack<AVLTree.Node> stack = new Stack<>();

    /**
     * Initializes the iterator for the given AVL tree.
     *
     * @param tree The AVLTree instance to traverse.
     */
    public AVLTreeIterator(AVLTree tree) {
        pushLeft(tree.getRoot());
    }

    private void pushLeft(AVLTree.Node node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    /**
     * Checks if there are more vehicles to iterate over.
     *
     * @return True if more elements are available.
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /**
     * Returns the next vehicle in in-order sequence.
     *
     * @return The next {@link Vehicle} object.
     */
    public Vehicle next() {
        AVLTree.Node current = stack.pop();
        pushLeft(current.right);
        return current.vehicle;
    }
}
