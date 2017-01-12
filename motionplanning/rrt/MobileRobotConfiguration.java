package motionplanning.rrt;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import motionplanning.Configuration;
import motionplanning.Constants;

public class MobileRobotConfiguration implements Configuration{
	private static final int[][] actions = {{1, 0}, {-1, 0}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

	double cost;
	double x;
	double y;
	double theta;
	
	List<Shape> shapes = new ArrayList<Shape>();
	
	List<Configuration> neighbours = new ArrayList<Configuration>();
	
	public MobileRobotConfiguration(double cost, double x, double y, double theta) {
		this.cost = cost;
		this.x = x;
		this.y = y;
		this.theta = theta;
		
		AffineTransform af = new AffineTransform();
		af.rotate(-theta, x, y);
		shapes.add(af.createTransformedShape(
				new Rectangle(getX() - Constants.CAR_LENGTH / 2, getY() - Constants.CAR_WIDTH / 2, Constants.CAR_LENGTH, Constants.CAR_WIDTH)));
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}

	@Override
	public double getCost() {
		return cost;
	}

	@Override
	public double getDistance(Configuration other) {
		MobileRobotConfiguration otherConfig = (MobileRobotConfiguration) other;
		return Math.sqrt(Math.pow(this.x - otherConfig.x, 2) + Math.pow(this.y - otherConfig.y, 2)) + Math.abs(this.theta - otherConfig.theta);
	}
	
	@Override
	public List<Shape> getShapes() {
		return shapes;
	}

	@Override
	public List<Configuration> getSuccessors() {
		return neighbours;
	}
	
	@Override
	public boolean equals(Object other) {
		MobileRobotConfiguration otherConfig = (MobileRobotConfiguration) other;
		return this.x == otherConfig.x && this.y == otherConfig.y && this.theta == otherConfig.theta;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(new double[]{x, y, theta});
	}
	
	protected MobileRobotConfiguration getIncrementalConfiguration(MobileRobotConfiguration randomConfig) {
		MobileRobotConfiguration minConfig = null;
		for (int[] action : actions) {
			minConfig = randomConfig.minConfig(minConfig, takeAction(action));
		}
		return minConfig;
	}
	
	protected MobileRobotConfiguration minConfig(MobileRobotConfiguration config, MobileRobotConfiguration anotherConfig) {
		return config != null && config.getDistance(this) <= anotherConfig.getDistance(this) ? config : anotherConfig;
	}
	
	private MobileRobotConfiguration takeAction(int[] action) {
		double newX = x;
		double newY = y;
		double newTheta = theta;
		
		if (action[1] == 0) {
			newX += Constants.DT * Math.cos(theta) * action[0];
			newY -= Constants.DT * Math.sin(theta) * action[0];
		} else {
			newTheta += Math.toRadians(Constants.DT * action[1]);
			newX -= action[1] * Math.abs(action[0] / action[1]) * (Math.sin(theta) - Math.sin(newTheta));
			newY -= action[1] * Math.abs(action[0] / action[1]) * (Math.cos(theta) - Math.cos(newTheta));
		}
		
		return new MobileRobotConfiguration(cost + 1, newX, newY, newTheta);
	}
}
