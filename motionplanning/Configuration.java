package motionplanning;

import java.awt.Shape;
import java.util.List;

public interface Configuration {
	
	double getCost();
	
	double getDistance(Configuration other);
	
	List<Shape> getShapes();
	
	List<Configuration> getSuccessors();
	
}
