package hmm;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Distribution {
	private static final double precision = 0.88;
	
	private static final Format format = new DecimalFormat("0.000000");
	
	private static Map<String, int[]> dirs = new HashMap<>();
	
	private final int m;
	private final int n;
	private final int k;
	private final char[][] maze;
	
	private int[] location;

	private double[][] probability;
	
	static {
		dirs.put("North", new int[]{-1, 0});
		dirs.put("South", new int[]{1, 0});
		dirs.put("West", new int[]{0, -1});
		dirs.put("East", new int[]{0, 1});
	}
	
	public Distribution(char[][] maze, int[] location) {
		this.maze = maze;
		this.m = maze.length;
		this.n = maze[0].length;
		this.location = location;
		
		this.probability = new double[m][n];
		
		int w = 0;
		HashSet<Character> colors = new HashSet<>();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (maze[i][j] == 'W') {
					w++;
				} else {
					colors.add(maze[i][j]);
				}
			}
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (maze[i][j] != 'W') {
					probability[i][j] = 1.0 / (m * n - w);
				}
			}
		}
		
		this.k = colors.size();

		display(' ');
		System.out.println("The robot is initialized at the location (" + location[0] + ", " + location[1] + ")");
		filter(maze[location[0]][location[1]]);
	}
	
	/**
	 * @author Xiankai Yang
	 * 
	 * Public method which applies a serious of directions to the robot
	 * and filter the distribution at the same time.
	 * 
	 * @param directions
	 */
	public void apply(String... directions) {
		for (String direction : directions) {
			System.out.print("The robot moved from " + "(" + location[0] + ", " + location[1] + ")");
			move(location, dirs.get(direction));
			System.out.println(" to " + "(" + location[0] + ", " + location[1] + ")");
			filter(maze[location[0]][location[1]]);
		}
	}

	/**
	 * @author Xiankai Yang
	 * 
	 * Filtering algorithm consists of 3 steps:
	 * 		1. Predict
	 * 		2. Update
	 * 		3. Normalize
	 * 
	 * @param color
	 */
	private void filter(char color) {
		double[][] oldProbability = probability;
		probability = new double[m][n];

		// Predict
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int[] dir : dirs.values()) {
					int[] newLocation = new int[]{i, j};
					move(newLocation, dir);
					probability[newLocation[0]][newLocation[1]] += oldProbability[i][j] / dirs.size();
				}
			}
		}

		// Update
		double s = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				s += probability[i][j] *= (maze[i][j] == color ? precision : ((1 - precision) / (k - 1)));
			}
		}

		// Normalize
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				probability[i][j] /= s;
			}
		}
		
		display(color);
	}
	
	/**
	 * @author Xiankai Yang
	 * 
	 * Display the current probability distribution
	 * and the actual maze with the robot.
	 * 
	 * @param color
	 */
	private void display(char color) {
		System.out.println("********** " + (color == ' ' ? "Initial Distribution" : "Sensed Color: " + color) + " **********");
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(format.format(probability[i][j]) + " ");
			}
			System.out.print("\t\t\t\t\t\t\t\t\t");
			for (int j = 0; j < n; j++) {
				System.out.print((i == location[0] && j == location[1] && color != ' ' ? "@" : maze[i][j]) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * @author Xiankai Yang
	 * 
	 * Update the location p with direction dir.
	 * Stay put if it walks out of the maze or runs into a wall.
	 * 
	 * @param p
	 * @param dir
	 */
	private void move(int[] p, int[] dir) {
		p[0] += dir[0];
		p[1] += dir[1];
		if (p[0] < 0 || p[0] >= m || p[1] < 0 || p[1] >= n || maze[p[0]][p[1]] == 'W') {
			p[0] -= dir[0];
			p[1] -= dir[1];
		}
	}
}
