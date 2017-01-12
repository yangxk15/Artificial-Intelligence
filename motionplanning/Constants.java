package motionplanning;

public interface Constants {
	/* Display Constants */
	int DISPLAY_WIDTH = 600;
	int DISPLAY_HEIGHT = 600;
	
	/* Obstacles Constants */
	long OBSTACLE_SEED = 2715;
	int OBSTACLE_WIDTH = 20;
	int OBSTACLE_HEIGHT = 20;
	double OBSTACLE_DENSITY = 0.01;

	/* Robot Arm Constants */
	int ARM_LENGTH = 100;

	/* Mobile Robot Constants */
	int CAR_LENGTH = 20;
	int CAR_WIDTH = 10;
	
	/* PRM Constants */
	long PRM_SEED = 156;
	int NEAREST_K = 10;

	/* RRT Constants */
	long RRT_SEED = 2715;
	int DT = 10;
	
}
