package motionplanning;

import java.util.Comparator;

public interface PriorityComparator extends Comparator<Configuration>{
	
	public abstract double getPriority(Configuration configuration);
	
	@Override
	public default int compare(Configuration config1, Configuration config2) {
		return (int) Math.signum(getPriority(config1) - getPriority(config2));
	}
	
}