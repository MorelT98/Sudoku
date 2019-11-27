/**
 *
 * @author morel
 */
public class Sudoku {

    static Grid grid;

    public static void main(String[] args) {
        Cell[][] cells = new Cell[9][9];
        Scanner input = new Scanner(System.in);
        String row = new String();
        int rowNumber = 0;
        System.out.println("Enter grid to solve:");
        for (int i = 0; i < 9; i++) {
            row = input.nextLine();
            fillGrid(row, cells, rowNumber);
            rowNumber++;
        }
        grid = new Grid(cells);

        printGridWithMarkups(grid);

//        System.out.println("Enter the cell you want the neighborhood from:");
//        System.out.print("Row: ");
//        int rowIndex = input.nextInt();
//        System.out.print("Column: ");
//        int columnIndex = input.nextInt();
//        
//        print3x3Grid(grid, rowIndex, columnIndex);
//        printRow(grid, rowIndex, columnIndex);
//        printColumn(grid, rowIndex, columnIndex);
//        printAdjCells(grid, rowIndex, columnIndex);
        grid.markup();
        printGridWithMarkups(grid);

        printAllPreemptiveSets();
        //grid.getPreemptiveSet(grid.getRow(1, 0, true));

        System.out.println("\n\nUpdated grid:");
        printGridWithMarkups(grid);
        
        System.out.println("\n\nUpdated grid with row preemptive sets removed:");
        grid.useAllPreemptiveSets();
        printGridWithMarkups(grid);

    }

    public static void printAllPreemptiveSets() {
        System.out.println("Preemptive sets: \n");
        //Rows
        System.out.println("Rows:");
        for (int i = 0; i < 9; i++) {
            System.out.println("Row #" + i + ":");
            printPreemptiveSet(grid.getPreemptiveSet(grid.getRow(i, 0, true)));
        }

        //Columns
        System.out.println("Columns:");
        for (int i = 0; i < 9; i++) {
            System.out.println("Column #" + i + ":");
            printPreemptiveSet(grid.getPreemptiveSet(grid.getColumn(0, i, true)));
        }

        //3x3 Grids
        System.out.println("3x3 Grids");
        for (int i = 0; i < 9; i+=3) {
            for (int j = 0; j < 9; j+=3) {
                System.out.println("Grid (" + i + ", " + j + "):");
                printPreemptiveSet(grid.getPreemptiveSet(grid.get3x3Grid(i, j, true)));
            }
        }
    }

    public static void fillGrid(String row, Cell[][] cells, int rowNumber) {
        for (int i = 0; i < 9; i++) {
            cells[rowNumber][i] = new Cell(rowNumber, i, Integer.parseInt(row.charAt(i) + ""));
        }
    }

    public static void printGridWithMarkups(Grid grid) {
        System.out.println("Current 9x9 grid with markups: ");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid.grid[i][j].value + "         ");
            }
            System.out.println();
            for (int j = 0; j < 9; j++) {
                for (int markup : grid.grid[i][j].markup) {
                    System.out.print(markup);
                }
                for (int k = grid.grid[i][j].markup.size(); k < 10; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public static void printGrid(Grid grid) {
        System.out.println("Current 9x9 grid: ");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid.grid[i][j].value);
            }
            System.out.println();
        }
    }

    public static void print3x3Grid(Grid grid, int row, int column) {
        System.out.println("Values in the 3x3 grid: ");
        ArrayList<Cell> _3x3Grid = grid.get3x3Grid(row, column, false);
        for (Cell cell : _3x3Grid) {
            System.out.print(cell.value + " ");
        }
        System.out.println();
    }

    public static void printRow(Grid grid, int row, int column) {
        System.out.println("Values in the row: ");
        ArrayList<Cell> rowList = grid.getRow(row, column, false);
        for (Cell cell : rowList) {
            System.out.print(cell.value + " ");
        }
        System.out.println();
    }

    public static void printColumn(Grid grid, int row, int column) {
        System.out.println("Values in the column: ");
        ArrayList<Cell> columnList = grid.getColumn(row, column, false);
        for (Cell cell : columnList) {
            System.out.print(cell.value + " ");
        }
        System.out.println();
    }

    public static void printAdjCells(Grid grid, int row, int column) {
        System.out.println("Adjacent cells: ");
        ArrayList<Cell> adjCells = grid.getAdjCells(row, column, false);
        for (int i = 0; i < adjCells.size(); i++) {
            System.out.print(adjCells.get(i).value + " ");
            if (i == 9) {
                System.out.println();
            }
        }
        System.out.println();
    }

    public static void printPreemptiveSet(ArrayList<Cell> set) {
        if (set == null || set.isEmpty()) {
            System.out.println("");
            return;
        }
        for (int i = 0; i < set.size(); i++) {
            System.out.print(set.get(i).value + "         ");
        }
        System.out.println();
        for (int i = 0; i < set.size(); i++) {
            for (int j : set.get(i).markup) {
                System.out.print(j);
            }
            for (int j = set.get(i).markup.size(); j < 10; j++) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

}
