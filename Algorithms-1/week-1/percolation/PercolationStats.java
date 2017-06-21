import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private int trails;
    private double[] probability;
    private double sites;
    private double mean, stddev;
    private boolean meanCalculated, stdDevCalculated;
    
    public PercolationStats(int n, int trails)    // perform trials independent experiments on an n-by-n grid
    {
        if (n <= 0 || trails <= 0) {
            throw new java.lang.IllegalArgumentException("n and trails should be greate than zero");
        }
        
        this.trails = trails;        
        this.sites = n*n;
        this.probability = new double[this.trails];
        this.meanCalculated = false;
        this.stdDevCalculated = false;
        int rowIndex, colIndex;         
        for (int i = 0; i < this.trails; i++) {          
            Percolation percolation = new Percolation(n);          
            while (!percolation.percolates()) {
                rowIndex = StdRandom.uniform(1, n+1);
                colIndex = StdRandom.uniform(1, n+1);        
                if (!percolation.isOpen(rowIndex, colIndex)) {           
                    percolation.open(rowIndex, colIndex);
                }
            }           
            this.probability[i] = percolation.numberOfOpenSites()/this.sites;
            percolation = null;
        }       
    }
    
    public double mean()
    {
        if (!this.meanCalculated) {
            this.mean = StdStats.mean(this.probability);
            this.meanCalculated = true;
        }
        return this.mean;
    }
    public double stddev()
    {
        if (!this.stdDevCalculated) {
            if (this.trails == 1) {
                stddev = Double.NaN;
            }
            stddev = StdStats.stddev(this.probability);
            this.stdDevCalculated = true;
        }
        return stddev;      
    }
    public double confidenceLo() 
    {
        if (this.trails == 1) {
            return Double.NaN;
        }
        return this.mean() - ((1.96*this.stddev())/(Math.sqrt(this.trails)));       
    }
    public double confidenceHi()
    {
        if (this.trails == 1) {
            return Double.NaN;
        }
        return this.mean() + ((1.96*this.stddev())/(Math.sqrt(this.trails)));
    }
    public static void main(String[] args) {        
        int n, t;
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);      
            PercolationStats stats = new PercolationStats(n, t);        
            System.out.println("mean                    = " + stats.mean());
            System.out.println("stddev                  = " + stats.stddev());
            System.out.println("95% confidence interval = ["+  stats.confidenceLo() +" , " + stats.confidenceHi() +" ]");   
        }
    }
}