package mazeworld;

import java.util.List;

import mazeworld.SearchProblem.SearchNode;

public class MultiRobotProblemTest {
	public static void main(String[] args) throws InterruptedException {
		/* Test Case 1 */
		/* One robot, traveling from the north west corner to the south east corner */
		test(new char[][]{
			".....".toCharArray(),
			"####.".toCharArray(),
			".....".toCharArray(),
			".####".toCharArray(),
			".....".toCharArray(),
			},
			new int [][]{{0, 0}},
			new int [][]{{4, 4}}
		);
		/* Test Case 2 */
		/* Example mentioned in the requirements */
		test(new char[][]{
			".......".toCharArray(),
			".##....".toCharArray(),
			"..##...".toCharArray(),
			"....#..".toCharArray(),
			"..##...".toCharArray(),
			"..#....".toCharArray(),
			"....##.".toCharArray(),
			},
			new int [][]{{6, 0}, {0, 1}, {1, 4}},
			new int [][]{{5, 6}, {4, 6}, {6, 6}}
		);
		/* Test Case 3 */
		/* 2 trapped in the north west closed region and 2 in the south east closed region */
		test(new char[][]{
			"...#......".toCharArray(),
			"...#......".toCharArray(),
			"...#......".toCharArray(),
			"####......".toCharArray(),
			"..........".toCharArray(),
			"..........".toCharArray(),
			"......####".toCharArray(),
			"......#...".toCharArray(),
			"......#...".toCharArray(),
			"......#...".toCharArray(),
			},
			new int [][]{{0, 1}, {1, 0}, {8, 9}, {9, 8}},
			new int [][]{{8, 9}, {9, 8}, {0, 1}, {1, 0}}
		);
		/* Test Case 4 */
		/* 20x20, long corridor with two robots at each end, and one in the middle which doesn't want to move */
		test(new char[][]{
			"....................".toCharArray(),
			"....................".toCharArray(),
			"....................".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"#########.##########".toCharArray(),
			"....................".toCharArray(),
			"....................".toCharArray(),
			},
			new int [][]{{3, 9}, {9, 9}, {17, 9}},
			new int [][]{{17, 9}, {9, 9}, {3, 9}}
		);
		/* Test Case 5 */
		/* 8 puzzle worst case initial state to the goal state */
		test(new char[][]{
			"...".toCharArray(),
			"...".toCharArray(),
			"...".toCharArray(),
			},
			new int [][]{{2, 2}, {2, 0}, {2, 1}, {1, 1}, {1, 0}, {0, 2}, {1, 2}, {0, 0}},
			new int [][]{{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 1}}
		);
		System.out.println("The end...");
	}

	private static void test(char[][] maze, int[][] start, int[][] goal) throws InterruptedException {
		SearchProblem problem = new MultiRobotProblem(maze, start, goal);
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
