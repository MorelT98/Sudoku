package SudokuSolver;

import java.util.ArrayList;
import java.util.TreeMap;

public class Grid {

    //The grid containing all the sudoku cells
    public Cell[][] grid = new Cell[9][9];
    
    TreeMap<Cell, ArrayList<Coordinates>> cellsMap;

    public Grid(Cell[][] cell) {
        for (int i = 0; i < cell.length; i++) {
            for (int j = 0; j < cell.length; j++) {
                grid[i][j] = cell[i][j];
            }
        }
        initiateMarkups();
    }

    /**
     * Adds numbers from 1 to 9 to the markups
     */
    private void initiateMarkups() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].markup.clear();
                //Only add 1 to 9 to unsolved cells
                if (grid[i][j].value == 0) {
                    for (int k = 1; k <= 9; k++) {
                        grid[i][j].markup.add(k);
                    }
                }
            }
        }
    }

    /**
     * Get the cells in the same row, column and 3x3 grid
     *   as the given cell
     * @param row: the row containing the cell
     * @param column: the column containing the cell
     * @param included: whether or not the cell itself should be included 
     *  in the result
     * @return ArrayList<Cell>: the list of adjacent cells
     */
    public ArrayList<Cell> getAdjCells(int row, int column, boolean included) {
        return getAdjCells(grid[row][column], included);
    }

    /**
     * Get the cells in the same row, column and 3x3 grid
     *   as the current grid
     * @param cell: the cell whose neighborhood is to be found
     * @param included: whether or not the cell should be included in the adjacent cells
     * @return ArrayList<Cell>: the list of adjacent cells
     */
    public ArrayList<Cell> getAdjCells(Cell cell, boolean included) {
        //Each cell contains 20 adjacent cells
        ArrayList<Cell> adjCells = new ArrayList(20);

        //Adding the cells in the same row
        for (int i = 0; i < 9; i++) {
            //If the cell has to be included, add it directly
            //If not, only add cells that are in a different column
            if (included || (!included && i != cell.column)) {
                adjCells.add(grid[cell.row][i]);
            }
        }

        //Adding the cells in the same column
        for (int j = 0; j < 9; j++) {
            //No need to add the given cell again, just add the ones
            //  that are in a different row
            if (j != cell.row) {
                adjCells.add(grid[j][cell.column]);
            }
        }

        //Borders of the 3x3 grid the cell is in
        int rowStart = cell.row - (cell.row % 3);
        int columnStart = cell.column - (cell.column % 3);

        //Adding the cells in the same 3x3 grid, excluding the
        //  ones counted in the row adding and column adding
        for (int i = rowStart; i < rowStart + 3; i++) {
            for (int j = columnStart; j < columnStart + 3; j++) {
                //Since the ones on the same row and one the same column
                //  have been added already, only add the ones in different
                //  rows and columns
                if (i != cell.row && j != cell.column) {
                    adjCells.add(grid[i][j]);
                }
            }
        }

        return adjCells;
    }

    /**
     * Returns the cells in the same 3x3 grid as the given cell
     * @param row: the row containing the cell
     * @param column: the column containing the cell
     * @param included: whether or not the cell itself should be included 
     *  in the result
     * @return ArrayList<Cell>: the list of cells in the same 3x3 grid
     */
    public ArrayList<Cell> get3x3Grid(int row, int column, boolean included) {
        return get3x3Grid(grid[row][column], included);
    }

    /**
     * Returns the cells in the same 3x3 grid as the given cell
     * @param cell: the given cell in the 3x3 grid
     * @param included: whether or not the cell itself should be included 
     *  in the result
     * @return ArrayList<Cell>: the list of cells in the 3x3 grid
     */
    public ArrayList<Cell> get3x3Grid(Cell cell, boolean included) {
        ArrayList<Cell> _3x3Grid = new ArrayList<>();

        //Borders of the 3x3 grid the cell is in
        int rowStart = cell.row - (cell.row % 3);
        int columnStart = cell.column - (cell.column % 3);

        //Adding the cells in the same 3x3 grid, excluding the
        //  ones counted in the row adding and column adding
        for (int i = rowStart; i < rowStart + 3; i++) {
            for (int j = columnStart; j < columnStart + 3; j++) {
                if (included || (!included && (i != cell.row || j != cell.column))) {
                    _3x3Grid.add(grid[i][j]);
                }
            }
        }

        return _3x3Grid;
    }

    /**
     * Returns the cells in the same row as the given cell
     * @param row: the row containing the cell
     * @param column: the column containing the cell
     * @param included: whether or not the cell itself should be included 
     *  in the result
     * @return ArrayList<Cell>: the list of cells in the same row
     */
    public ArrayList<Cell> getRow(int row, int column, boolean included) {
        return getRow(grid[row][column], included);
    }

    /**
     * Returns the cells in the same row as the given cell
     * @param cell: the given cell in the row
     * @param included: whether or not the cell should be included in the row
     * @return ArrayList<Cell>: the list of cells in the same row
     */
    public ArrayList<Cell> getRow(Cell cell, boolean included) {
        ArrayList<Cell> row = new ArrayList<>();

        for (int j = 0; j < 9; j++) {
            if (included || (!included && j != cell.column)) {
                row.add(grid[cell.row][j]);
            }
        }

        return row;
    }

    /**
     * Returns the cells in the same column as the given cell
     * @param row: the row containing the cell
     * @param column: the column containing the cell
     * @param included: whether or not the cell itself should be included 
     *  in the result
     * @return ArrayList<Cell>: the list of cells in the column
     */
    public ArrayList<Cell> getColumn(int row, int column, boolean included) {
        return getColumn(grid[row][column], included);
    }

    /**
     * Returns the cells in the same column as the given cell
     * @param cell: the given cell in the column
     * @param included: whether or not the cell should be included in the column
     * @return ArrayList<Cell>: the list of cells in the column
     */
    public ArrayList<Cell> getColumn(Cell cell, boolean included) {
        ArrayList<Cell> column = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            if (included || (!included && i != cell.row)) {
                column.add(grid[i][cell.column]);
            }
        }

        return column;
    }

    /**
     * Looks through the values of the adjacent cells of each cell and removes
     *   those values from the current cell's markup, thus eliminating the 
     *   possibilities of the values of the cell
     * @return true if any value was removed from a markup, false if nothing changed
     */
    public boolean markup() {
        //Check if overrall,at least one of the grid markups changed
        boolean changed = false;

        //Checks if at the current iteration, one of the grid markups
        //  changed
        boolean inAction = true;

        //Keep scanning the grid and update markups, until no change is made
        while (inAction) {
            inAction = false;
            for (Cell[] row : grid) {
                for (Cell cell : row) {
                    for (Cell adjCell : getAdjCells(cell, false)) {
                        if (cell.markup.remove(adjCell.value)) {
                            inAction = true;
                            changed = true;
                        }
                    }
                    //If there is only one number in the markup, that is the only
                    //  possible value of the cell
                    if (cell.markup.size() == 1) {
                        cell.value = cell.markup.first();
                        cell.markup.clear();
                    }
                }
            }
        }
        return changed;
    }
    
    public void useAllPreemptiveSets() {
        //Rows
        for(int i = 0; i < 9; i++) {
            ArrayList<Cell> currentRow = this.getRow(i, 0, true);
            ArrayList<Cell> preemptiveSet = this.getPreemptiveSet(currentRow);
            while(!cellsMap.isEmpty() && !preemptiveSet.isEmpty()) {
                this.updateNeighborhood(currentRow, preemptiveSet, cellsMap);
                currentRow = this.getRow(i, 0, true);
                this.getPreemptiveSet(currentRow);
            }
        }
    }

    /**
     * Finds a preemptiveSet, given the neighborhood of a cell
     * @param cellsList: the neighborhood of a cell
     * @return a preemptive set of the neighborhood, or null if there isn't any
     */
    public ArrayList<Cell> getPreemptiveSet(ArrayList<Cell> cellsList) {
        int unsolvedCells = 0;
        //Will contain the result
        ArrayList<Cell> preemptiveSet = new ArrayList<>();

        //All the cells in the neighborhood will be copied in a sorted map, to
        //  organize them and deal with markup repetitions
        cellsMap = new TreeMap();

        //Putting all the cells in the map
        for (Cell cell : cellsList) {
            if (cell.value == 0) {
                unsolvedCells++;
                if (!cellsMap.containsKey(cell)) {
                    cellsMap.put(cell, new ArrayList<>());
                }
                cellsMap.get(cell).add(new Coordinates(cell.row, cell.column));
            }
        }

        for (Cell cell : cellsMap.keySet()) {
            //Cells with the same markup
            ArrayList<Coordinates> similarCells = cellsMap.get(cell);

            //If the markup size, and the number of cells having that markup 
            //  size are the same, then they form a trivial preemptive set
            if (cell.markup.size() == similarCells.size()
                    && similarCells.size() < (9 - cellsList.size())) {
                //Add those cells to the preemptive set
                for (int i = 0; i < similarCells.size(); i++) {
                    int row = similarCells.get(i).row;
                    int column = similarCells.get(i).column;
                    preemptiveSet.add(grid[row][column]);
                }
                if (preemptiveSet.size() < unsolvedCells) {
                    return preemptiveSet;
                }
                return null;
                //Remove the numbers in the preemptive set found from the other cells
                //updateNeighborhood(cellsList, preemptiveSet, cellsMap);
            }

            CellNode root = new CellNode(cell);
            buildPreemptiveTree(root, cellsMap);
            ArrayList<CellNode> preemptiveLeaves = new ArrayList<>();
            findPreemptiveLeaves(root, preemptiveLeaves);
            if (!preemptiveLeaves.isEmpty()) {
                preemptiveSet = getPreemptiveSetFromLeaf(preemptiveLeaves.get(0));
            }
            if (preemptiveSet.size() < unsolvedCells) {
                return preemptiveSet;
            }
        }
        //updateNeighborhood(cellsList, preemptiveSet, cellsMap);
        return null;
    }

    private void buildPreemptiveTree(CellNode node,
            TreeMap<Cell, ArrayList<Coordinates>> cellsMap) {
        for (Cell child : cellsMap.keySet()) {
            if (child.compareTo(node.cell) > 0) {
                CellNode childNode = new CellNode(child);
                if (node.addChild(childNode)) {
                    buildPreemptiveTree(childNode, cellsMap);
                }
            }
        }
        ArrayList<Coordinates> currentList = cellsMap.get(node.cell);
        int size = currentList.size();
        int currentIndex = currentList.indexOf(new Coordinates(node.cell.row, node.cell.column));
        if (size > 1 && currentIndex < (size - 1)) {
            CellNode childNode = new CellNode(grid[node.cell.row][node.cell.column]);
            if (node.addChild(childNode)) {
                buildPreemptiveTree(childNode, cellsMap);
            }
        }
    }

    private ArrayList<Cell> getPreemptiveSetFromLeaf(CellNode leaf) {
        ArrayList<Cell> result = new ArrayList<>();
        CellNode current = leaf;
        while (current != null) {
            result.add(current.cell);
            current = current.parent;
        }
        return result;
    }

    private void findPreemptiveLeaves(CellNode node,
            ArrayList<CellNode> preemptiveLeaves) {
        if (node.isLeaf()) {
            if (node.depth == node.nodesNeeded && node.numbersLeft.isEmpty()) {
                preemptiveLeaves.add(node);
            }
        } else {
            for (CellNode child : node.children) {
                findPreemptiveLeaves(child, preemptiveLeaves);
            }
        }
    }

    /**
     *  Removes the numbers in the preemptive set from the other cells in the 
     *      neighborhood, then removes the cells of the preemptive set from the 
     *      map, and update the values of the cells in the neighborhood
     * @param neighborhood: the neighborhood to update
     * @param preemptiveSet: the preemptive set that will affect the neighborhood
     * @param cellsMap: the maps that organizes the cells of the neighborhood
     */
    public void updateNeighborhood(ArrayList<Cell> neighborhood,
            ArrayList<Cell> preemptiveSet,
            TreeMap<Cell, ArrayList<Coordinates>> cellsMap) {
        //Checks if there are still some cells being removed from the map
        boolean inAction = true;

        while (inAction && cellsMap.size() > 0) {
            inAction = false;
            for (Cell cell : preemptiveSet) {
                inAction = cellsMap.remove(cell) != null;
                //Remove the numbers in the preemptive set from the rest of
                //  the cells in the neighborhood
                for (Cell neighbor : neighborhood) {
                    if (!preemptiveSet.contains(cell)) {
                        for (int i : cell.markup) {
                            neighbor.markup.remove(i);
                        }
                    }
                    //Update the value of the cell
                    if (neighbor.markup.size() == 1) {
                        neighbor.value = neighbor.markup.pollFirst();
                    }
                }
            }
        }
        
        cellsMap.clear();
    }

    /**
     * Mini class used to store the coordinates for the map. In case there are
     *   two elements with the same key in the map, their respective coordinates
     *   will be added to the list of values
     */
    class Coordinates {

        int row;
        int column;

        Coordinates(int row, int column) {
            this.row = row;
            this.column = column;
        }

        Coordinates(Cell cell) {
            row = cell.row;
            column = cell.column;
        }

        public boolean equals(Object t) {
            Coordinates other = (Coordinates) t;
            return row == other.row && column == other.column;
        }
    }

}
