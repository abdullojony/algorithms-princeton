import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final boolean[] open;
    private int count;
    private final int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than 0");
        uf = new WeightedQuickUnionUF(n * n + 2);
        open = new boolean[n * n + 2];
        open[0] = true;
        open[n * n + 1] = true;
        this.n = n;
        count = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException("row and col must be between 1 and n");
        int index = (row - 1) * n + col;
        if (open[index]) return;
        open[index] = true;
        count++;
        if (row == 1) uf.union(0, index);
        if (row == n) uf.union(n * n + 1, index);
        if (row > 1 && isOpen(row - 1, col)) uf.union(index, index - n);
        if (row < n && isOpen(row + 1, col)) uf.union(index, index + n);
        if (col > 1 && isOpen(row, col - 1)) uf.union(index, index - 1);
        if (col < n && isOpen(row, col + 1)) uf.union(index, index + 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException("row and col must be between 1 and n");
        int index = (row - 1) * n + col;
        return open[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException("row and col must be between 1 and n");
        int index = (row - 1) * n + col;
        return uf.find(0) == uf.find(index);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(n * n + 1);
    }
}
