import java.util.Arrays;

public class FastCollinearPoints {
   
    private LineSegment[] segments; 
    private int counter;
    
    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        validate(points);
        int N = points.length;
        segments = new LineSegment[0];
        Point[] tempPoints = new Point[N];
        for (int i = 0; i < N; i++) {
            tempPoints[i] = points[i];
        }       
        for (int i = 0; i < N; i++) {           
            Arrays.sort(tempPoints, points[i].slopeOrder());            
            SlopePoint[] slopePoints = new SlopePoint[N-1];
            int slopeCounter = 0;
            for (int j = 0; j < N; j++) {
                if (points[i].compareTo(tempPoints[j]) != 0) {
                    double slope = points[i].slopeTo(tempPoints[j]);
                    if (slopeCounter > 0  && slope != slopePoints[slopeCounter-1].slope) {
                        if (slopeCounter < 3) {
                            slopePoints = new SlopePoint[N-1];
                            slopeCounter = 0;
                        }
                        else {
                            break;
                        }
                    }
                    slopePoints[slopeCounter] = new SlopePoint();
                    slopePoints[slopeCounter].slope = slope;
                    slopePoints[slopeCounter].point = tempPoints[j];
                    slopeCounter++;
                }
            }           
            if (slopeCounter >= 3) {
                Point[] colinearPoints = new Point[slopeCounter+1];
                for (int k = 0; k < slopeCounter; k++) {
                    colinearPoints[k] = slopePoints[k].point;
                }
                colinearPoints[slopeCounter] = points[i];
                Arrays.sort(colinearPoints);
                LineSegment lineSegment = new LineSegment(colinearPoints[0], colinearPoints[colinearPoints.length-1]);
                save(lineSegment);
            }           
        }       
    }
   
    public int numberOfSegments()        // the number of line segments
    {
        return counter;
    }
    public LineSegment[] segments()                // the line segments
    {
        return segments;
    }
    
    private class SlopePoint {
        private double slope;
        private Point point;
    }
    
    private void save(LineSegment lineSegment) {           
        boolean isDuplicate = false;
        LineSegment[] temp = new LineSegment[counter+1];
        for (int j = 0; j < segments.length; j++) {
            if (!lineSegment.toString().equals(segments[j].toString())) {
                temp[j] = segments[j];
            }
            else { 
                isDuplicate = true;
                break;
            }
        }
        if (!isDuplicate) {
            segments = temp;
            segments[counter] = lineSegment;
            counter++;
        }
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