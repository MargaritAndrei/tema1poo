package simulation;

public final class Table {
    private Cell[][] grid;
    private int height, width;
    public Table(final int height, final int width) {
        grid = new Cell[height][width];
        this.height = height;
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    /**
     * Sets a Cell object to the Cell matrix.
     */
    public void setCell(final int x, final int y, final Cell cell) {
        this.grid[x][y] = cell;
    }
    /**
     * Getter function for the Cell object located at x row and y collumn in the matrix.
     */
    public Cell getCell(final int x, final int y) {
        if (0 <= x && x < height && 0 <= y && y < width) {
            return grid[x][y];
        }
        return null;
    }
}
