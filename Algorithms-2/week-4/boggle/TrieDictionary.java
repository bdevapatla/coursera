import java.util.HashSet;

import java.util.Set;

import edu.princeton.cs.algs4.Queue;

public class TrieDictionary  {
	private static final int R = 26; // only alphabets

	private Node root; // root of trie
	private int n; // number of keys in trie

	// R-way trie node
	private static class Node {
		private Node[] next = new Node[R];
		private boolean isString;
	}

	public TrieDictionary() {
	}

	public boolean contains(String key) {
		if (key == null)
			throw new IllegalArgumentException("argument to contains() is null");
		Node x = get(root, key, 0);
		if (x == null)
			return false;
		return x.isString;
	}

	private Node get(Node x, String key, int d) {
		if (x == null)
			return null;
		if (d == key.length())
			return x;
		char c = key.charAt(d);
		return get(x.next[c - 65], key, d + 1);
	}

	public void add(String key) {
		if (key == null)
			throw new IllegalArgumentException("argument to add() is null");
		root = add(root, key, 0);
	}

	private Node add(Node x, String key, int d) {
		if (x == null)
			x = new Node();
		if (d == key.length()) {
			if (!x.isString)
				n++;
			x.isString = true;
		} else {
			char c = key.charAt(d);
			// System.out.println(c-65);
			x.next[c - 65] = add(x.next[c - 65], key, d + 1);
		}
		return x;
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	

	public boolean keysWithPrefix(String prefix) {
		Set<String> results = new HashSet<String>();
		Node x = get(root, prefix, 0);
		collect(x, new StringBuilder(prefix), results);
		return results.size() > 0;
	}

	private void collect(Node x, StringBuilder prefix, Set<String> results) {
		if (x == null)
			return;
		if (x.isString){
			results.add(prefix.toString());
			return;
		}
		for (char c = 65; c < (R + 65); c++) {
			prefix.append(c);
			collect(x.next[c - 65], prefix, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

	public Iterable<String> keysThatMatch(String pattern) {
		Queue<String> results = new Queue<String>();
		StringBuilder prefix = new StringBuilder();
		collect(root, prefix, pattern, results);
		return results;
	}

	private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
		if (x == null)
			return;
		int d = prefix.length();
		if (d == pattern.length() && x.isString)
			results.enqueue(prefix.toString());
		if (d == pattern.length())
			return;
		char c = pattern.charAt(d);
		if (c == '.') {
			for (char ch = 0; ch < R; ch++) {
				prefix.append(ch);
				collect(x.next[ch - 65], prefix, pattern, results);
				prefix.deleteCharAt(prefix.length() - 1);
			}
		} else {
			prefix.append(c);
			collect(x.next[c - 65], prefix, pattern, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

}
