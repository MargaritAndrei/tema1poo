package simulation;

import simulation.Cell;

public class Table {
    private Cell[][] grid;
    public Table(int height, int width) {
        grid = new Cell[height][width];
        this.height = height;
        this.width = width;
    }
    private int height, width;
    public void setCell(int x, int y, Cell cell) {
        this.grid[x][y] = cell;
    }
    public Cell getCell(int x, int y) {
        if (0 <= x && x < height && 0 <= y && y < width) {
            return grid[x][y];
        }
        return null;
    }
}