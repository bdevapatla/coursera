import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	// constructor takes a WordNet object
	private final WordNet wordNet;

	public Outcast(WordNet wordnet) {
		if(wordnet == null){
			throw new java.lang.IllegalArgumentException("Input arguments cannot be NULL");
		}
		this.wordNet = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		if(nouns == null){
			throw new java.lang.IllegalArgumentException("Input arguments cannot be NULL");
		}
		String outCast = null;
		int maxDistance = -1;
		for (int i = 0; i < nouns.length; i++) {
			int distance = 0;
			for (int j = 0; j < nouns.length; j++) {
				distance += this.wordNet.distance(nouns[i], nouns[j]);
				
			}
			if (maxDistance < distance) {
				maxDistance = distance;
				outCast = nouns[i];
			}
		}
		return outCast;
	}

	// see test client below
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}