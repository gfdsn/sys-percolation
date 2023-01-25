import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] searchingDirections = new int[][] {
            { 1, 0 },
            { -1, 0 },
            { 0, -1 },
            { 0, 1 }
    };

    private WeightedQuickUnionUF weightedQuickUnion;
    private WeightedQuickUnionUF weightedQuickUnionSup;

    private int size;
    private int[] grid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n < 0) {
            throw new IllegalArgumentException();
        }

        this.size = n;

        // example: 5 * 5 = 25 + 2 (top and bottom virtual sites)
        weightedQuickUnion = new WeightedQuickUnionUF((n * n) + 2);
        weightedQuickUnionSup = new WeightedQuickUnionUF((n * n) + 1);

        // connect every site at the top row to top virtual site
        for (int i = 1; i < n + 1; i++) {
            weightedQuickUnion.union(i, 0);
            weightedQuickUnionSup.union(i, 0);
        }

        // connect every site at the bottom row to the bottom virtual site
        // (n * n) is the bottom row
        // +1 so you we get to the remaining site
        for (int i = n * n; i > n * n - n; i--) {
            weightedQuickUnion.union(i, (n * n) + 1);
        }

        // N-by-N grid
        // All the sites are blocked by default
        grid = new int[n * n];

        for (int i = 0; i < n * n; i++) {
            grid[i] = 0;
        }
    }

    // Get site index
    private int getSiteIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    private boolean vaidateIndex(int row, int col) {
        if (row > 0 && row <= size && col > 0 && col <= size) {
            return true;
        }
        return false;
    }

    public void open(int row, int col) {

        if (!vaidateIndex(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }

        int mainSiteRow = row;
        int mainSiteCol = col;

        if (isBlocked(row, col)) {
            // Open site
            if (!isOpen(row, col)) {
                grid[getSiteIndex(row, col)] = 1;
            }

            for (int sd = 0; sd < searchingDirections.length; sd++) {

                // change the row to the row before and after
                row = mainSiteRow + searchingDirections[sd][0];

                // change the col to the col before and after
                col = mainSiteCol + searchingDirections[sd][1];

                if (vaidateIndex(row, col) && isOpen(row, col)) {

                    int mainSiteIndex = getSiteIndex(mainSiteRow, mainSiteCol) + 1;
                    int connectedSiteIndex = getSiteIndex(row, col) + 1;

                    weightedQuickUnion.union(connectedSiteIndex, mainSiteIndex);
                    weightedQuickUnionSup.union(connectedSiteIndex, mainSiteIndex);

                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (vaidateIndex(row, col)) {
            return grid[getSiteIndex(row, col)] == 1;
        }
        throw new java.lang.IndexOutOfBoundsException();
    }

    // is the site (row, col) open?
    private boolean isBlocked(int row, int col) {
        if (vaidateIndex(row, col)) return grid[getSiteIndex(row, col)] == 0;
        throw new java.lang.IndexOutOfBoundsException();
    }


    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (vaidateIndex(row, col)) {
            if (isOpen(row, col)) {

                int index = getSiteIndex(row, col) + 1;

                if (index < size) {

                    return true;

                }

                return weightedQuickUnionSup.find(index) == weightedQuickUnionSup.find(0);
            }
        }
        else {
            throw new java.lang.IllegalArgumentException();
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int openSites = 0;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 1) {
                openSites++;
            }
        }
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        int virtualTopSite = 0;
        int virtualBottomSite = size * size + 1;

        // If there is only 1 site
        if (virtualBottomSite == 2) {
            return isOpen(1, 1);
        }

        // If there are no sites
        if (virtualBottomSite == 0) {
            return false;
        }

        return weightedQuickUnion.find(virtualTopSite) == weightedQuickUnion.find(
                virtualBottomSite);
    }

    public static void main(String[] args) {

    }
}



