/* *****************************************************************************
 *  Name:              Guilherme Santos
 *  Last modified:     22/01/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    private double[] openCellsFractions;
    private int gridSize;
    private int trials;
    private int openCellsCount = 0;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new java.lang.IllegalArgumentException();

        this.gridSize = n;
        this.trials = trials;
        openCellsFractions = new double[trials];

        for (int trialNumber = 0; trialNumber < trials; trialNumber++) {

            Percolation percolation = new Percolation(gridSize);

            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(1, gridSize + 1);
                int col = StdRandom.uniformInt(1, gridSize + 1);

                if (!percolation.isOpen(row, col)) {
                    this.openCellsCount++;
                    percolation.open(row, col);
                }

            }
            // Calc the estimate percolate threshold
            openCellsFractions[trialNumber] = (double) this.openCellsCount / (gridSize * gridSize);
            this.openCellsCount = 0;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.openCellsFractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (trials == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(this.openCellsFractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - ((1.96 * this.stddev()) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + ((1.96 * this.stddev()) / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {

        if (args.length != 2) return;

        int gridSize = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);


        Stopwatch sw = new Stopwatch();
        PercolationStats percolationStats = new PercolationStats(gridSize, trials);
        double elapsedTime = sw.elapsedTime();

        System.out.println("elapsed time              " + elapsedTime);
        System.out.printf("mean=                     %f%n", percolationStats.mean());
        System.out.printf("stddev=                   %f%n", percolationStats.stddev());
        System.out.printf("95%% confidence interval=  %f, %f%n", percolationStats.confidenceLo(),
                          percolationStats.confidenceHi());

    }
}
