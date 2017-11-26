import edu.princeton.cs.algs4.Stack;

public class Board {

	private int[][] tiles;
	private final int n;
	private int emptyX;

	/*
	 * construct a board from a n-by-n array of blocks blocks[i][j] , row = i
	 * and column = j
	 */
	public Board(int[][] blocks) {
		n = blocks.length;
		this.tiles = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (blocks[i][j] == 0) {
					emptyX = i;
				}
				this.tiles[i][j] = blocks[i][j];
			}
		}
	}

	/*
	 * board dimension n
	 */
	public int dimension() {
		return n;
	}

	/*
	 * number of blocks out of place
	 */

	private int getGoalValue(int i, int j) {
		if (i == n - 1 && j == n - 1) {
			return 0;
		} else {
			return i * n + j + 1;
		}
	}

	public int hamming() {
		int priority = 0;
		if (!isGoal()) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (this.tiles[i][j] != 0 && this.tiles[i][j] != getGoalValue(i, j)) {
						priority++;
					}
				}
			}
		}

		return priority;
	}

	/*
	 * sum of Manhattan distances between blocks and goal The baseline
	 * understanding is that each element's target location can be calculated
	 * using its value. Once the target location is calculated then summation of
	 * absolute diff in coordinates is the distance the Manhattan uses for
	 * calculating priority
	 */

	public int manhattan() {
		int priority = 0;
		int targeti, targetj;
		if (!isGoal()) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					int element = tiles[i][j];
					if (element != 0 && element != getGoalValue(i, j)) {
						targeti = (element - 1) / n;
						targetj = (element - 1) % n;
						priority += Math.abs(i - targeti) + Math.abs(j - targetj);

					}
				}
			}
		}
		return priority;
	}

	public boolean isGoal() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (this.tiles[i][j] != getGoalValue(i, j)) {
					return false;
				}
			}
		}
		return true;
	}

	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
		Board twin = new Board(this.tiles);

		// find the location of blank square
		// Do not swap with blank square
		// Swap any other location
		int temp, newX;

		if (emptyX - 1 >= 0) {
			newX = emptyX - 1;
		} else {
			newX = emptyX + 1;
		}

		temp = twin.tiles[newX][0];
		twin.tiles[newX][0] = twin.tiles[newX][1];
		twin.tiles[newX][1] = temp;
		return twin;
	}

	public boolean equals(Object y) {
		if (y == null) {
			return false;
		}

		if (!(y.getClass() == this.getClass())) {
			return false;
		}
		int[][] x = ((Board) y).tiles;

		if (this.tiles == x) {
			return true;
		}
		if (this.tiles == null || x == null) {
			return false;
		}
		if (this.tiles.length != x.length) {
			return false;
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (this.tiles[i][j] != x[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	private Board createNeighbor(int x1, int y1, int x2, int y2, int[][] blocks) {
		Board neighbor = new Board(blocks);
		int temp = neighbor.tiles[x1][y1];
		neighbor.tiles[x1][y1] = neighbor.tiles[x2][y2];
		neighbor.tiles[x2][y2] = temp;
		return neighbor;
	}

	public Iterable<Board> neighbors() {
		Stack<Board> neighborList = new Stack<Board>();
		int x = 0, y = 0;
		nestedLoop: for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (this.tiles[i][j] == 0) {
					// This is the location of the empty tile
					x = i;
					y = j;
					break nestedLoop;
				}
			}
		}

		if (x - 1 >= 0) {
			Board neighbor = createNeighbor(x, y, x - 1, y, this.tiles);
			if (neighbor != null) {
				neighborList.push(neighbor);
			}
		}
		if (y - 1 >= 0) {
			Board neighbor = createNeighbor(x, y, x, y - 1, this.tiles);
			if (neighbor != null) {
				neighborList.push(neighbor);
			}
		}
		if (x + 1 < n) {
			Board neighbor = createNeighbor(x, y, x + 1, y, this.tiles);
			if (neighbor != null) {
				neighborList.push(neighbor);
			}
		}
		if (y + 1 < n) {
			Board neighbor = createNeighbor(x, y, x, y + 1, this.tiles);
			if (neighbor != null) {
				neighborList.push(neighbor);
			}
		}

		return neighborList;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(n + "\n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s.append(String.format("%2d ", tiles[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

	public static void main(String[] args) {

	}

}
