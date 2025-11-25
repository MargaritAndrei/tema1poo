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
     * Sets a Cell object to the Cell matrix at the specified coordinates.
     *
     * @param x    The row index.
     * @param y    The column index.
     * @param cell The Cell object to set.
     */
    public void setCell(final int x, final int y, final Cell cell) {
        grid[x][y] = cell;
    }
    /**
     * Retrieves the Cell object located at the specified row and column in the matrix.
     *
     * @param x The row index.
     * @param y The column index.
     * @return The Cell object at the specified coordinates, or null if out of bounds.
     */
    public Cell getCell(final int x, final int y) {
        if (0 <= x && x < height && 0 <= y && y < width) {
            return grid[x][y];
        }
        return null;
    }
}
