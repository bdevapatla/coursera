import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	private final Digraph G;
	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked1, marked2;
	private int[] edgeTo1, edgeTo2;
	private int[] distTo1, distTo2;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if(G == null){
			throw new java.lang.IllegalArgumentException("Input arguments cannot be NULL");
		}
		
		this.G = new Digraph(G);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {		
		validateVertex(v);
		validateVertex(w);		
		bfs(this.G, v, w);
		int bestDistance = -1;
		for (int s = 0; s < G.V(); s++) {
			if (marked1[s] && marked2[s]) {
				if (bestDistance == -1 || bestDistance > distTo1[s] + distTo2[s])
					bestDistance = distTo1[s] + distTo2[s];
			}
		}
		return bestDistance;
	}

	private void bfs(Digraph G, int s1, int s2) {

		marked1 = new boolean[G.V()];
		edgeTo1 = new int[G.V()];
		distTo1 = new int[G.V()];

		marked2 = new boolean[G.V()];
		edgeTo2 = new int[G.V()];
		distTo2 = new int[G.V()];

		for (int v = 0; v < G.V(); v++) {
			distTo1[v] = distTo2[v] = INFINITY;
		}

		Queue<Integer> q1 = new Queue<>();
		marked1[s1] = true;
		distTo1[s1] = 0;
		q1.enqueue(s1);

		Queue<Integer> q2 = new Queue<>();
		marked2[s2] = true;
		distTo2[s2] = 0;
		q2.enqueue(s2);

		while (!q1.isEmpty()) {
			int v1 = q1.dequeue();
			for (int w : G.adj(v1)) {
				if (!marked1[w]) {
					edgeTo1[w] = v1;
					distTo1[w] = distTo1[v1] + 1;
					marked1[w] = true;
					q1.enqueue(w);
				}
			}
		}

		while (!q2.isEmpty()) {
			int v2 = q2.dequeue();
			for (int w : G.adj(v2)) {
				if (!marked2[w]) {
					edgeTo2[w] = v2;
					distTo2[w] = distTo2[v2] + 1;
					marked2[w] = true;
					q2.enqueue(w);
				}
			}
		}
	}

	private void bfs(Digraph G, Iterable<Integer> s1, Iterable<Integer> s2) {

		marked1 = new boolean[G.V()];
		edgeTo1 = new int[G.V()];
		distTo1 = new int[G.V()];

		marked2 = new boolean[G.V()];
		edgeTo2 = new int[G.V()];
		distTo2 = new int[G.V()];

		for (int v = 0; v < G.V(); v++) {
			distTo1[v] = distTo2[v] = INFINITY;
		}

		Queue<Integer> q1 = new Queue<>();
		for (int s : s1) {
			validateVertex(s);
			marked1[s] = true;
			distTo1[s] = 0;
			q1.enqueue(s);
		}

		Queue<Integer> q2 = new Queue<>();
		for (int s : s2) {
			validateVertex(s);
			marked2[s] = true;
			distTo2[s] = 0;
			q2.enqueue(s);
		}

		while (!q1.isEmpty()) {
			int v1 = q1.dequeue();
			for (int w : G.adj(v1)) {
				if (!marked1[w]) {
					edgeTo1[w] = v1;
					distTo1[w] = distTo1[v1] + 1;
					marked1[w] = true;
					q1.enqueue(w);
				}
			}
		}

		while (!q2.isEmpty()) {
			int v2 = q2.dequeue();
			for (int w : G.adj(v2)) {
				if (!marked2[w]) {
					edgeTo2[w] = v2;
					distTo2[w] = distTo2[v2] + 1;
					marked2[w] = true;
					q2.enqueue(w);
				}
			}
		}
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		
		validateVertex(v);
		validateVertex(w);	
		
		bfs(this.G, v, w);
		int bestDistance = -1, bestAncestor = -1;
		for (int s = 0; s < G.V(); s++) {
			if (marked1[s] && marked2[s]) {
				if (bestDistance == -1 || bestDistance > distTo1[s] + distTo2[s]) {
					bestDistance = distTo1[s] + distTo2[s];
					bestAncestor = s;
				}
			}
		}
		return bestAncestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		
		if(v == null || w == null){
			throw new java.lang.IllegalArgumentException("Input arguments cannot be NULL");
		}
		
		bfs(this.G, v, w);
		int bestDistance = -1;
		for (int s = 0; s < G.V(); s++) {
			if (marked1[s] && marked2[s]) {
				if (bestDistance == -1 || bestDistance > distTo1[s] + distTo2[s])
					bestDistance = distTo1[s] + distTo2[s];
			}
		}
		return bestDistance;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		
		if(v == null || w == null){
			throw new java.lang.IllegalArgumentException("Input arguments cannot be NULL");
		}
		
		bfs(this.G, v, w);
		int bestDistance = -1, bestAncestor = -1;
		for (int s = 0; s < G.V(); s++) {
			if (marked1[s] && marked2[s]) {
				if (bestDistance == -1 || bestDistance > distTo1[s] + distTo2[s]) {
					bestDistance = distTo1[s] + distTo2[s];
					bestAncestor = s;
				}
			}
		}
		return bestAncestor;
	}
	
	private void validateVertex(int v) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (G.V()-1));
    }

	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}

}
