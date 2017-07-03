
public class TestBCP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Point p1 = new Point(0, 0);
    	Point p2 = new Point(1, 1);
    	Point p3 = new Point(1, 3);
    	Point p4 = new Point(4, 1);
    	Point p5 = new Point(5, 1);
    	Point p6 = new Point(2,2);
    	Point p7 = new Point(3,1);
    	Point p8 = new Point(4,0);
    	//Point p9 = new Point(4,0);

    	Point[] points = {p1,p2,p3,p4,p5,p6,p7,p8};
    	BruteCollinearPoints bcp = new BruteCollinearPoints(points);
    	int numberOfSegments = bcp.numberOfSegments();
    	LineSegment[] segments = bcp.segments();
    	System.out.println(numberOfSegments);
    	for(int i=0; i < numberOfSegments; i++){
    		System.out.println(segments[i]);
    		segments[i].draw();
    	}
	}

}
