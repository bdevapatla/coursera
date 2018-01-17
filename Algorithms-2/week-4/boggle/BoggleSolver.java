import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

	private final TrieDictionary dictionary;
	private final int[] rules;
	private Bag<Integer>[] adj;
	private Set<String> words;
	private int[][] index;
	private char[] letter;
	private int N;
	private HashMap<String, Boolean> prefixNotMap;

	public BoggleSolver(String[] dictionary) {
		this.dictionary = new TrieDictionary();
		for (int i = 0; i < dictionary.length; i++) {
			this.dictionary.add(dictionary[i]);
		}
		this.rules = new int[] { 0, 0, 0, 1, 1, 2, 3, 5, 11 };
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
	private void createValidWords(BoggleBoard board) {
		this.words = new HashSet<>();
		int n = board.rows();
		int m = board.cols();
		N = n * m;
		adj = (Bag<Integer>[]) new Bag[N];
		int counter = 0;
		index = new int[n][m];
		letter = new char[n * m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				index[i][j] = counter;
				letter[counter] = board.getLetter(i, j);
				counter++;
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				adj[index[i][j]] = createAdjacenyList(board, i, j, n, m);
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				dfs(board, index[i][j]);
			}
		}

	}

	private Bag<Integer> createAdjacenyList(BoggleBoard board, int i, int j, int n, int m) {
		Bag<Integer> bag = new Bag<>();
		if (i - 1 >= 0) {
			if (j - 1 >= 0) {
				bag.add(index[i - 1][j - 1]);
			}
			// j
			bag.add(index[i - 1][j]);
			if (j + 1 <= m - 1) {
				bag.add(index[i - 1][j + 1]);
			}
		}

		if (i + 1 <= n - 1) {
			if (j - 1 >= 0) {
				bag.add(index[i + 1][j - 1]);
			}
			// j
			bag.add(index[i + 1][j]);
			if (j + 1 <= m - 1) {
				bag.add(index[i + 1][j + 1]);
			}
		}

		if (j - 1 >= 0) {
			bag.add(index[i][j - 1]);
		}

		if (j + 1 <= m - 1) {
			bag.add(index[i][j + 1]);
		}

		return bag;
	}

	private void dfs(BoggleBoard board, int idx) {
		boolean[] localMarked = new boolean[N];
		StringBuilder word = new StringBuilder();
		char c = letter[idx];
		word.append(c);
		if (c == 'Q') {
			word.append('U');
		}
		dfs(board, idx, localMarked, word);
	}

	private void dfs(BoggleBoard board, int v, boolean[] localMarked, StringBuilder word) {
		localMarked[v] = true;
		
		for (int w : adj[v]) {
			if (!localMarked[w]) {
				char c = letter[w];
				word.append(c);
				if (c == 'Q') {
					word.append('U');
				}


				if (!this.dictionary.keysWithPrefix(word.toString())) {
					
					if (c == 'Q' && word.length() > 1) {
						word.setLength(word.length() - 2);
					} else if (word.length() > 0) {
						word.setLength(word.length() - 1);
					}
					continue;
					
				}

				
				if (word.length() >= 3 && this.dictionary.contains(word.toString())
						&& !words.contains(word.toString())) {
					words.add(word.toString());
				}
				
				dfs(board, w, localMarked, word);
			}
		}
		// This is the most important section for graph traversal
		localMarked[v] = false;
		char c = letter[v];
		if (c == 'Q' && word.length() > 1) {
			word.setLength(word.length() - 2);
		} else if (word.length() > 0) {
			word.setLength(word.length() - 1);
		}
	}

	private boolean isValidWord(String word) {
		return word.length() >= 3;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(args[1]);
		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
	}

}
