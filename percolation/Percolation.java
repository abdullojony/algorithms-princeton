import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private boolean[] open;
    private int count;
    private int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        uf = new WeightedQuickUnionUF(n * n + 2);
        open = new boolean[n * n + 2];
        open[0] = true;
        open[n * n + 1] = true;
        count = 0;
        this.n = n;
    }

    private int getIndex(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) throw new IllegalArgumentException("row and col should be between 1 and n");
        return ((row - 1) * n + col);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int i = getIndex(row, col);
        if (open[i]) return;
        open[i] = true;
        count++;
        if (row == 1) uf.union(0, i);
        if (row == n) uf.union(n * n + 1, i);
        if (row > 1 && isOpen(row - 1, col)) uf.union(i - n, i);
        if (row < n && isOpen(row + 1, col)) uf.union(i + n, i);
        if (col > 1 && isOpen(row, col - 1)) uf.union(i - 1, i);
        if (col < n && isOpen(row, col + 1)) uf.union(i + 1, i); 
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int i = getIndex(row, col);
        return open[i];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int i = getIndex(row, col);
        return uf.find(0) == uf.find(i);
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