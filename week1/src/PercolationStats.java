import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] res;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        this.res = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation pcl = new Percolation(n);

            do {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;

                pcl.open(row, col);

            } while (!pcl.percolates());

            this.res[i] = (double) pcl.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.res);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.res);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - this.stddev();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + this.stddev();
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);

        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}