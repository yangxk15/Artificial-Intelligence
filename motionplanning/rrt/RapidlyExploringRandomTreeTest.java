package motionplanning.rrt;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import motionplanning.Drawer;
import motionplanning.MotionPlanningProblem;
import motionplanning.Obstacles;

public class RapidlyExploringRandomTreeTest {
	public static void main(String[] args) {
		
		MobileRobotConfiguration startConfig = new MobileRobotConfiguration(0, 50, 50, 0);
		MobileRobotConfiguration goalConfig = new MobileRobotConfiguration(Integer.MAX_VALUE, 550, 550, 0);

		List<Shape> customizedObstacles = new ArrayList<Shape>();

		/* Example 1 */
		customizedObstacles.add(new Rectangle(0, 150, 480, 10));
		customizedObstacles.add(new Rectangle(120, 300, 480, 10));
		customizedObstacles.add(new Rectangle(0, 450, 480, 10));
		int sample = 1000;
		
		/* Example 2 */
//		customizedObstacles.add(new Rectangle(185, 185, 30, 30));
//		customizedObstacles.add(new Rectangle(185, 385, 30, 30));
//		customizedObstacles.add(new Rectangle(385, 185, 30, 30));
//		customizedObstacles.add(new Rectangle(385, 385, 30, 30));
//		int sample = 1000;
		
		/* Example 3 with a larger sample nubmer, takes about 3.5 minutes */
//		customizedObstacles.add(new Rectangle(0, 150, 480, 10));
//		customizedObstacles.add(new Rectangle(120, 300, 480, 10));
//		customizedObstacles.add(new Rectangle(0, 450, 480, 10));
//		int sample = 10000;

		/* Example 4 with a larger sample nubmer, takes about 2.9 minutes */
//		customizedObstacles.add(new Rectangle(185, 185, 30, 30));
//		customizedObstacles.add(new Rectangle(185, 385, 30, 30));
//		customizedObstacles.add(new Rectangle(385, 185, 30, 30));
//		customizedObstacles.add(new Rectangle(385, 385, 30, 30));
//		int sample = 10000;
		
		Obstacles.generateCustomizedObstacles(customizedObstacles);

		MotionPlanningProblem problem = new RapidlyExploringRandomTree(startConfig, goalConfig, sample);

		new Drawer(problem).draw();
	}
}
