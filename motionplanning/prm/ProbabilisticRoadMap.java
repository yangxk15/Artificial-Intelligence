package motionplanning.prm;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import motionplanning.Constants;
import motionplanning.MotionPlanningProblem;
import motionplanning.Obstacles;

public class ProbabilisticRoadMap extends MotionPlanningProblem {
	Random r = new Random(Constants.PRM_SEED);
	
	protected static int local_planner;
	protected static int sample_number;
	
	static Set<RobotArmConfiguration> configurations = new HashSet<RobotArmConfiguration>();
	
	public ProbabilisticRoadMap(RobotArmConfiguration startConfiguration, RobotArmConfiguration goalConfiguration, int local_planner, int sample_number) {
		super(startConfiguration, goalConfiguration);
		
		ProbabilisticRoadMap.local_planner = local_planner;
		ProbabilisticRoadMap.sample_number = sample_number;
		
		ProbabilisticRoadMap.configurations.add(startConfiguration);
		ProbabilisticRoadMap.configurations.add(goalConfiguration);
		
		generateRoadMap();
	}

	private void generateRoadMap() {
		for (int i = 0; i < sample_number; i++) {
			double[] angles = new double[startConfiguration.getShapes().size()];
			
			for (int j = 0; j < angles.length; j++) {
				angles[j] = r.nextDouble() * 2 * Math.PI;
			}
			
			RobotArmConfiguration randomConfig = new RobotArmConfiguration(-1, angles);
			
			if (!configurations.contains(randomConfig) && !Obstacles.hasCollision(randomConfig)) {
				configurations.add(randomConfig);
			} else {
				i--;
			}
		}
		
		System.out.println("RoadMap generated!");
	}
}
