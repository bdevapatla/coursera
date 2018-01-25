import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

	private static final int R = 256;
	private static char[] ascii_sequence = new char[R];

	// apply move-to-front encoding, reading from standard input and writing to
	// standard output
	public static void encode() {
		String s = BinaryStdIn.readString();
		for (int i = 0; i < R; i++) {
			ascii_sequence[i] = (char) i;
		}
		char[] input = s.toCharArray();
		int N = input.length;
		for (int i = 0; i < N; i++) {
			moveToFront(input[i]);
		}
		BinaryStdOut.close();
	}

	private static void moveToFront(char c) {
		int index = -1;
		for (int i = 0; i < R; i++) {
			if (ascii_sequence[i] == c) {
				index = i;
				BinaryStdOut.write(i, 8);
				break;
			}
		}

		for (int i = index; i > 0; i--) {
			ascii_sequence[i] = ascii_sequence[i - 1];
		}
		ascii_sequence[0] = c;
	}
	
	private static void moveToFrontDecode(int in) {
		char c = ascii_sequence[in];
		BinaryStdOut.write(c);		
		for (int i = in; i > 0; i--) {
			ascii_sequence[i] = ascii_sequence[i - 1];
		}
		ascii_sequence[0] = c;
	}


	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
	public static void decode() {
		for (int i = 0; i < 256; i++) {
			ascii_sequence[i] = (char) i;
		}
		while(!BinaryStdIn.isEmpty()){
			moveToFrontDecode(BinaryStdIn.readInt(8));
		}
		BinaryStdOut.close();
	}

	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			throw new IllegalArgumentException("Illegal command line argument");
	}

}
