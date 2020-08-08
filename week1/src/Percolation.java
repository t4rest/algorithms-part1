import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean percolate;
    private final int n;
    private final int tvs; // top virtual site
    private final int bvs; // bottom virtual site
    private int nOpenSites = 0;
    private final boolean[] openSites;
    private final WeightedQuickUnionUF wqu;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        this.tvs = n * n;
        this.bvs = n * n + 1;
        this.wqu = new WeightedQuickUnionUF(n * n + 2);
        this.openSites = new boolean[n * n];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        this.validateRowCol(row, col);

        int site = this.twoD2oneD(row, col);

        // already open
        if (this.openSites[site]) {
            return;
        }

        // top row
        if (row == 1) {
            // connect to tvs
            this.wqu.union(site, this.tvs);
        } else {
            // connect to topSite if open
            int topSite = site - this.n;
            if (this.openSites[topSite]) {
                this.wqu.union(site, topSite);
            }
        }

        // bottom row
        if (row == this.n) {
            // connect to bvs
            this.wqu.union(site, this.bvs);
        } else {
            // connect to bottomSite if open
            int bottomSite = site + this.n;
            if (this.openSites[bottomSite]) {
                this.wqu.union(site, bottomSite);
            }
        }

        // if not left column
        if (col != 1) {
            // connect left if open
            int leftSite = site - 1;
            if (this.openSites[leftSite]) {
                this.wqu.union(site, leftSite);
            }
        }

        // if not right column
        if (col != this.n) {
            // connect right if open
            int rightSite = site + 1;
            if (this.openSites[rightSite]) {
                this.wqu.union(site, rightSite);
            }
        }

        this.openSites[site] = true;
        this.nOpenSites++;
        this.percolate = this.wqu.find(this.tvs) == this.wqu.find(this.bvs);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        this.validateRowCol(row, col);

        int site = this.twoD2oneD(row, col);

        return this.openSites[site];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        this.validateRowCol(row, col);

        int site = this.twoD2oneD(row, col);
        if (!this.openSites[site]) {
            return false;
        }

        return this.wqu.find(this.tvs) == this.wqu.find(site);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.nOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.percolate;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);

        System.out.println(p.isOpen(2, 2));
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());

        p.open(2, 2);
        System.out.println(p.isOpen(2, 2));
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());

        p.open(1, 2);
        System.out.println(p.percolates());
    }

    // return index of one-dimensional array
    private int twoD2oneD(int row, int col) {
        return (row - 1) * this.n + col - 1;
    }

    private void validateRowCol(int row, int col) throws IllegalArgumentException {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            throw new IllegalArgumentException("Illegal argument!");
        }
    }

}