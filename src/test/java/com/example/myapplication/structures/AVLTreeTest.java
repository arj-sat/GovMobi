package com.example.myapplication.structures;

import com.example.myapplication.model.Vehicle;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for AVLTree implementation.
 * This class contains unit tests for verifying the functionality of the AVLTree class,
 * including insertion, deletion, search operations, and tree traversal.
 * The tests use Vehicle objects as data nodes to validate the tree's behavior with real data.
 *
 * @author u7884012 - Ruoheng Feng
 */
public class AVLTreeTest {

    private AVLTree tree;
    private Vehicle v1, v2, v3, v4;

    /**
     * Sets up the test environment before each test.
     * Initializes an empty AVLTree and creates four test vehicles with different properties.
     * Inserts all vehicles into the tree to create a test dataset.
     */
    @Before
    public void setUp() {
        tree = new AVLTree();

        v1 = new Vehicle("Car A", "ABC123", "VIN001", "Active", "2025-12-31",
                "2024-01-01", "OK", "No", "Canberra", "Public");

        v2 = new Vehicle("Car B", "XYZ789", "VIN002", "Expired", "2023-06-30",
                "2023-01-01", "Fail", "No", "Sydney", "Private");

        v3 = new Vehicle("Car C", "DEF456", "VIN003", "Suspended", "2024-09-15",
                "2023-11-01", "OK", "Yes", "Melbourne", "Public");

        v4 = new Vehicle("Car D", "GHI321", "VIN004", "Active", "2026-03-01",
                "2024-03-03", "OK", "No", "Brisbane", "Private");

        tree.insert(v1);
        tree.insert(v2);
        tree.insert(v3);
        tree.insert(v4);
    }

    /**
     * Tests basic insertion and search functionality.
     * Verifies that a vehicle can be inserted and retrieved using its VIN.
     */
    @Test
    public void testBasicInsertAndSearch() {
        Vehicle result = tree.search("VIN002");
        assertNotNull(result);
        assertEquals("Car B", result.getVehicleName());
    }

    /**
     * Tests search functionality using license plate number.
     * Verifies that a vehicle can be found using its license plate.
     */
    @Test
    public void testSearchByLicensePlate() {
        Vehicle result = tree.searchByLicensePlate("ABC123");
        assertNotNull(result);
        assertEquals("Car A", result.getVehicleName());
    }

    /**
     * Tests deletion of a leaf node.
     * Verifies that a vehicle can be removed from the tree when it has no children.
     */
    @Test
    public void testDeleteLeafNode() {
        tree.delete(v4);
        assertNull(tree.search("VIN004"));
    }

    /**
     * Tests deletion of a node with two children.
     * Verifies that a vehicle can be removed from the tree when it has two children,
     * and the tree maintains its structure.
     */
    @Test
    public void testDeleteNodeWithTwoChildren() {
        tree.delete(v1);
        assertNull(tree.search("VIN001"));
    }

    /**
     * Tests deletion followed by reinsertion.
     * Verifies that a vehicle can be removed and then reinserted into the tree.
     */
    @Test
    public void testDeleteAndReinsert() {
        tree.delete(v2);
        assertNull(tree.search("VIN002"));
        tree.insert(v2);
        assertNotNull(tree.search("VIN002"));
    }

    /**
     * Tests handling of duplicate insertions.
     * Verifies that inserting a duplicate vehicle does not break the tree structure.
     */
    @Test
    public void testDuplicateInsertDoesNotBreakTree() {
        tree.insert(v1); // insert VIN001 again
        Vehicle result = tree.search("VIN001");
        assertEquals("Car A", result.getVehicleName());
    }

    /**
     * Tests search for non-existent VIN.
     * Verifies that searching for a non-existent VIN returns null.
     */
    @Test
    public void testSearchNonExistentVinReturnsNull() {
        assertNull(tree.search("VIN999"));
    }

    /**
     * Tests case sensitivity in search operations.
     * Verifies that VIN searches are case-sensitive.
     */
    @Test
    public void testSearchCaseSensitivity() {
        assertNull(tree.search("vin001")); // lower case, should be null
        assertNotNull(tree.search("VIN001")); // correct case
    }

    /**
     * Tests in-order traversal of the tree.
     * Verifies that the iterator returns VINs in sorted order.
     */
    @Test
    public void testInOrderTraversalReturnsSortedVin() {
        AVLTreeIterator iterator = new AVLTreeIterator(tree);
        List<String> vins = new ArrayList<>();
        while (iterator.hasNext()) {
            vins.add(iterator.next().getVin());
        }

        List<String> sorted = new ArrayList<>(vins);
        Collections.sort(sorted);
        assertEquals(sorted, vins);
    }

    /**
     * Tests tree root initialization.
     * Verifies that the tree has a valid root node after insertions.
     */
    @Test
    public void testRootNotNullAfterInserts() {
        assertNotNull(tree.getRoot());
    }
}
