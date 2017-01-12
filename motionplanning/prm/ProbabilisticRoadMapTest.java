package motionplanning.prm;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import motionplanning.Drawer;
import motionplanning.MotionPlanningProblem;
import motionplanning.Obstacles;

public class ProbabilisticRoadMapTest {
	public static void main(String[] args) {
		List<Shape> customizedObstacles = new ArrayList<Shape>();

		/* Please uncomment out any of the following example to run*/
		
		/* 2 Arm Example */
		RobotArmConfiguration startConfig = new RobotArmConfiguration(0, 0, 0);
		RobotArmConfiguration goalConfig = new RobotArmConfiguration(Integer.MAX_VALUE, Math.PI, 0);
		int local_planner = 20;
		int sample_number = 10000;
		
		/* 3 Arm Example */
//		RobotArmConfiguration startConfig = new RobotArmConfiguration(0, 0, 0, 0);
//		RobotArmConfiguration goalConfig = new RobotArmConfiguration(Integer.MAX_VALUE, Math.PI, 0, 0);
//		int local_planner = 50;
//		int sample_number = 10000;

		/* 4 Arm Example longer time about 7s */
//		RobotArmConfiguration startConfig = new RobotArmConfiguration(0, 0, 0, 0, 0);
//		RobotArmConfiguration goalConfig = new RobotArmConfiguration(Integer.MAX_VALUE, Math.PI, 0, 0, 0);
//		int local_planner = 70;
//		int sample_number = 50000;
		
		/* Another 4 Arm Example, longer time about 10s */
//		RobotArmConfiguration startConfig = new RobotArmConfiguration(0, Math.PI * 1.5, 0, Math.PI * 1.5, 0);
//		RobotArmConfiguration goalConfig = new RobotArmConfiguration(Integer.MAX_VALUE, 0, 0, Math.PI * 0.5, 0);
//		int local_planner = 70;
//		int sample_number = 50000;
		

		customizedObstacles.add(new Rectangle(185, 185, 30, 30));
		customizedObstacles.add(new Rectangle(185, 385, 30, 30));
		customizedObstacles.add(new Rectangle(385, 185, 30, 30));
		customizedObstacles.add(new Rectangle(385, 385, 30, 30));
		Obstacles.generateCustomizedObstacles(customizedObstacles);
		
		MotionPlanningProblem problem = new ProbabilisticRoadMap(startConfig, goalConfig, local_planner, sample_number);
		
		new Drawer(problem).draw();
	}
}
