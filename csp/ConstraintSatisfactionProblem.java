package csp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConstraintSatisfactionProblem {
	
	// Number of node visited
	int visited = 0;
	
	// time elapsed during search
	long timeElapsed;
	
	boolean minimumRemainingValueEnabled;
	
	boolean leastConstrainingValueEnabled;
	
	boolean AC3Enabled;
	
	List<Set<Integer>> domains;

	Constraint constraints;
	
	Map<Integer, Integer> assignment = new HashMap<>();
	
	protected ConstraintSatisfactionProblem(List<Set<Integer>> domains, Constraint constraints,
			boolean minimumRemainingValueEnabled, boolean leastConstrainingValueEnabled, boolean AC3Enabled) {
		
		this.minimumRemainingValueEnabled = minimumRemainingValueEnabled;
		this.leastConstrainingValueEnabled = leastConstrainingValueEnabled;
		this.AC3Enabled = AC3Enabled;
		this.domains = domains;
		this.constraints = constraints;
		
		if (this.AC3Enabled) {
			constraints.ac3Inference(domains);
		}

		long startTime = System.currentTimeMillis();
		backtrack(getNextIndex(-1));
		long endTime = System.currentTimeMillis();
		
		timeElapsed = endTime - startTime;
	}
	
	private boolean backtrack(Integer index) {
		visited++;
		
		if (index == null) {
			return true;
		}
		
		List<Integer> domain = new ArrayList<>(domains.get(index));
		
		Collections.sort(domain,
				leastConstrainingValueEnabled
				? ((a, b) -> constraints.getConstrainingValue(domains, index, a, assignment) - constraints.getConstrainingValue(domains, index, b, assignment))
				: null);
		
		for (Integer value : domain) {
			assignment.put(index, value);
			if (constraints.isSatisfied(assignment, index)) {
//				System.out.println("Set " + index + " to " + assignment.get(index));
				if (backtrack(getNextIndex(index))) {
					return true;
				}
//				System.out.println("Shouldn't set " + index + " to " + assignment.get(index));
			}
			assignment.remove(index);
		}
		
		return false;
	}
	
	public void display() {
		System.out.println("**************************");
		System.out.println("Time elapsed: " + timeElapsed + "ms");
		System.out.println("Node visited: " + visited);
		System.out.println("MRV Enabled: " + minimumRemainingValueEnabled + ", LCV Enabled: " + leastConstrainingValueEnabled + ", AC3 Enabled: " + AC3Enabled);
		System.out.println("Assignment: {");
		System.out.println(assignment.size() != domains.size() ? "No assignment can satisfy the constraints!" : displayAssignment());
		System.out.println("}");
	}
	
	protected String displayAssignment() {
		return "\t" + assignment.toString();
	}
	
	private Integer getNextIndex(int index) {
		return minimumRemainingValueEnabled ? constraints.getMinimumRemainingValue(domains, assignment) : (index == domains.size() - 1 ? null : index + 1);
	}
}
