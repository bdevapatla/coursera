import java.util.Arrays;

public class BruteCollinearPoints {
    
    private LineSegment[] segments; 
    
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        validate(points);
        int N = points.length;
        int counter = 0;        
        segments = new LineSegment[0];
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                for (int k = j+1; k < N; k++) {
                    for (int l = k+1; l < N; l++) {                     
                        double slope1 = points[i].slopeTo(points[j]);
                        double slope2 = points[i].slopeTo(points[k]);
                        double slope3 = points[i].slopeTo(points[l]);
                        if (slope1 == slope2 && slope2 == slope3) {
                            counter++;                          
                            Point[] colinearPoints = {points[i], points[j], points[k], points[l]};
                            Arrays.sort(colinearPoints);
                            if (segments.length < counter) {
                                resize(counter);
                            }
                            //sort 4 elements to display the line segment                           
                            segments[segments.length-1] = new LineSegment(colinearPoints[0], colinearPoints[3]);
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments()        // the number of line segments
    {
        return segments.length;
    }
    public LineSegment[] segments()                // the line segments
    {
        return segments;
    }
    
    private void resize(int capacity) {
        LineSegment[] temp = new LineSegment[capacity];        
        for (int j = 0; j < segments.length; j++) {
            temp[j] = segments[j];
        }
        segments = temp;
    }
     
    private void validate(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException("Input points cannot be null");
        }       
        for (int i = 0; i < points.length; i++) {           
            for (int j = i+1; j < points.length; j++) {
                if (points[i] == null || points[j] == null) {
                    throw new java.lang.IllegalArgumentException("Input contains either null elements");
                }
                if (points[i].compareTo(points[j]) == 0) {
                    throw new java.lang.IllegalArgumentException("Input contains duplicate elements");
                }
            }
        }       
    }
}
