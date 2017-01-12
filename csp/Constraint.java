package csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Constraint {
	
	/**
	 * 
	 * @author Xiankai Yang
	 * 
	 * Outer Key: Index of variables
	 * Outer Value: {
	 *     Inner Key:  Indexes of constraint variables
	 *     Inner Value: Values of constraint variables
	 * }
	 */
	Map<Integer, Map<List<Integer>, Set<List<Integer>>>> constraints = new HashMap<>();

	/**
	 * @author Xiankai Yang
	 * 
	 * Constraint constructor.
	 * 
	 * @param inputConstraints
	 */
	public Constraint(Map<List<Integer>, Set<List<Integer>>> inputConstraints) {
		for (Map.Entry<List<Integer>, Set<List<Integer>>> inputConstraint : inputConstraints.entrySet()) {
			List<Integer> indexList = inputConstraint.getKey();
			Set<List<Integer>> valueList = inputConstraint.getValue();
			for (Integer index : indexList) {
				if (!constraints.containsKey(index)) {
					constraints.put(index, new HashMap<>());
				}
				constraints.get(index).put(indexList, valueList);
			}
		}
	}
	/**
	 * @author Xiankai Yang
	 * 
	 * Given the current assignment, check whether it's still satisfied by the constraints
	 * involved with the latest index.
	 * 
	 * @param assignment
	 * @return
	 */
	protected boolean isSatisfied(Map<Integer, Integer> assignment, int latestIndex) {
		Map<List<Integer>, Set<List<Integer>>> subConstraints = constraints.get(latestIndex);
		
		if (subConstraints == null) {
			return true;
		}
		
		for (Map.Entry<List<Integer>, Set<List<Integer>>> entry : subConstraints.entrySet()) {
			List<Integer> indexList = entry.getKey();
			Set<List<Integer>> valueList = entry.getValue();
			
			if (!assignment.keySet().containsAll(indexList)) {
				continue;
			}
			
			List<Integer> partialAssignment = new ArrayList<>();
			
			for (Integer index : indexList) {
				partialAssignment.add(assignment.get(index));
			}
			
			if (!valueList.contains(partialAssignment)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @author Xiankai Yang
	 * 
	 * Given the current assignment, return the index with the minimum number of possible values.
	 * 
	 * @param domain
	 * @param assignment
	 * @return
	 */
	public Integer getMinimumRemainingValue(List<Set<Integer>> domain, Map<Integer, Integer> assignment) {
		Integer index = null;
		Integer value = null;
		for (int i = 0; i < domain.size(); i++) {
			if (assignment.containsKey(i)) {
				continue;
			}
			Integer rv = getRemainingValue(i, domain.get(i), assignment);
			if (index == null || rv < value) {
				index = i;
				value = rv;
			}
		}
		return index;
	}
	
	/**
	 * @author Xiankai Yang
	 * 
	 * Given the current assignment, and the index assigned by value, return the number of "ruled out" possible values of other variables,
	 * which is # current possible values of other variables - # afterwards possible values of other variables
	 * For comparison purpose, the former is the same, so we only calculate # afterwards possible values of other variables
	 * 
	 * @param index
	 * @param a
	 * @param assignment
	 * @return
	 */
	public int getConstrainingValue(List<Set<Integer>> domains, Integer index, Integer value, Map<Integer, Integer> assignment) {
		int count = 0;
		assignment.put(index, value);
		for (int i = 0; i < domains.size(); i++) {
			if (assignment.containsKey(i) || i == index) {
				continue;
			}
			count += getRemainingValue(i, domains.get(i), assignment);
		}
		assignment.remove(index);
		return -count;
	}

	/**
	 * @author Xiankai Yang
	 * 
	 * Given the current assignment, count the number of possible values for the index.
	 * 
	 * @param index
	 * @param domain
	 * @param assignment
	 * @return
	 */
	private Integer getRemainingValue(int index, Set<Integer> domain, Map<Integer, Integer> assignment) {
		int count = 0;
		for (Integer value : domain) {
			assignment.put(index, value);
			if (isSatisfied(assignment, index)) {
				count++;
			}
			assignment.remove(index);
		}
		return count;
	}
	
	/**
	 * Use the current constraints to update the domains using AC3 inference algorithm
	 * @param domains
	 */
	public void ac3Inference(List<Set<Integer>> domains) {
		Queue<int[]> arcs = new LinkedList<>();
		Map<Integer, Integer> assignment = new HashMap<>();
		
		for (int i = 0; i < domains.size(); i++) {
			for (int j = 0; j < domains.size(); j++) {
				if (i == j) {
					continue;
				}
				arcs.add(new int[]{i, j});
			}
		}
		
		while (!arcs.isEmpty()) {
			int[] arc = arcs.poll();
			// enforce consistency
			int a = arc[0];
			int b = arc[1];
			Set<Integer> toDelete = new HashSet<>();
			for (Integer v1 : domains.get(a)) {
				boolean legal = false;
				assignment.put(a, v1);
				if (!isSatisfied(assignment, a)) {
					continue;
				}
				for (Integer v2 : domains.get(b)) {
					assignment.put(b, v2);
					if (isSatisfied(assignment, b)) {
						assignment.remove(b);
						legal = true;
						break;
					}
					assignment.remove(b);
				}
				assignment.remove(a);
				if (!legal) {
					//System.out.println(a + "->" + b + " " + v1);
					toDelete.add(v1);
					for (int i = 0; i < domains.size(); i++) {
						if (i == a) {
							continue;
						}
						arcs.add(new int[]{i, a});
					}
				}
			}
			domains.get(a).removeAll(toDelete);
		}
	}
}
