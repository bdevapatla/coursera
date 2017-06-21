import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private boolean[][] open;
    private int opened = 0;
    private int n = 0;
    private WeightedQuickUnionUF wquf;  
    public Percolation(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Grid size must be greathen than zero.");
        }   
        this.n = size;
        open = new boolean[this.n][this.n];     
        wquf = new WeightedQuickUnionUF(this.n*this.n+2);       
    }

    private void validate(int row, int col) {               
        if (row <= 0 || col <= 0 || row > this.n || col > this.n) {
            throw new java.lang.IndexOutOfBoundsException("row and col indices cannot be greater than :" +this.n);
        }       
    }

    private int xyTo1D(int row, int col) {
        validate(row, col);
        return (row-1)*this.n+col;
    }
    
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            open[row-1][col-1] = true;
            opened++;           
            if (row == 1) {
                wquf.union(0, xyTo1D(row, col));
            }           
            if (row == n) {
                wquf.union(n*n+1, xyTo1D(row, col));
            }
            if (col-1 >= 1 && isOpen(row, col-1)) {
                wquf.union(xyTo1D(row, col-1), xyTo1D(row, col));
            }           
            if (col+1 <= n && isOpen(row, col+1)) {
                wquf.union(xyTo1D(row, col+1), xyTo1D(row, col));
            }           
            if (row-1 >= 1 && isOpen(row-1, col)) {
                wquf.union(xyTo1D(row-1, col), xyTo1D(row, col));
            }           
            if (row+1 <= n && isOpen(row+1, col)) {
                wquf.union(xyTo1D(row+1, col), xyTo1D(row, col));
            }
        }
    }
    
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return open[row-1][col-1];
    }   

    public boolean isFull(int row, int col) {
        validate(row, col);
        return open[row-1][col-1] && wquf.connected(0, xyTo1D(row, col));
    }
    
    public int numberOfOpenSites() {
        return opened;
    }
    
    public boolean percolates() {
        return wquf.connected(0, this.n*this.n+1);
    }   
    
    public static void main(String[] args) {    
    }
}