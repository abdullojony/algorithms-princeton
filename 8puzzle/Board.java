import java.util.List;
import java.util.ArrayList;

public class Board {
    private final int[][] board;
    private final int size;
    private int i0, j0;

    // create a board from an n-by-n array of tiles, O(n2)
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.size = tiles.length;
        this.board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    this.i0 = i;
                    this.j0 = j;
                }
            }
        }
    }

    // number of tiles out of place O(n2)
    public int hamming() {
        int h = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0 && board[i][j] != i * size + j + 1) {
                    h++;
                }
            }
        }
        return h;
    }

    // is this board the goal board? O(n2)
    public boolean isGoal() {
        return hamming() == 0;
    }

    // sum of Manhattan distances between tiles and goal O(n2)
    public int manhattan() {
        int m = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0 && board[i][j] != i * size + j + 1) {
                    int linearIndex = board[i][j] - 1;
                    int oi = linearIndex / size;
                    int oj = (linearIndex - oi * size) % size;
                    m += Math.abs(i - oi) + Math.abs(j - oj);
                }
            }
        }
        return m;
    }

    // all neighboring boards O(n2)
    public Iterable<Board> neighbors() {
        List<Board> ls = new ArrayList<>();
        if (j0 < size - 1) {
            swap(i0, j0, i0, j0+1);
            ls.add(new Board(board));
            swap(i0, j0, i0, j0+1);
        }
        if (j0 > 0) {
            swap(i0, j0, i0, j0-1);
            ls.add(new Board(board));
            swap(i0, j0, i0, j0-1);
        }
        if (i0 < size - 1) {
            swap(i0, j0, i0+1, j0);
            ls.add(new Board(board));
            swap(i0, j0, i0+1, j0);
        }
        if (i0 > 0) {
            swap(i0, j0, i0-1, j0);
            ls.add(new Board(board));
            swap(i0, j0, i0-1, j0);
        }
        return ls;
    }

    // a board that is obtained by exchanging any pair of tiles O(n2)
    public Board twin() {
        Board b = null;
        int i = i0 > 0 ? i0 - 1 : i0 + 1;
        int j = j0 > 0 ? j0 - 1 : j0 + 1;
        swap(i, j0, i0, j);
        b = new Board(board);
        swap(i, j0, i0, j);
        return b;
    }

    private void swap(int i1, int j1, int i2, int j2) {
        int tmp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = tmp;
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) return false;
        Board other = (Board) y;
        if (this.dimension() != other.dimension()) return false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.board[i][j] != other.board[i][j]) return false;
            }
        }
        return true;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size);
        sb.append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(" ");
                sb.append(board[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Board b1 = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
        Board b2 = new Board(new int[][]{{8, 2, 3}, {4, 0, 2}, {7, 6, 5}});
        System.out.println(b1.equals(b2));
        System.out.print(b1);
        System.out.println(b1.hamming());
        System.out.println(b1.manhattan());
        Board b3 = new Board(new int[][]{{1, 0, 3}, {4, 2, 5}, {7, 8, 6}});
        System.out.println(b3);
        int i = 0;
        for (Board b : b3.neighbors()) {
            i++;
            System.out.println(b.toString());
        }
        System.out.println(i + " neighbors.");
    }
}