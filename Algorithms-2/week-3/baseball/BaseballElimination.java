import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	private final int N;
	private List<String> teams;
	private int[] wins, loss, left;
	private int[][] g;

	// Create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		teams = new ArrayList<>();
		In in = new In(filename);
		String[] lines = in.readAllLines();
		N = Integer.parseInt(lines[0]);
		wins = new int[N];
		loss = new int[N];
		left = new int[N];
		g = new int[N][N];
		for (int i = 0; i < lines.length - 1; i++) {
			String[] lineItems = lines[i + 1].trim().split("\\s+");
			teams.add(lineItems[0]);
			wins[i] = Integer.parseInt(lineItems[1]);
			loss[i] = Integer.parseInt(lineItems[2]);
			left[i] = Integer.parseInt(lineItems[3]);
			for (int j = 4; j < lineItems.length; j++) {
				g[i][j - 4] = Integer.parseInt(lineItems[j]);
			}
		}
	}

	// number of teams
	public int numberOfTeams() {
		return N;
	}

	// all teams
	public Iterable<String> teams() {
		return teams;
	}

	// number of wins for given team
	public int wins(String team) {
		if (team == null)
			throw new java.lang.IllegalArgumentException("Invalid input");
		int index = teams.indexOf(team);
		if (index == -1) {
			throw new java.lang.IllegalArgumentException("Invalid input");
		}
		return wins[index];
	}

	// number of losses for given team
	public int losses(String team) {
		if (team == null)
			throw new java.lang.IllegalArgumentException("Invalid input");
		int index = teams.indexOf(team);
		if (index == -1) {
			throw new java.lang.IllegalArgumentException("Invalid input");
		}
		return loss[index];
	}

	// number of remaining games for given team
	public int remaining(String team) {
		if (team == null)
			throw new java.lang.IllegalArgumentException("Invalid input");
		int index = teams.indexOf(team);
		if (index == -1) {
			throw new java.lang.IllegalArgumentException("Invalid input");
		}
		return left[index];

	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		if (team1 == null || team2 == null)
			throw new java.lang.IllegalArgumentException("Invalid input");
		int index1 = teams.indexOf(team1);
		int index2 = teams.indexOf(team2);

		if (index1 == -1 || index2 == -1) {
			throw new java.lang.IllegalArgumentException("Invalid input");
		}
		return g[index1][index2];
	}

	private Iterable<String> isTriviallyEliminated(String team) {
		int index = teams.indexOf(team);
		int maxPossibleWins = wins[index] + left[index];
		List<String> temp = null;
		for (int i = 0; i < N; i++) {
			if (i != index) {
				if (wins[i] > maxPossibleWins) {
					if (temp == null) {
						temp = new ArrayList<>();
					}
					temp.add(teams.get(i));
					break;
				}
			}
		}
		return temp;
	}
	
	/*private int getTeamIndices(int i, int j, int counter, int N, int index) {
		int vertexI = counter+1, vertexJ = counter + 1;
		
		//index is getting removed
		//g is between ith team jth team 
		
		//before i comes into picture there will be counter+1 definitely
		//how many more will come in between depends on index
		
		return vertex;
	}*/
	
	

	private Iterable<String> isMathematicallyEliminated(String team) {
		int index = teams.indexOf(team);
		int counter = 0;
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				if (i == index || j == index) {
					continue;
				} else if (g[i][j] != 0) {
					counter++;
				}
			}
		}
		int V = counter + N + 1;
		FlowNetwork network = new FlowNetwork(V);
		int c = 0, d = 0;
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				if (i == index || j == index) {
					continue;
				} else if (g[i][j] != 0) {
					network.addEdge(new FlowEdge(0, c + 1, g[i][j]));

					// Construct edges from games to teams

					network.addEdge(new FlowEdge(c + 1, (i > index ? counter + i : counter + i + 1), 
							Double.POSITIVE_INFINITY));
					network.addEdge(new FlowEdge(c + 1, (j > index ? counter + j : counter + j + 1),
							Double.POSITIVE_INFINITY));
					c++;
				}
			}

			if (i != index) {
				network.addEdge(new FlowEdge(counter + 1 + d, network.V() - 1, wins[index] + left[index] - wins[i]));
				d++;
			}

		}

		/*for (FlowEdge edge : network.edges()) {
			System.out.println(edge.from() + "-->" + edge.to() + " capacity: " + edge.capacity());
		}*/

		FordFulkerson fulkerson = new FordFulkerson(network, 0, network.V() - 1); 
		List<String> temp = null;	
		d=0;
		for (int i = 0; i < N; i++) {
			if (i != index) {
				if (fulkerson.inCut(counter + 1 + d)) {
					if (temp == null) {
						temp = new ArrayList<>();
					}
					temp.add(teams.get(i));
				}
				d++;
			}
		}
		return temp;
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		if (team == null)
			throw new java.lang.IllegalArgumentException("Invalid input");
		int index = teams.indexOf(team);
		if (index == -1) {
			throw new java.lang.IllegalArgumentException("Invalid input");
		}
		if (isTriviallyEliminated(team) != null) {
			return true;
		} else if (isMathematicallyEliminated(team) != null) {
			return true;
		}
		return false;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		if (team == null)
			throw new java.lang.IllegalArgumentException("Invalid input");
		int index = teams.indexOf(team);
		if (index == -1) {
			throw new java.lang.IllegalArgumentException("Invalid input");
		}
		Iterable<String> temp = isTriviallyEliminated(team);
		if (temp == null) {
			temp = isMathematicallyEliminated(team);
		}
		return temp;
	}

	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}
