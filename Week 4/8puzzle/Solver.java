/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Solver {

    private SearchNode finalNode;
    private boolean isSolvable;

    private class SearchNode {
        public Board board;
        public int movesMade;
        public SearchNode previous;
        public int manhattan;

        public SearchNode(Board board, int movesMade, SearchNode previous, int manhattan) {
            this.board = board;
            this.movesMade = movesMade;
            this.previous = previous;
            this.manhattan = manhattan;
        }
    }

    private class hammingComparator implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return Integer
                    .compare(a.board.hamming() + a.movesMade, b.board.hamming() + b.movesMade);
        }
    }

    private class manhattanComparator implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return Integer
                    .compare(a.manhattan + a.movesMade, b.manhattan + b.movesMade);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        Board twinBoard = initial.twin();

        MinPQ<SearchNode> PQ = new MinPQ<SearchNode>(new manhattanComparator());
        PQ.insert(new SearchNode(initial, 0, null, initial.manhattan()));
        MinPQ<SearchNode> PQTwin = new MinPQ<SearchNode>(new manhattanComparator());
        PQTwin.insert(new SearchNode(twinBoard, 0, null, twinBoard.manhattan()));

        while (true) {
            SearchNode currentNode = PQ.delMin();
            if (currentNode.board.isGoal()) {
                isSolvable = true;
                finalNode = currentNode;
                break;
            }
            for (Board neighbour : currentNode.board.neighbors()) {
                if (currentNode.previous == null || !neighbour.equals(currentNode.previous.board)) {
                    PQ.insert(new SearchNode(neighbour, currentNode.movesMade + 1, currentNode,
                                             neighbour.manhattan()));
                }
            }

            currentNode = PQTwin.delMin();
            if (currentNode.board.isGoal()) {
                isSolvable = false;
                finalNode = currentNode;
                break;
            }
            for (Board neighbour : currentNode.board.neighbors()) {
                if (currentNode.previous == null || !neighbour.equals(currentNode.previous.board)) {
                    PQTwin.insert(
                            new SearchNode(neighbour, currentNode.movesMade + 1, currentNode,
                                           neighbour.manhattan()));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return finalNode.movesMade;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Board[] tempSolutions = new Board[finalNode.movesMade + 1];
        SearchNode currentSolutionsNode = finalNode.previous;
        tempSolutions[finalNode.movesMade] = finalNode.board;

        for (int i = finalNode.movesMade - 1; i >= 0; i--) {
            tempSolutions[i] = currentSolutionsNode.board;
            currentSolutionsNode = currentSolutionsNode.previous;
        }

        List<Board> solutions = Arrays.asList(tempSolutions);
        return solutions;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
