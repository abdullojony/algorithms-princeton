import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n and trials must be greater than 0");
        double[] thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                percolation.open(row, col);
            }
            thresholds[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        double confidence = 1.96 * stddev / Math.sqrt(trials);
        confidenceLo = mean - confidence;
        confidenceHi = mean + confidence;
    }

    // sample mean of percolation threshold
    public double mean() { return mean; }

    // sample standard deviation of percolation threshold
    public double stddev() { return stddev; }

    // low endpoint of 95% confidence interval
    public double confidenceLo() { return confidenceLo; }

    // high endpoint of 95% confidence interval
    public double confidenceHi() { return confidenceHi; }

   // test client (see below)
   public static void main(String[] args) {
         int n = Integer.parseInt(args[0]);
         int trials = Integer.parseInt(args[1]);
         PercolationStats stats = new PercolationStats(n, trials);
         System.out.println("mean                    = " + stats.mean());
         System.out.println("stddev                  = " + stats.stddev());
         System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
   }
}
