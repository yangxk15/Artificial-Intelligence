package mazeworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiRobotProblem extends SearchProblem {
	int[][] actions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
	
	public MultiRobotProblem (char[][] maze, int[][] startCoordinates, int[][] goalCoordinates) {
		super(maze);
		this.startNode = new MultiRobotNode(0, startCoordinates);
		this.goalNode = new MultiRobotNode(Integer.MAX_VALUE, goalCoordinates);
	}
	
	private class MultiRobotNode implements SearchNode {
		int cost;
		int[][] state;
		
		public MultiRobotNode(int cost, int[][] coordinates) {
			this.cost = cost;
			this.state = new int[coordinates.length][2];
			
			for (int i = 0; i < state.length; i++) {
				this.state[i][0] = coordinates[i][0];
				this.state[i][1] = coordinates[i][1];
			}
		}
		
		@Override
		public int getCost() {
			return cost;
		}

		/**
		 * @author Xiankai Yang
		 * 
		 * Calculate the heuristic value based on the sum of Manhattan distance of all robots.
		 * 
		 * @return the calculated heuristic value
		 */
		@Override
		public int getHeuristic() {
			int[][] goalState = ((MultiRobotNode) goalNode).state;
			int sum = 0;
			
			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state[i].length; j++) {
					sum += Math.abs(state[i][j] - goalState[i][j]);
				}
			}
			
			return sum;
		}

		/**
		 * @author Xiankai Yang
		 * 
		 * Get the successors of current combination of all robots coordinates.
		 * 
		 * @return a list of successors derived from current node
		 */
		@Override
		public List<SearchNode> getSuccessors() {
			List<SearchNode> successors = new ArrayList<SearchNode>();
			
			for (int[] robotCoordinate : state) {
				for (int[] action : actions) {
					robotCoordinate[0] += action[0];
					robotCoordinate[1] += action[1];
					
					if (isLegal()) {
						successors.add(new MultiRobotNode(cost + 1, state));
					}
					
					robotCoordinate[0] -= action[0];
					robotCoordinate[1] -= action[1];
				}
			}
			
			return successors;
		}

		/**
		 * @author Xiankai Yang
		 * 
		 * Determine whether the current state is legal or not.
		 * 
		 * @return a boolean variable indicating whether the action can be taken.
		 */
		private boolean isLegal() {
			int m = maze.length;
			int n = maze[0].length;
			
			List<int[]> robotLocation = new ArrayList<int[]>();
			
			for (int[] robotCoordinate : state) {
				for (int[] location : robotLocation) {
					if (Arrays.equals(robotCoordinate, location)) {
						return false;
					}
				}
				
				robotLocation.add(robotCoordinate);
				
				if (robotCoordinate[0] < 0 || robotCoordinate[0] >= m
				 || robotCoordinate[1] < 0 || robotCoordinate[1] >= n
				 || maze[robotCoordinate[0]][robotCoordinate[1]] == WALL) {
					return false;
				}
			}
			
			return true;
		}

		@Override
		public int hashCode() {
			return Arrays.deepHashCode(state);
		}
		
		@Override
		public boolean equals(Object other) {
			return Arrays.deepEquals(this.state, ((MultiRobotNode) other).state);
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
					int robotIndex = getRobotIndex(i, j);
					System.out.print(robotIndex == -1 ? maze[i][j] : (char) ('A' + robotIndex));
				}
				
				System.out.println();
			}

			System.out.println("--------------------------------------------------");
			Thread.sleep(200);
		}
		
		/**
		 * @author Xiankai Yang
		 * 
		 * Get robot index from the input coordinates.
		 * 
		 * @param i
		 * @param j
		 * @return the robot index if matched, -1 otherwise
		 */
		private int getRobotIndex(int i, int j) {
			for (int k = 0; k < state.length; k++) {
				if (state[k][0] == i && state[k][1] == j) {
					return k;
				}
			}
			
			return -1;
		}
	}
}
