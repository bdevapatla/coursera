import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

	private Stack<Board> steps;
	private boolean isSolvable;

	private class SearchNode {
		private Board board;
		private int moves;
		private SearchNode predecessor;
		private int priority;
	}

	private class PriorityComparator<T> implements Comparator<T> {
		@Override
		public int compare(T o1, T o2) {
			SearchNode node1 = (SearchNode) o1;
			SearchNode node2 = (SearchNode) o2;
			return (node1.priority + node1.moves) - (node2.priority + node2.moves);
		}
	}

	/*
	 * find a solution to the initial board (using the A* algorithm)
	 */
	public Solver(Board initial) {
		
		if(initial == null)
			throw new java.lang.IllegalArgumentException("Invalid input");
		
		MinPQ<SearchNode> set1 = new MinPQ<>(new PriorityComparator<SearchNode>());
		MinPQ<SearchNode> set2 = new MinPQ<>(new PriorityComparator<SearchNode>());

		SearchNode node1 = new SearchNode();
		node1.board = initial;
		node1.moves = 0;
		node1.priority = initial.manhattan();
		set1.insert(node1);

		SearchNode node2 = new SearchNode();
		node2.board = initial.twin();
		node2.moves = 0;
		node2.priority = node2.board.manhattan();
		set2.insert(node2);

		for (;;) {
			SearchNode current1 = set1.delMin();
			// steps.enqueue(current1.board);

			if (current1.board.isGoal()) {
				steps = new Stack<>();
				steps.push(current1.board);
				while (current1.predecessor != null) {
					steps.push(current1.predecessor.board);
					current1 = current1.predecessor;
				}
				isSolvable = true;
				break;
			}
			SearchNode current2 = set2.delMin();
			if (current2.board.isGoal()) {
				isSolvable = false;
				break;
			}

			for (Board board : current1.board.neighbors()) {
				if (current1.predecessor == null || !board.equals(current1.predecessor.board)) {
					SearchNode neighbor = new SearchNode();
					neighbor.board = board;
					neighbor.moves = current1.moves + 1;
					neighbor.predecessor = current1;
					neighbor.priority = board.manhattan();
					set1.insert(neighbor);
				}
			}

			for (Board board : current2.board.neighbors()) {
				if (current2.predecessor == null || !board.equals(current2.predecessor.board)) {
					SearchNode neighbor = new SearchNode();
					neighbor.board = board;
					neighbor.moves = current2.moves + 1;
					neighbor.predecessor = current2;
					neighbor.priority = board.manhattan();
					set2.insert(neighbor);
				}
			}
		}
	}

	/*
	 * is the initial board solvable?
	 */
	public boolean isSolvable() {
		return isSolvable;
	}

	/*
	 * min number of moves to solve initial board; -1 if unsolvable
	 */
	public int moves() {
		if (steps != null && steps.size() > 0) {
			return steps.size() - 1;
		} else {
			return -1;
		}
	}

	/*
	 * sequence of boards in a shortest solution; null if unsolvable
	 */
	public Iterable<Board> solution() {
		return steps;
	}

	public static void main(String[] args) {
		// Create initial board from file
		// In in = new In(args[0]);
		// int n = 3;// in.readInt();

		// int[][] blocks = {{1,2,3},{0,7,6},{5,4,8}};
		// int[][] blocks = new int[][] { { 3, 0, 5, 2 }, { 9, 14, 1, 11 }, {
		// 13, 15, 12, 6 }, { 8, 10, 4, 7 } };

		/*
		 * int[][] blocks = new int[][] { { 26, 73, 60, 69, 21, 12, 16, 67, 53
		 * }, { 36, 25, 37, 1, 54, 31, 19, 58, 79 }, { 52, 48, 11, 62, 18, 68,
		 * 38, 34, 27 }, { 8, 35, 33, 23, 40, 61, 17, 39, 15 }, { 78, 46, 56,
		 * 32, 50, 80, 63, 47, 24 }, { 71, 51, 4, 13, 41, 22, 77, 3, 44 }, { 28,
		 * 42, 45, 57, 72, 14, 74, 6, 55 }, { 29, 70, 2, 7, 66, 49, 20, 9, 65 },
		 * { 75, 30, 10, 5, 43, 64, 0, 59, 76 } };
		 */
		/*
		 * for (int i = 0; i < n; i++) { for (int j = 0; j < n; j++) {
		 * blocks[i][j] = in.readInt(); } }
		 */

		/*
		 * 3 0 5 2 9 14 1 11 13 15 12 6 8 10 4 7
		 */
		// create initial board from file

		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();		

		Board initial = new Board(blocks);
		Solver solver = new Solver(initial);

		if (!solver.isSolvable()) {
			System.out.println("No solution possible");
		} else {
			System.out.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution()) {
				System.out.println(board.toString());
			}

		}

	}

}
