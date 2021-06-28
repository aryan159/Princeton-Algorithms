/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.LinkedList;

public class Board {

    private final int[][] tiles;
    private final int n;
    private final int hamming;
    private final int manhattan;
    private int[] zeroPosition;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = tiles.clone();

        int currentTile;

        int preHamming = 0;
        int preManhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                currentTile = tiles[i][j];
                if (currentTile != i * n + j + 1 && i + j != 2 * (n - 1)) preHamming++;
                if (currentTile != 0) {
                    preManhattan += Math.abs((currentTile - 1) / n - i) + Math
                            .abs((currentTile - 1) % n - j);
                }
                else zeroPosition = new int[] { i, j };
            }
        }

        hamming = preHamming;
        manhattan = preManhattan;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // string representation of this board
    public String toString() {
        String preOutput = n + "\n";

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                preOutput += " " + tiles[i][j];
            }
            preOutput += "\n";
        }
        return preOutput;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hamming == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbours = new LinkedList<Board>();
        if (zeroPosition[0] > 0) { // not in 1st row
            neighbours.add(exch(this, zeroPosition[0], zeroPosition[1], zeroPosition[0] - 1,
                                zeroPosition[1]));
        }
        if (zeroPosition[0] < n - 1) { // not in last row
            neighbours.add(exch(this, zeroPosition[0], zeroPosition[1], zeroPosition[0] + 1,
                                zeroPosition[1]));
        }
        if (zeroPosition[1] > 0) { // not in 1st col
            neighbours.add(exch(this, zeroPosition[0], zeroPosition[1], zeroPosition[0],
                                zeroPosition[1] - 1));
        }
        if (zeroPosition[1] < n - 1) { // not in last col
            neighbours.add(exch(this, zeroPosition[0], zeroPosition[1], zeroPosition[0],
                                zeroPosition[1] + 1));
        }
        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (zeroPosition[0] == 0 && zeroPosition[1] < 2) return exch(this, 1, 0, 1, 1);
        else return exch(this, 0, 0, 0, 1);

    }

    // returns a new Board after making the exchange
    private Board exch(Board board, int rowA, int colA, int rowB, int colB) {
        int[][] newTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newTiles[i][j] = board.tiles[i][j];
            }
        }
        newTiles[rowA][colA] = board.tiles[rowB][colB];
        newTiles[rowB][colB] = board.tiles[rowA][colA];
        Board newBoard = new Board(newTiles);
        return newBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 1, 0 }, { 2, 3 } };
        Board a = new Board(tiles);
        tiles = new int[][] { { 0, 2, 3 }, { 4, 5, 6 }, { 8, 7, 1 } };
        Board b = new Board(tiles);
        tiles = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board c = new Board(tiles);
        System.out.println(a.toString());
    }

}
