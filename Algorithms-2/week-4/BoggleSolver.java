import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

	private final TreeSet<String> dictionary;
	private final int[] rules;
	private Bag<Integer>[] adj;
	private List<String> words;
	private int[][] index;
	private int N;

	public BoggleSolver(String[] dictionary) {
		this.dictionary = new TreeSet<>();
		this.dictionary.addAll(Arrays.asList(dictionary));
		this.rules = new int[] { 0, 0, 0, 1, 1, 2, 3, 5, 11 };
		this.words = new ArrayList<>();
	}

	public Iterable<String> getAllValidWords(BoggleBoard board) {
		createValidWords(board);
		return this.words;
	}

	public int scoreOf(String word) {
		if (!isValidWord(word)) {
			return 0;
		}
		int length = word.length();
		if (length > 8) {
			return 11;
		}
		return this.rules[length];

	}

	// Get a graph using adjacency list from board
	@SuppressWarnings("unchecked")
	private Set<String> createValidWords(BoggleBoard board) {
		int n = board.rows();
		int m = board.cols();
		N = n * m;

		Set<String> words = new HashSet<>();
		adj = (Bag<Integer>[]) new Bag[N];
		int counter = 0;
		index = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				index[i][j] = counter++;
				adj[index[i][j]] = createAdjacenyList(board, i, j, n, m);
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				dfs(board, index[i][j], i, j, n, m);
			}
		}
		return words;
	}

	private int getIndex(int i, int j, int n) {
		return n * i + j;
	}

	private char getCharAtIndex(BoggleBoard board, int index, int n) {
		int i = index / n;
		int j = index % n;
		return board.getLetter(i, j);
	}

	private Bag<Integer> createAdjacenyList(BoggleBoard board, int i, int j, int n, int m) {
		Bag<Integer> bag = new Bag<>();
		if (i - 1 >= 0) {
			if (j - 1 >= 0) {
				bag.add(getIndex(i - 1, j - 1, n));
			}
			// j
			bag.add(getIndex(i - 1, j, n));
			if (j + 1 <= m - 1) {
				bag.add(getIndex(i - 1, j + 1, n));
			}
		}

		if (i + 1 <= n - 1) {
			if (j - 1 >= 0) {
				bag.add(getIndex(i + 1, j - 1, n));
			}
			// j
			bag.add(getIndex(i + 1, j, n));
			if (j + 1 <= m - 1) {
				bag.add(getIndex(i + 1, j + 1, n));
			}
		}

		if (j - 1 >= 0) {
			bag.add(getIndex(i, j - 1, n));
		}

		if (j + 1 <= m - 1) {
			bag.add(getIndex(i, j + 1, n));
		}

		return bag;
	}

	private void dfs(BoggleBoard board, int idx, int i, int j, int n, int m) {
		boolean[] localMarked = new boolean[n * m];		
		StringBuilder word = new StringBuilder();
		char letter = getCharAtIndex(board, idx, n);
		word.append(letter);
		if (letter == 'Q') {
			word.append('U');
		}		
		dfs(board, n, idx, localMarked, word);		
	}

	private void dfs(BoggleBoard board, int n, int v, boolean[] localMarked, StringBuilder word) {
		localMarked[v] = true;
		for (int w : adj[v]) {
			if (!localMarked[w]) {
				char letter = getCharAtIndex(board, w, n);
				word.append(letter);
				if (letter == 'Q') {
					word.append('U');
				}
				if (word.length() >= 3 && this.dictionary.contains(word.toString())
						&& !this.words.contains(word.toString())) {
					this.words.add(word.toString());
				}
				dfs(board, n, w, localMarked, word);
			}
		}
		//This is the most important section for graph traversal 
		localMarked[v] = false;
		char letter = getCharAtIndex(board, v, n);
		if (letter == 'Q' && word.length() > 1) {
			word.setLength(word.length() - 2);
		} else if (word.length() > 0) {
			word.setLength(word.length() - 1);
		}
	}

	private boolean isValidWord(String word) {
		return word.length() >= 3;		
	}

	public static void main(String[] args) {
		In in = new In("dictionary-algs4.txt");
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("board-q.txt");

		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);

	}

}
