package mazeworld;

import java.util.List;

import mazeworld.SearchProblem.SearchNode;

public class BlindRobotProblemTest {
	public static void main(String[] args) throws InterruptedException {
		/* Test Case 1 */
		/* Traveling from the north west corner to the south east corner */
		test(new char[][]{
			"....".toCharArray(),
			"....".toCharArray(),
			"....".toCharArray(),
			"....".toCharArray(),
			},
			new int []{0, 0},
			new int []{3, 3}
		);
		/* Test Case 2 */
		/* Same test case with Multiple robots test #1 */
		test(new char[][]{
			".....".toCharArray(),
			"####.".toCharArray(),
			".....".toCharArray(),
			".####".toCharArray(),
			".....".toCharArray(),
			},
			new int []{0, 0},
			new int []{4, 4}
		);
		/* Test Case 3 */
		/* 7x7 the robot start and goal is the same, but itself doesn't know */
		test(new char[][]{
			".......".toCharArray(),
			".......".toCharArray(),
			".......".toCharArray(),
			".......".toCharArray(),
			".......".toCharArray(),
			".......".toCharArray(),
			".......".toCharArray(),
			},
			new int []{3, 3},
			new int []{3, 3}
		);
		/* Test Case 4 */
		/* 7x7 the robot is not actually stuck but there are small corners formed by walls */
		test(new char[][]{
			".......".toCharArray(),
			".##....".toCharArray(),
			".#.....".toCharArray(),
			".......".toCharArray(),
			".....#.".toCharArray(),
			"....##.".toCharArray(),
			".......".toCharArray(),
			},
			new int []{2, 2},
			new int []{4, 4}
		);
		System.out.println("The end...");
	}

	private static void test(char[][] maze, int[] actual, int[] goal) throws InterruptedException {
		SearchProblem problem = new BlindRobotProblem(maze, actual, goal);
		List<SearchNode> list = problem.AStarSearch();
		
		if (list == null) {
			System.out.println("No way to do that!!!!!");
		} else {
			for (SearchNode node : list) {
				node.visualize();
			}
		}
		
		for (int i = 3; i >= 1; i--) {
			System.out.println("Next test case coming in " + i + "s");
			Thread.sleep(1000);
		}
	}
}
