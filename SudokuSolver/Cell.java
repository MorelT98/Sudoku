package SudokuSolver;

import java.util.Collection;
import java.util.TreeSet;

public class Cell implements Comparable<Cell>{

    //Coordinates of the cell
    int row;
    int column;

    //Value of the cell from 1 to 9, 0 if the cell has no value yet
    public int value;

    //Markups of the cells, which are the possibles values the
    //  cell can currently have
    public TreeSet<Integer> markup = new TreeSet();

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Cell(int row, int column, int value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    /**
     * Removes the given numbers from the markup of the cell
     * @param numbers: the numbers to remove
     * @return boolean: true if a number was removed, false if not 
     */
    public boolean removeFromMarkup(Collection<Integer> numbers) {
        boolean removed = false;
        for (int number : numbers) {
            if (markup.remove(number)) {
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Removes one number from the markup of the cell
     * @param number: the number to be removed
     * @return boolean: true if the number was removed, false if it was 
     *    not in the markup
     */
    public boolean removeFromMarkup(int number) {
        return markup.remove(number);
    }
    
    @Override
    public boolean equals(Object other) {
        return compareTo((Cell)other) == 0;
    }

    @Override
    public int compareTo(Cell other) {
        if (convertMarkupToInteger(markup) == convertMarkupToInteger(other.markup)) {
            return 0;
        } else {
            if(convertMarkupToInteger(markup) < convertMarkupToInteger(other.markup)){
                return -1;
            } else {
                return 1;
            }
        }
    }
    
    private int convertMarkupToInteger(TreeSet<Integer> markup) {
        if(markup == null || markup.size() == 0) {
            return 0;
        }
        int result = 0;
        for(int i:markup) {
            result = result * 10 + i;
        }
        return result;
    }
}
