package mazeworld;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class SearchProblem {
	protected static final char FLOOR = '.';
	protected static final char WALL = '#';

	protected char[][] maze;
	
	protected SearchNode startNode;
	protected SearchNode goalNode;
	
	protected SearchProblem(char[][] maze) {
		this.maze = maze;
	}
	
	protected interface SearchNode {
		public int getCost();
		public int getHeuristic();
		public List<SearchNode> getSuccessors();
		public void visualize() throws InterruptedException;
	}
	
	protected abstract class PriorityComparator implements Comparator<SearchNode> {
		public abstract int getPriority(SearchNode node);
		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			return Integer.valueOf(getPriority(o1)).compareTo(Integer.valueOf(getPriority(o2)));
		}
	}

	private class UCSPriorityComparator extends PriorityComparator {
		@Override
		public int getPriority(SearchNode node) {
			return node.getCost();
		}
	}
	
	private class GreedyPriorityComparator extends PriorityComparator {
		@Override
		public int getPriority(SearchNode node) {
			return node.getHeuristic();
		}
	}
	
	private class AStarPriorityComparator extends PriorityComparator {
		@Override
		public int getPriority(SearchNode node) {
			return node.getCost() + node.getHeuristic();
		}
	}

	
	public List<SearchNode> UCSSearch() {
		return priorityBreathFirstSearch(new UCSPriorityComparator());
	}
	
	public List<SearchNode> GreedySearch() {
		return priorityBreathFirstSearch(new GreedyPriorityComparator());
	}
	
	public List<SearchNode> AStarSearch() {
		return priorityBreathFirstSearch(new AStarPriorityComparator());
	}
	
	/**
	 * @author Xiankai Yang
	 * @return
	 */
	private List<SearchNode> priorityBreathFirstSearch(PriorityComparator comparator) {
		PriorityQueue<SearchNode> frontier = new PriorityQueue<SearchNode>(comparator);
		Map<SearchNode, SearchNode> visited = new HashMap<SearchNode, SearchNode>();
		Map<SearchNode, Integer> minValue = new HashMap<SearchNode, Integer>();
		
		frontier.add(startNode);
		visited.put(startNode, null);
		minValue.put(startNode, comparator.getPriority(startNode));
		
		while (!frontier.isEmpty()) {
			SearchNode currentNode = frontier.poll();

			if (minValue.get(currentNode) < comparator.getPriority(currentNode)) {
				continue;
			}
			
			if (currentNode.equals(goalNode)) {
				return backchain(currentNode, visited);
			}

			List<SearchNode> successors = currentNode.getSuccessors();
			for (SearchNode successor : successors) {
				if (!minValue.containsKey(successor) || minValue.get(successor) > comparator.getPriority(successor)) {
					visited.put(successor, currentNode);
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
	 * @param node: Goal node of the problem
	 * @param visited: A hash map with the visited nodes as keys and their parent nodes as values
	 * 
	 * @return a list of connecting nodes from the start node to the goal node
	 */
	private List<SearchNode> backchain(SearchNode node,
			Map<SearchNode, SearchNode> visited) {
		List<SearchNode> nodeList = new LinkedList<SearchNode>();
		
		while (node != null && visited.containsKey(node)) {
			nodeList.add(0, node);
			node = visited.get(node);
		}
		
		return nodeList;
	}
}
