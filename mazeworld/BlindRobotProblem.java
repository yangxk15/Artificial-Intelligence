package mazeworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlindRobotProblem extends SearchProblem {
	int[][] actions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
	
	public BlindRobotProblem(char[][] maze, int[] actual, int[] goal) {
		super(maze);
		this.startNode = new BlindRobotNode(0, actual, getDefaultBelief(maze));
		this.goalNode = new BlindRobotNode(Integer.MAX_VALUE, actual, getGoalBelief(maze, goal));
	}

	private class BlindRobotNode implements SearchNode {
		int cost;
		int[] actual;
		boolean[][] state;
		
		public BlindRobotNode(int cost, int[] actual, boolean[][] belief) {
			this.cost = cost;
			this.actual = Arrays.copyOf(actual, actual.length);
			this.state = new boolean[belief.length][belief[0].length];
			
			for (int i = 0; i < belief.length; i++) {
				for (int j = 0; j < belief[i].length; j++) {
					this.state[i][j] = belief[i][j];
				}
			}
		}
		
		@Override
		public int getCost() {
			return cost;
		}

		/**
		 * @author Xiankai Yang
		 * 
		 * Calculate the heuristic value of current node by count the number of possible location of the belief state.
		 * 
		 * @return the calculated heuristic value
		 */
		@Override
		public int getHeuristic() {
			int sum = 0;
			
			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state[i].length; j++) {
					sum += state[i][j] ? 1 : 0;
				}
			}
			
			return sum;
		}

		/**
		 * @author Xiankai Yang
		 * 
		 * Get the successors from the current belief set, by applying every action
		 * on every possible position of the blind robot.
		 * 
		 * @return a list of belief from each action
		 */
		@Override
		public List<SearchNode> getSuccessors() {
			Set<SearchNode> successors = new HashSet<SearchNode>();
			
			for (int[] action : actions) {
				int[] tempActual = Arrays.copyOf(actual, actual.length);
				boolean[][] tempState = new boolean[state.length][state[0].length];
				
				for (int i = 0; i < state.length; i++) {
					for (int j = 0; j < state[i].length; j++) {
						if (state[i][j]) {
							int I = i + action[0];
							int J = j + action[1];
							
							if (isLegal(I, J)) {
								tempState[I][J] = true;
							} else {
								tempState[i][j] = true;
							}
						}
					}
				}
				
				if (isLegal(tempActual[0] + action[0], tempActual[1] + action[1])) {
					tempActual[0] += action[0];
					tempActual[1] += action[1];
				}
				
				successors.add(new BlindRobotNode(cost + 1, tempActual, tempState));
			}
			
			// Make sure the successors don't include the current node
			successors.remove(this);
			return new ArrayList<SearchNode>(successors);
		}
		
		/**
		 * @author Xiankai Yang
		 * 
		 * Determine whether the robot can actually move to that position.
		 * 
		 * @param i
		 * @param j
		 * @return a boolean variable indicating the result
		 */
		private boolean isLegal(int i, int j) {
			int m = state.length;
			int n = state[0].length;
			return i >= 0 && i < m && j >= 0 && j < n && maze[i][j] == FLOOR;
		}

		@Override
		public int hashCode() {
			return Arrays.deepHashCode(state);
		}
		
		@Override
		public boolean equals(Object other) {
			return Arrays.deepEquals(state, ((BlindRobotNode) other).state);
		}


		/**
		 * @author Xiankai Yang
		 * 
		 * Visualize current state of the node
		 * @throws InterruptedException 
		 */
		@Override
		public void visualize() throws InterruptedException {
			System.out.println("--------------------------------------------------");
			
			for (int i = 0; i < maze.length; i++) {
				for (int j = 0; j < maze[i].length; j++) {
					System.out.print(i == actual[0] && j == actual[1] ? "A" : maze[i][j]);
				}
				
				System.out.println();
			}
			
			System.out.println("Belief: " + this);
			
			System.out.println("--------------------------------------------------");
			Thread.sleep(200);
		}
		
		@Override
		public String toString() {
			List<String> possiblePositions = new ArrayList<String>();
			
			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state[i].length; j++) {
					if (state[i][j]) {
						possiblePositions.add("[" + i + "," + j + "]");
					}
				}
			}
			
			return String.join(",", possiblePositions);
		}
		
	}
	
	/**
	 * @author Xiankai Yang
	 * @param maze
	 * @return A default belief derived from the maze, which contains everything possible floor area as start location
	 */
	private static boolean[][] getDefaultBelief(char[][] maze) {
		boolean[][] defaultBelief = new boolean[maze.length][maze[0].length];
		
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				defaultBelief[i][j] = maze[i][j] == FLOOR;
			}
		}
		
		return defaultBelief;
	}


	/**
	 * @author Xiankai Yang
	 * @param maze
	 * @param goal
	 * @return A goal belief derived from the maze and goal
	 */
	private boolean[][] getGoalBelief(char[][] maze, int[] goal) {
		boolean[][] goalBelief = new boolean[maze.length][maze[0].length];
		
		goalBelief[goal[0]][goal[1]] = true;
		
		return goalBelief;
	}
}
