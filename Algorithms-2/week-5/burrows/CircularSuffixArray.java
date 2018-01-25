
public class CircularSuffixArray {
	private int[] index;
	private final int N;
	private final char[] input;

	public CircularSuffixArray(String s) {
		if (s == null)
			throw new java.lang.IllegalArgumentException("Invalid input S");
		this.N = s.length();
		this.input = s.toCharArray();
		this.index = new int[N];
		for (int i = 0; i < N; i++) {
			this.index[i] = i;
		}
		sort(0, N - 1, 0);
	}

	private int charAt(int i, int d) {
		if (i + d < N) {
			return this.input[i + d];
		} else if (i + d == N) {
			return this.input[0];
		} else if(i+d-N < N) {
			return this.input[i+d-N];
		}
		else {
			return -1;
		}
	}
	
	//This is standard 3-way quicksort method 
	private void sort(int lo, int hi, int d) {
		if (hi <= lo) {
			return;
		}
		int lt = lo, gt = hi;
		int v = charAt(index[lo], d);
		int i = lo + 1;
		while (i <= gt) {
			int t = charAt(index[i], d);
			if (t < v)
				exch(lt++, i++);
			else if (t > v)
				exch(i, gt--);
			else
				i++;
		}

		sort(lo, lt - 1, d);
		if (v >= 0)
			sort(lt, gt, d + 1);
		sort(gt + 1, hi, d);
	}

	private void exch(int i, int j) {
		int temp = index[i];
		index[i] = index[j];
		index[j] = temp;
	}

	public int length() {
		return N;
	}

	public int index(int i) {
		if (i < 0 || i >= N)
			throw new java.lang.IllegalArgumentException("Invalid input S");
		return this.index[i];
	}

	public static void main(String[] args) {
		
	}
}
