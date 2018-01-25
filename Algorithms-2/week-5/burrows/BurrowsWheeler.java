import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

	// apply Burrows-Wheeler transform, reading from standard input and writing
	// to standard output
	

	public static void transform() {
		String s = BinaryStdIn.readString();
		CircularSuffixArray suffixArray = new CircularSuffixArray(s);
		int length = suffixArray.length();
		int first = -1;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int idx = suffixArray.index(i);
			if (idx == 0) {
				first = i;
				builder.append(s.charAt(length - 1));
			} else {
				builder.append(s.charAt(idx - 1));
			}			
		}
		BinaryStdOut.write(first);
		BinaryStdOut.write(builder.toString());
		BinaryStdOut.close();
	}

	private static String reconstructString(String transformedString, int first) {
		StringBuilder builder = new StringBuilder();
		int N = transformedString.length();
		int R = 256;
		char[] t = transformedString.toCharArray();
		int[] count = new int[R + 1];
		int[] next = new int[N];
		char[] sorted = new char[N];

		// Compute frequency counts.
		for (int i = 0; i < N; i++)
			count[t[i] + 1]++;
		// Transform counts to indices.
		for (int r = 0; r < R; r++)
			count[r + 1] += count[r];
		// Distribute the records.
		for (int i = 0; i < N; i++) {
			next[count[t[i]]] = i;
			sorted[count[t[i]]] = t[i];
			count[t[i]]++;
		}

		/*Queue<Integer> q = new Queue<>();
		q.enqueue(first);
		while (!q.isEmpty()) {
			int i = q.dequeue();
			builder.append(sorted[i]);
			if (next[i] != first) {
				q.enqueue(next[i]);
			}
		}*/
		
		int counter = 0;
		int i = first;
		while(counter < N){
			builder.append(sorted[i]);
			i = next[i];
			counter++;
		}

		return builder.toString();
	}

	// apply Burrows-Wheeler inverse transform, reading from standard input and
	// writing to standard output
	public static void inverseTransform() {		
		int first = BinaryStdIn.readInt();
		String transformedString = BinaryStdIn.readString();// "ARD!RCAAAABB";
		String originalString = reconstructString(transformedString, first);
		BinaryStdOut.write(originalString);
		BinaryStdOut.close();
	}

	public static void main(String[] args) {
		if (args[0].equals("-"))
			transform();
		else if (args[0].equals("+"))
			inverseTransform();
		else
			throw new IllegalArgumentException("Illegal command line argument");
	}

}
