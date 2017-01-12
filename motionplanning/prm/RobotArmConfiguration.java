package motionplanning.prm;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import motionplanning.Configuration;
import motionplanning.Constants;
import motionplanning.Obstacles;

public class RobotArmConfiguration implements Configuration {
	
	double cost;
	
	final List<Double> angles = new ArrayList<Double>();
	final List<Point> points = new ArrayList<Point>();
	final List<Shape> shapes = new ArrayList<Shape>();
	
	public RobotArmConfiguration(double cost, double... angles) {
		this.cost = cost;

		double theta = 0;
		double x = Constants.DISPLAY_WIDTH / 2;
		double y = Constants.DISPLAY_HEIGHT / 2;
		
		for (double angle : angles) {
			this.angles.add(angle);
			theta += angle;
			double dx = Constants.ARM_LENGTH * Math.cos(theta);
			double dy = -Constants.ARM_LENGTH * Math.sin(theta);
			this.points.add(new Point((int) (x + dx), (int) (y + dy)));
			this.shapes.add(new Line2D.Double(x, y, x + dx, y + dy));
			x += dx;
			y += dy;
		}
	}
	
	public boolean within(RobotArmConfiguration anotherConfig) {
		return getDistance(anotherConfig) <= ProbabilisticRoadMap.local_planner;
	}
	
	@Override
	public double getCost() {
		return cost;
	}
	
	@Override
	public double getDistance(Configuration other) {
		List<Point> anotherPoints = ((RobotArmConfiguration) other).points;
		double diff = 0;
		
		for (int i = 0; i < angles.size(); i++) {
			diff += points.get(i).distance(anotherPoints.get(i));
		}
		
		return diff;
	}
	
	@Override
	public List<Shape> getShapes() {
		return shapes;
	}

	@Override
	public List<Configuration> getSuccessors() {
		TreeSet<RobotArmConfiguration> neighbours = new TreeSet<>((a, b) -> a.getDistance(this) > b.getDistance(this) ? 1 : -1);

		for (RobotArmConfiguration configuration : ProbabilisticRoadMap.configurations) {
			if (this.within(configuration)) {
				// Create intermediate configuration to make up for sparse sampling
				double[] middleAngles = new double[configuration.angles.size()];
				
				for (int i = 0; i < configuration.angles.size(); i++) {
					middleAngles[i] = (configuration.angles.get(i) + this.angles.get(i)) / 2;
				}
				
				if (Obstacles.hasCollision(new RobotArmConfiguration(-1, middleAngles))) {
					continue;
				}
				
				if (neighbours.size() == Constants.NEAREST_K && configuration.getDistance(this) < neighbours.last().getDistance(this)) {
					neighbours.remove(neighbours.last());
				}
				
				configuration.cost = this.cost + 1;
				neighbours.add(configuration);
			}
		}
		
		return new ArrayList<Configuration>(neighbours);
	}

	@Override
	public boolean equals(Object other) {
		return this.angles.equals(((RobotArmConfiguration) other).angles);
	}
	
	@Override
	public int hashCode() {
		return this.angles.hashCode();
	}
	
	@Override
	public String toString() {
		return String.valueOf(angles.get(0));
	}
}
