package motionplanning;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class MotionPlanningProblem {
	protected Configuration startConfiguration;
	protected Configuration goalConfiguration;
	
	public MotionPlanningProblem(Configuration startConfiguration, Configuration goalConfiguration) {
		this.startConfiguration = startConfiguration;
		this.goalConfiguration = goalConfiguration;
	}
	
	public List<Configuration> UCSSearch() {
		return priorityBreathFirstSearch(a -> a.getCost());
	}
	
	public List<Configuration> GreedySearch() {
		return priorityBreathFirstSearch(a -> a.getDistance(goalConfiguration));
	}
	
	public List<Configuration> AStarSearch() {
		return priorityBreathFirstSearch(a -> a.getCost() + a.getDistance(goalConfiguration));
	}
	
	/**
	 * @author Xiankai Yang
	 * 
	 * Private generic function for priority based breath first search.
	 * 
	 * @param comparator: Customized comparator for different search preference
	 * 
	 * @return a list of configurations which indicates the path
	 */
	private List<Configuration> priorityBreathFirstSearch(PriorityComparator comparator) {
		PriorityQueue<Configuration> frontier = new PriorityQueue<>(comparator);
		Map<Configuration, Configuration> visited = new HashMap<Configuration, Configuration>();
		Map<Configuration, Double> minValue = new HashMap<Configuration, Double>();
		
		frontier.add(startConfiguration);
		visited.put(startConfiguration, null);
		minValue.put(startConfiguration, comparator.getPriority(startConfiguration));
		
		while (!frontier.isEmpty()) {
			Configuration currentConfig = frontier.poll();

			if (currentConfig.equals(goalConfiguration)) {
				return backchain(currentConfig, visited);
			}
			
			if (minValue.get(currentConfig) < comparator.getPriority(currentConfig)) {
				continue;
			}

			List<Configuration> successors = currentConfig.getSuccessors();

			for (Configuration successor : successors) {
				if (!minValue.containsKey(successor) || minValue.get(successor) > comparator.getPriority(successor)) {
					visited.put(successor, currentConfig);
					minValue.put(successor, comparator.getPriority(successor));
					frontier.add(successor);
				}
			}
		}
		return null;
	}
	
	/**
	 * @author Xiankai Yang
	 * 
	 * Use the visited hash map to obtain the path from the start node
	 * to the goal node by backchaining parent nodes
	 * 
	 * @param config: Goal node of the problem
	 * @param visited: A hash map with the visited nodes as keys and their parent nodes as values
	 * 
	 * @return a list of connecting nodes from the start node to the goal node
	 */
	private List<Configuration> backchain(Configuration config,
			Map<Configuration, Configuration> visited) {
		List<Configuration> nodeList = new LinkedList<Configuration>();
		
		while (config != null && visited.containsKey(config)) {
			nodeList.add(0, config);
			config = visited.get(config);
		}
		
		return nodeList;
	}
}
