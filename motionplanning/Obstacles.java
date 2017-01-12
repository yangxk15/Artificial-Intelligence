package motionplanning;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import motionplanning.prm.RobotArmConfiguration;


public class Obstacles {
	static Random r = new Random(Constants.OBSTACLE_SEED);
	
	final static List<Shape> obstacles = new ArrayList<Shape>();

	public static void generateRandombstacle(RobotArmConfiguration startConfig, RobotArmConfiguration goalConfig) {
		int width = Constants.OBSTACLE_WIDTH;
		int height = Constants.OBSTACLE_HEIGHT;
		for (int i = 0; i < Constants.DISPLAY_WIDTH; i += width) {
			for (int j = 0; j < Constants.DISPLAY_HEIGHT; j += height) {
				if (r.nextDouble() < Constants.OBSTACLE_DENSITY) {
					Shape obstacle = new Rectangle(i, j, width, height);
					if (!hasCollision(startConfig) && !hasCollision(goalConfig)) {
						obstacles.add(obstacle);
					}
				}
			}
		}
	}
	
	public static void generateCustomizedObstacles(List<Shape> customizedObstacles) {
		for (Shape shape : customizedObstacles) {
			obstacles.add(shape);
		}
	}
	
	public static boolean hasCollision(Configuration config) {
		for (Shape obstacle : obstacles) {
			if (hasCollision(obstacle, config)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean hasCollision(Shape obstacle, Configuration config) {
		for (Shape shape : config.getShapes()) {
			if (shape.intersects((Rectangle2D) obstacle)) {
				return true;
			}
		}
		return false;
	}
}
