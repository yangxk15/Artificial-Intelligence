package motionplanning.rrt;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import motionplanning.Constants;
import motionplanning.MotionPlanningProblem;
import motionplanning.Obstacles;

public class RapidlyExploringRandomTree extends MotionPlanningProblem {
	Random r = new Random(Constants.RRT_SEED);
	
	protected int sample_number;
	
	public Set<MobileRobotConfiguration> configurations = new HashSet<MobileRobotConfiguration>();
	
	public RapidlyExploringRandomTree(MobileRobotConfiguration startConfiguration, MobileRobotConfiguration goalConfiguration, int sample_number) {
		super(startConfiguration, goalConfiguration);
		
		this.sample_number = sample_number;
		
		this.configurations.add(startConfiguration);
		
		generateRandomTree();
	}

	private void generateRandomTree() {
		for (int i = 0; i < sample_number; i++) {
			int margin = Math.max(Constants.CAR_LENGTH, Constants.CAR_WIDTH);
			double x = margin + r.nextInt(Constants.DISPLAY_WIDTH - 2 * margin);
			double y = margin + r.nextInt(Constants.DISPLAY_HEIGHT - 2 * margin);
			double theta = r.nextDouble() * 2 * Math.PI;
			
			MobileRobotConfiguration randomConfig = new MobileRobotConfiguration(-1, x, y, theta);
			if (Obstacles.hasCollision(randomConfig)) {
				i--;
				continue;
			}
			
			MobileRobotConfiguration newConfig = null;
			MobileRobotConfiguration oldConfig = null;
			
			for (MobileRobotConfiguration configuration : configurations) {
				oldConfig = 
						newConfig == 
							(newConfig = randomConfig.minConfig(newConfig, configuration.getIncrementalConfiguration(randomConfig)))
						? oldConfig
						: configuration;
			}
			
			if (newConfig != null && !configurations.contains(newConfig) && !Obstacles.hasCollision(newConfig)) {
				configurations.add(newConfig);
				oldConfig.neighbours.add(newConfig);
			} else {
				i--;
			}
			
			System.out.println("Progress: " + i + " / " + sample_number);
		}

		MobileRobotConfiguration goalConfig = null;
		
		for (MobileRobotConfiguration configuration : configurations) {
			goalConfig = ((MobileRobotConfiguration) goalConfiguration).minConfig(goalConfig, configuration);
		}
		
		this.goalConfiguration = goalConfig;
		
		System.out.println("Random Tree generated!");
	}
}
