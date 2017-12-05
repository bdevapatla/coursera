import java.util.Iterator;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {

	private SET<Point2D> set;

	// Construct an empty set of points
	public PointSET() {
		set = new SET<>();
	}

	// Is the set empty?
	public boolean isEmpty() {
		return set.isEmpty();
	}

	// number of points in the set
	public int size() {
		return set.size();
	}

	// add the point to the set, if it not already part of it
	public void insert(Point2D p) {
		if (p == null) {
			throw new java.lang.IllegalArgumentException("Invalid input to insert() method.");
		}
		set.add(p);
	}

	// Does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null) {
			throw new java.lang.IllegalArgumentException("Invalid input to contains() method.");
		}
		return set.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Iterator<Point2D> iterator = set.iterator(); iterator.hasNext();) {
			Point2D p = iterator.next();
			p.draw();
		}
	}

	// all the points that are inside the rectangle or on the boundary
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new java.lang.IllegalArgumentException("Invalid input to range() method.");
		}
		Stack<Point2D> points = new Stack<>();
		for (Iterator<Point2D> iterator = set.iterator(); iterator.hasNext();) {
			Point2D p = iterator.next();
			if ((p.x() >= rect.xmin() && p.x() <= rect.xmax()) && (p.y() >= rect.ymin() && p.y() <= rect.ymax())) {
				points.push(p);
			}
		}
		return points;
	}

	// a nearest neighbor in the set to point p, null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new java.lang.IllegalArgumentException("Invalid input to nearest() method.");
		}
		Point2D currentChampion = null;
		double bestNearestDistance = Double.NaN;		
		for (Iterator<Point2D> iterator = set.iterator(); iterator.hasNext();) {
			Point2D q = iterator.next();
			double distance = p.distanceSquaredTo(q);
			if (Double.isNaN(bestNearestDistance) || Double.compare(distance, bestNearestDistance) == -1) {
				bestNearestDistance = distance;
				currentChampion = q;
			}
		}
		return currentChampion;
	}

	public static void main(String[] args) {

	}
}
