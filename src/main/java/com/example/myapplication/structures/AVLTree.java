package com.example.myapplication.structures;

import com.example.myapplication.model.Vehicle;

/**
 * AVLTree is a self-balancing binary search tree that stores {@link Vehicle} objects.
 * It uses the vehicle's VIN as the unique key and supports efficient insert, search, and delete operations.
 *
 * This implementation is tailored for use in features like vehicle status search or reporting tools.
 * Balancing ensures O(log n) performance for all major operations.
 *
 * NOTE: The tree is not thread-safe.
 *
 * @author
 * u7763790 - Vrushabh Vijay Dhoke
 */
public class AVLTree {

    /**
     * Node represents a single element in the AVL tree.
     * Stores a {@link Vehicle} and pointers to left and right children.
     */
    // Removed 'private' so iterator can access it
    static class Node {
        Vehicle vehicle;
        String key; // VIN as key
        int height;
        Node left, right;

        Node(Vehicle vehicle) {
            this.vehicle = vehicle;
            this.key = vehicle.getVin();
            this.height = 1;
        }
    }

    private Node root;

    /** Returns the root of the AVL tree (used by the iterator). */
    public Node getRoot() {
        return root;
    }

    /** Returns the height of a node or 0 if null. */
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    /** Computes the balance factor of a node. */
    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    /** Right rotation (for LL imbalance). */
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    /** Left rotation (for RR imbalance). */
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    /**
     * Inserts a new vehicle into the AVL tree.
     *
     * @param vehicle The vehicle object to insert.
     */
    public void insert(Vehicle vehicle) {
        root = insert(root, vehicle);
    }

    private Node insert(Node node, Vehicle vehicle) {
        if (node == null) return new Node(vehicle);

        if (vehicle.getVin().compareTo(node.key) < 0) {
            node.left = insert(node.left, vehicle);
        } else if (vehicle.getVin().compareTo(node.key) > 0) {
            node.right = insert(node.right, vehicle);
        } else {
            return node; // Duplicate VIN (no-op)
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Balancing cases
        if (balance > 1 && vehicle.getVin().compareTo(node.left.key) < 0)
            return rightRotate(node);
        if (balance < -1 && vehicle.getVin().compareTo(node.right.key) > 0)
            return leftRotate(node);
        if (balance > 1 && vehicle.getVin().compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && vehicle.getVin().compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    /**
     * Searches the AVL tree for a vehicle by VIN.
     *
     * @param vin The VIN key.
     * @return The Vehicle object, or null if not found.
     */
    public Vehicle search(String vin) {
        Node node = search(root, vin);
        return node != null ? node.vehicle : null;
    }

    private Node search(Node node, String vin) {
        if (node == null || node.key.equals(vin)) return node;
        if (vin.compareTo(node.key) < 0) return search(node.left, vin);
        return search(node.right, vin);
    }

    /**
     * Performs a recursive search for a vehicle by license plate (case-insensitive).
     *
     * @param licensePlate The license plate string.
     * @return The matching Vehicle, or null if not found.
     */
    public Vehicle searchByLicensePlate(String licensePlate) {
        return searchByLicensePlateRecursive(root, licensePlate);
    }

    private Vehicle searchByLicensePlateRecursive(Node node, String licensePlate) {
        if (node == null) return null;
        if (node.vehicle.getLicensePlate().equalsIgnoreCase(licensePlate)) return node.vehicle;
        Vehicle found = searchByLicensePlateRecursive(node.left, licensePlate);
        return (found != null) ? found : searchByLicensePlateRecursive(node.right, licensePlate);
    }

    /**
     * Deletes a vehicle by VIN.
     *
     * @param vehicle The vehicle to remove.
     */
    public void delete(Vehicle vehicle) {
        root = delete(root, vehicle.getVin());
    }

    private Node delete(Node node, String vin) {
        if (node == null) return null;

        if (vin.compareTo(node.key) < 0)
            node.left = delete(node.left, vin);
        else if (vin.compareTo(node.key) > 0)
            node.right = delete(node.right, vin);
        else {
            if (node.left == null || node.right == null)
                node = (node.left != null) ? node.left : node.right;
            else {
                Node successor = minValueNode(node.right);
                node.key = successor.key;
                node.vehicle = successor.vehicle;
                node.right = delete(node.right, successor.key);
            }
        }

        if (node == null) return null;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Balance correction
        if (balance > 1 && getBalance(node.left) >= 0) return rightRotate(node);
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) return leftRotate(node);
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    /** Finds the node with the minimum key (used in deletion). */
    private Node minValueNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }
}
