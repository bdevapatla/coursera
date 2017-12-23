import java.util.TreeMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

	private TreeMap<String, Bag<Integer>> synsetTree = new TreeMap<>();
	private TreeMap<Integer, String> synsetFileTree = new TreeMap<>();
	private int numberOfSynsets = 0;
	private Digraph digraph;
	private SAP sap;

	// constructor takes the name of two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new java.lang.IllegalArgumentException("Input arguments cannot be NULL");
		}
		createSysnetsList(synsets);
		createDiagraph(hypernyms);
		Topological topological = new Topological(digraph);
		Iterable<Integer> order = topological.order();		
		boolean rootFound = false;
		if (order == null) {
			throw new java.lang.IllegalArgumentException("input does not correspond to a rooted DAG");
		} else {
			for (int v : order) {			
				if (digraph.outdegree(v) == 0) {
					if (!rootFound) {
						rootFound = true;
					}
					else {
						throw new java.lang.IllegalArgumentException("input does not correspond to a rooted DAG");
					}
				}
			}
		}
		sap = new SAP(digraph);
	}

	private void createSysnetsList(String synsets) {
		In in = new In(synsets);
		String[] fields;
		while (in.hasNextLine()) {
			String line = in.readLine();
			fields = line.split(",");
			Integer id = Integer.parseInt(fields[0]);
			String[] nouns = fields[1].split("\\s+");
			synsetFileTree.put(id, fields[1]);
			for (String noun : nouns) {
				Bag<Integer> ids;
				if (synsetTree.containsKey(noun)) {
					ids = synsetTree.get(noun);
					ids.add(id);
					synsetTree.put(noun, ids);
				} else {
					ids = new Bag<>();
					ids.add(id);
					synsetTree.put(noun, ids);
				}
			}
			numberOfSynsets++;
		}
	}

	private void createDiagraph(String hypernyms) {
		In in = new In(hypernyms);
		String[] fields;
		digraph = new Digraph(numberOfSynsets);
		while (in.hasNextLine()) {
			String line = in.readLine();
			fields = line.split(",");
			for (int i = 1; i < fields.length; i++) {
				digraph.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
			}
		}
	}

	// Return all wordnet nouns
	public Iterable<String> nouns() {
		return synsetTree.keySet();
	}

	// is the word a wordnet noun
	public boolean isNoun(String word) {
		if (word == null) {
			throw new java.lang.IllegalArgumentException("Argument cannot be NULL");
		}
		return synsetTree.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null || nounA.isEmpty() || nounB.isEmpty()) {
			throw new java.lang.IllegalArgumentException("Arguments are not wordnet nouns");
		}

		Iterable<Integer> idA = synsetTree.get(nounA);
		Iterable<Integer> idB = synsetTree.get(nounB);

		if (idA == null || idB == null) {
			throw new java.lang.IllegalArgumentException("Arguments are not wordnet nouns");
		}

		return sap.length(idA, idB);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null || nounA.isEmpty() || nounB.isEmpty()) {
			throw new java.lang.IllegalArgumentException("Arguments are not wordnet nouns");
		}
		Iterable<Integer> idA = synsetTree.get(nounA);
		Iterable<Integer> idB = synsetTree.get(nounB);

		if (idA == null || idB == null) {
			throw new java.lang.IllegalArgumentException("Arguments are not wordnet nouns");
		}

		return synsetFileTree.get(sap.ancestor(idA, idB));
	}

	public static void main(String[] args) {
		WordNet wordNet = new WordNet(args[0], args[1]);
	}

}
