/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SudokuSolver;

import java.util.TreeSet;

/**
 * This class defines the nodes used in the tree for finding preemptive sets
 * @author morel
 */
public class CellNode implements Comparable<CellNode> {

    Cell cell;  //The cell in the node
    int nodesNeeded;    //Number of nodes needed to complete the preemptive set
    TreeSet<Integer> numbersFound = new TreeSet<>();  //The numbers already found to complete the set
    TreeSet<Integer> numbersLeft = new TreeSet<>();   //Numbers to be found to complete the set
    CellNode parent;
    TreeSet<CellNode> children = new TreeSet<>();
    int depth;  //The number of nodes from the parent to the node, both included

    CellNode(Cell cell) {
        this.cell = cell;
        updateDepth(this);
        updateNumbers(this);
        updateNodesNeeded(this);
    }

    @Override
    public int compareTo(CellNode t) {
        return cell.compareTo(t.cell);
    }

    /**
     * Adds a child to the current node
     * @param node: the child to add
     * @return boolean: true if the child was added, false if not
     */
    public boolean addChild(CellNode node) {
        //Only add children with markups that contain at least one of the 
        //  numbers to be found
        boolean intersect = false;
        for (int i : node.cell.markup) {
            if (this.numbersLeft.contains(i)) {
                intersect = true;
            }
        }
        if (!intersect) {
            return false;
        }

        //Avoid cell duplicates in children
        boolean alreadyIn = false;
        for (CellNode child : children) {
            if (child.cell.equals(node.cell)) {
                return true;
            }
        }
        if (alreadyIn) {
            return false;
        }

        //No need to add children if the preemptive set is complete already
        if (this.depth == this.nodesNeeded) {
            return false;
        }

        //Link parent and children together and update the node attributes 
        //  accordingly
        node.parent = this;
        children.add(node);
        updateDepth(node);
        updateNumbers(node);
        updateNodesNeeded(node);
        return true;
    }

    /**
     * Determines whether or not a node is a leaf (COntains no children)
     * @return boolean
     */
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    /**
     * Updates the nodes needed to complete the preemptive set, according to
     *   the parent attributes
     * @param node: the node to be updated
     */
    private void updateNodesNeeded(CellNode node) {
        //If the node is the root, we need at least, as much nodes as 
        //  the its markup size to complete a preemptive seet
        if (node.parent == null) {
            node.nodesNeeded = node.cell.markup.size();
        } else {
            //If there is no number to be found but the number of nodes needed to 
            //  complete the preemtive set has not been reached, keep the 
            //  number of nodes needed the same and add children later
            if (node.depth < node.parent.nodesNeeded) {
                node.nodesNeeded = node.parent.nodesNeeded;
            } else if(node.numbersLeft.isEmpty()) {
                node.nodesNeeded = node.numbersFound.size();
            } else {
                //If at the current node, there are still numbers to be found, 
                //  then we will need at least one more node that contains those
                //  numbers
                node.nodesNeeded = node.parent.nodesNeeded + 1;
            }
        }
    }

    /**
     * Updates the depth of the given node (number of nodes from root to node)
     * @param node: The node to be updated
     */
    private void updateDepth(CellNode node) {
        // The root has a depth of 1
        if (node.parent == null) {
            node.depth = 1;
        } else {
            node.depth = node.parent.depth + 1;
        }
    }

    /**
     * Update the numbers found and the numbers to be found, according to the
     * added node and the parent attributes
     * @param node: The node to be updated
     */
    private void updateNumbers(CellNode node) {
        //For the root, all the numbers in its markup are yet to be found
        if (node.parent == null) {
            node.numbersFound = new TreeSet<>();
            node.numbersLeft = new TreeSet<>(node.cell.markup);
        } else {
            //For the other nodes:
            //  The numbers found is the previous numbers found, plus the
            //      numbers found in the current markup
            //  The numbers left are the previous numbers left, minus the
            //      numbers found in the current markup
            node.numbersFound = new TreeSet<>(node.parent.numbersFound);
            node.numbersLeft = new TreeSet<>(node.parent.numbersLeft);
            for (int i : node.cell.markup) {
                if (node.numbersLeft.contains(i)) {
                    node.numbersFound.add(i);
                    node.numbersLeft.remove(i);
                } else if (!node.numbersFound.contains(i)) {
                    node.numbersLeft.add(i);
                }
            }
        }
    }
}
