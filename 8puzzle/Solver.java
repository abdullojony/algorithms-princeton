import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private SearchNode solution;

    private static class SearchNode implements Comparable<SearchNode> {
        private final SearchNode parent;
        private final Board board;
        private final int moves;
        private final int priority;

        public SearchNode(SearchNode parent, Board board, int moves) {
            this.parent = parent;
            this.board = board;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode other) {
            return this.priority - other.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board can't be null.");
        }
        MinPQ<SearchNode> pq1 = new MinPQ<>();
        MinPQ<SearchNode> pq2 = new MinPQ<>();
        pq1.insert(new SearchNode(null, initial, 0));
        pq2.insert(new SearchNode(null, initial.twin(), 0));
        while (true) {
            SearchNode s;

            // initial board
            if (!pq1.isEmpty()) {
                s = pq1.delMin();
                if (s.board.isGoal()) {
                    this.solution = s;
                    break;
                }
                if (s.parent == null || s.parent.parent == null || 
                    !s.board.equals(s.parent.parent.board)) {
                    for (Board b : s.board.neighbors()) {
                        pq1.insert(new SearchNode(s, b, s.moves + 1));
                    }
                }
            }

            // twin board
            if (!pq2.isEmpty()) {
                s = pq2.delMin();
                if (s.board.isGoal()) {
                    this.solution = null;
                    break;
                }
                if (s.parent == null || s.parent.parent == null || 
                    !s.board.equals(s.parent.parent.board)) {
                    for (Board b : s.board.neighbors()) {
                        pq2.insert(new SearchNode(s, b, s.moves + 1));
                    }
                } 
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        else return solution.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        List<Board> ls = new ArrayList<>();
        SearchNode node = solution;
        while (node != null) {
            ls.add(node.board);
            node = node.parent;
        }
        Collections.reverse(ls);
        return ls;
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