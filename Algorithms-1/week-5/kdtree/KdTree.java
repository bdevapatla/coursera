import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

	private static class Node {
		private Point2D p; // the point
		private RectHV rect; // the axis-aligned rectangle corresponding to this
								// node
		private Node lb; // the left/bottom subtree
		private Node rt; // the right/top subtree

		public Node(Point2D p, RectHV rect, Node lb, Node rt) {
			this.p = p;
			this.rect = rect;
			this.lb = lb;
			this.rt = rt;
		}
	}

	private Node root;
	private int N;
	private static final String HORIZONTAL = "horizontal";
	private static final String VERTICAL = "vertical";

	// Construct an empty set of points
	public KdTree() {
		N = 0;
	}

	// Is the set empty?
	public boolean isEmpty() {
		return N == 0;
	}

	// number of points in the set
	public int size() {
		return N;
	}

	// add the point to the set, if it not already part of it
	public void insert(Point2D p) {
		N++;
		root = insert(root, p, VERTICAL);
		root.rect = new RectHV(0, 0, 1, 1);
	}

	private Node insert(Node node, Point2D p, String orientation) {
		if (node == null) {
			node = new Node(p, null, null, null);
			return node;
		}
		double cmp;
		if (orientation.equals(VERTICAL)) {
			cmp = p.x() - node.p.x();
		} else {
			cmp = p.y() - node.p.y();
		}
		if (cmp < 0.0) {
			node.lb = insert(node.lb, p, orientation.equals(VERTICAL) ? HORIZONTAL : VERTICAL);
			node.lb.rect = orientation.equals(VERTICAL)
					? new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax())
					: new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
		} else if (cmp > 0.0) {
			node.rt = insert(node.rt, p, orientation.equals(VERTICAL) ? HORIZONTAL : VERTICAL);
			node.rt.rect = orientation.equals(VERTICAL)
					? new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax())
					: new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
		} else {
			if (orientation.equals(VERTICAL) && p.y() == node.p.y()) {
				N--;
				return node;
			} else if (orientation.equals(HORIZONTAL) && p.x() == node.p.x()) {
				N--;
				return node;
			} else {
				node.rt = insert(node.rt, p, orientation.equals(VERTICAL) ? HORIZONTAL : VERTICAL);
				node.rt.rect = orientation.equals(VERTICAL)
						? new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax())
						: new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
			}
		}

		return node;
	}

	// Does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException("argument to contains() is null");
		// return get(p) != null;
		return get(root, p, VERTICAL);
	}

	private boolean get(Node node, Point2D p, String orientation) {
		if (node == null) {
			return false;
		}
		double cmp;
		if (orientation.equals(VERTICAL)) {
			cmp = p.x() - node.p.x();
		} else {
			cmp = p.y() - node.p.y();
		}
		if (cmp < 0.0) {
			return get(node.lb, p, orientation.equals(VERTICAL) ? HORIZONTAL : VERTICAL);
		} else if (cmp > 0.0) {
			return get(node.rt, p, orientation.equals(VERTICAL) ? HORIZONTAL : VERTICAL);
		} else {
			if (orientation.equals(VERTICAL) && p.y() == node.p.y()) {
				return true;
			} else if (orientation.equals(HORIZONTAL) && p.x() == node.p.x()) {
				return true;
			} else {
				return get(node.rt, p, orientation.equals(VERTICAL) ? HORIZONTAL : VERTICAL);
			}
		}
	}

	// draw all points to standard draw
	public void draw() {
		// Queue<Point2D> keys = new Queue<Point2D>();
		Queue<Node> queue = new Queue<Node>();
		queue.enqueue(root);
		while (!queue.isEmpty()) {
			Node x = queue.dequeue();
			if (x == null)
				continue;
			x.p.draw();
			//x.rect.draw();
			// keys.enqueue(point);
			queue.enqueue(x.lb);
			queue.enqueue(x.rt);
		}
	}

	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new IllegalArgumentException("argument to range() is null");
		Queue<Point2D> points = new Queue<>();
		if (root != null && root.rect.intersects(rect)) {
			Queue<Node> queue = new Queue<Node>();
			queue.enqueue(root);
			while (!queue.isEmpty()) {
				Node x = queue.dequeue();
				if (x == null)
					continue;
				if (rect.contains(x.p)) {
					points.enqueue(x.p);
				}
				if (x.lb != null && x.lb.rect.intersects(rect)) {
					queue.enqueue(x.lb);
				}
				if (x.rt != null && x.rt.rect.intersects(rect)) {
					queue.enqueue(x.rt);
				}
			}
		}
		return points;
	}

	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException("argument to nearest() is null");

		Point2D currentChampion = null;
		double bestNearestDistance = Double.NaN;
		if (root != null) {
			Queue<Node> queue = new Queue<Node>();
			queue.enqueue(root);
			while (!queue.isEmpty()) {
				Node x = queue.dequeue();
				if (x == null)
					continue;
				double distance = x.p.distanceSquaredTo(p);
				if (Double.isNaN(bestNearestDistance) || Double.compare(distance, bestNearestDistance) == -1) {
					bestNearestDistance = distance;
					currentChampion = x.p;
				}
				if (Double.compare(x.rect.distanceSquaredTo(p), bestNearestDistance) == -1) {
					if (x.lb != null && x.lb.rect.contains(p)) {
						queue.enqueue(x.lb);
						queue.enqueue(x.rt);
					} else {
						queue.enqueue(x.rt);
						queue.enqueue(x.lb);
					}
				}
			}
		}
		return currentChampion;
	}

	public static void main(String[] args) {

		KdTree kdtree = new KdTree();
		kdtree.insert(new Point2D(0.7, 0.2));
		kdtree.insert(new Point2D(0.5, 0.4));
		kdtree.insert(new Point2D(0.2, 0.3));
		kdtree.insert(new Point2D(0.4, 0.7));
		kdtree.insert(new Point2D(0.9, 0.6));
		kdtree.draw();
	}
}
