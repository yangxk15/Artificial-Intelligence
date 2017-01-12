// Author: Xiankai Yang
// Stub provided by Devin Balkcom

package cannibals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class UUSearchProblem {
	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;
	
	protected interface UUSearchNode {
		public ArrayList<UUSearchNode> getSuccessors();
		public boolean goalTest();
		public int getDepth();
	}

	/**
	 * @author Xiankai Yang
	 * 
	 * Start from the startNode and search the graph using BFS
	 * 
	 * @return a list of connecting nodes from the start node to the goal node
	 * if there is a path from the start node to the goal node, null otherwise.
	 */
	public List<UUSearchNode> breadthFirstSearch() {
		resetStats();

		Queue<UUSearchNode> queue = new LinkedList<UUSearchNode>();
		HashMap<UUSearchNode, UUSearchNode> visited = new HashMap<UUSearchNode, UUSearchNode>();
		
		queue.add(startNode);
		visited.put(startNode, null);
		
		while (!queue.isEmpty()) {
			UUSearchNode currentNode = queue.poll();
			List<UUSearchNode> successors = currentNode.getSuccessors();
			
			for (UUSearchNode successor : successors) {
				if (!visited.containsKey(successor)) {
					// Mark this node as visited and update the stats
					visited.put(successor, currentNode);
					updateMemory(visited.size());
					incrementNodeCount();
					
					if (successor.goalTest()) {
						return backchain(successor, visited);
					}
					
					queue.add(successor);
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
	private List<UUSearchNode> backchain(UUSearchNode node,
			HashMap<UUSearchNode, UUSearchNode> visited) {
		List<UUSearchNode> nodeList = new LinkedList<UUSearchNode>();
		
		while (node != null && visited.containsKey(node)) {
			nodeList.add(0, node);
			node = visited.get(node);
		}
		
		return nodeList;
	}

	/**
	 * @author Xiankai Yang
	 * 
	 * Start from the startNode and search the graph using memoizing DFS
	 * 
	 * @param maxDepth: Maximum depth that DFS can search
	 * 
	 * @return a list of connecting nodes from the start node to the goal node
	 */
	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats(); 

		HashMap<UUSearchNode, Integer> visited = new HashMap<UUSearchNode, Integer>();
		
		return dfsrm(startNode, visited, 0, maxDepth);
	}

	/**
	 * @author Xiankai Yang
	 * 
	 * Recursive function that implements memoizing DFS with maximum depth
	 * 
	 * @param currentNode: Current node during the DFS
	 * @param visited: A hash map with the visited nodes as keys and their depths as values
	 * @param depth: Current depth
	 * @param maxDepth: Maximum depth
	 * 
	 * @return a list of connecting nodes from the start node to the goal node
	 */
	private List<UUSearchNode> dfsrm(UUSearchNode currentNode, HashMap<UUSearchNode, Integer> visited, 
			int depth, int maxDepth) {
		// "base case"
		if (depth > maxDepth || visited.containsKey(currentNode)) {
			return null;
		}

		// Mark this node as visited and update the stats
		visited.put(currentNode, depth);
		updateMemory(visited.size());
		incrementNodeCount();
		
		// "base case"
		if (currentNode.goalTest()) {
			List<UUSearchNode> nodeList = new LinkedList<UUSearchNode>();
			
			nodeList.add(0, currentNode);
			
			return nodeList;
		}
		
		List<UUSearchNode> successors = currentNode.getSuccessors();
		for (UUSearchNode successor : successors) {
			// "recursive case"
			List<UUSearchNode> nodeList = dfsrm(successor, visited, depth + 1, maxDepth);
			
			if (nodeList != null) {
				nodeList.add(0, currentNode);
				
				return nodeList;
			}
		}
		return null;
	}
	
	/**
	 * @author Devin Balkcom
	 * 
	 * Start from the startNode and search the graph using path-checking DFS
	 * 
	 * @param maxDepth: Maximum depth
	 * 
	 * @return a list of connecting nodes from the start node to the goal node
	 */
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();

		HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();

		return dfsrpc(startNode, currentPath, 0, maxDepth);
	}

	/**
	 * @author Xiankai Yang
	 * 
	 * Recursive function that implements path-checking DFS with maximum depth
	 * 
	 * @param currentNode: Current node during the DFS
	 * @param currentPath: A hash set with the visited nodes
	 * @param depth: Current depth
	 * @param maxDepth: Maximum depth
	 * 
	 * @return a list of connecting nodes from the start node to the goal node
	 */
	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode, HashSet<UUSearchNode> currentPath,
			int depth, int maxDepth) {
		if (depth > maxDepth || currentPath.contains(currentNode)) {
			return null;
		}

		// Mark this node as visited and update the stats
		currentPath.add(currentNode);
		updateMemory(currentPath.size());
		incrementNodeCount();
		
		if (currentNode.goalTest()) {
			// "base case"
			List<UUSearchNode> nodeList = new LinkedList<UUSearchNode>();
			
			nodeList.add(0, currentNode);
			
			return nodeList;
		}
		
		List<UUSearchNode> successors = currentNode.getSuccessors();
		for (UUSearchNode successor : successors) {
			// "recursive case"
			List<UUSearchNode> nodeList = dfsrpc(successor, currentPath, depth + 1, maxDepth);
			
			if (nodeList != null) {
				nodeList.add(0, currentNode);
				
				return nodeList;
			}
		}
		
		// Cannot find the goal node
		// No need to update the stats since it's a removal
		currentPath.remove(currentNode);
		
		return null;
	}

	/**
	 * @author Xiankai Yang
	 * 
	 * Iteratively call DFS recursive path-checking function
	 * with the maximum depth from 0 to the maxDepth
	 * 
	 * @param maxDepth: Maximum depth
	 * 
	 * @return a list of connecting nodes from the start node to the goal node
	 */
	public List<UUSearchNode> IDSearch(int maxDepth) {
		resetStats();
		
		for (int i = 0; i < maxDepth; i++) {
			HashSet<UUSearchNode> currentPath = new HashSet<UUSearchNode>();
			
			List<UUSearchNode> nodeList = dfsrpc(startNode, currentPath, 0, i);
			
			if (nodeList != null) {
				return nodeList;
			}
		}
		
		return null;
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}
	
	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}
	
	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}
	
	protected void incrementNodeCount() {
		nodesExplored++;
	}

}
