package csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapColoringCSP extends ConstraintSatisfactionProblem  {
	List<String> colorLabel = new ArrayList<>();
	List<String> countryLabel = new ArrayList<>();
	
	public MapColoringCSP(
			String[] countries, String[] colors, String[][] adjacencies,
			boolean minimumRemainingValueEnabled,
			boolean leastConstrainingValueEnabled,
			boolean AC3Enabled) {
		
		super(getDomains(countries.length, colors.length), getConstraints(countries, colors.length, adjacencies),
				minimumRemainingValueEnabled, leastConstrainingValueEnabled, AC3Enabled);

		for (String color : colors) {
			this.colorLabel.add(color);
		}
		
		for (String country : countries) {
			this.countryLabel.add(country);
		}
	}
	
	private static Constraint getConstraints(String[] countries, int numberOfColors, String[][] adjacencies) {
		// Construct country name index map
		Map<String, Integer> countryIndex = new HashMap<>();
		
		for (int i = 0; i < countries.length; i++) {
			countryIndex.put(countries[i], i);
		}
		
		// Construct color constraints of adjacent countries
		Set<List<Integer>> adjacentConstraints = new HashSet<>();
		
		for (int i = 0; i < numberOfColors; i++) {
			for (int j = 0; j < numberOfColors; j++) {
				if (i == j) {
					continue;
				}
				List<Integer> adjacentConstraint = new ArrayList<>();
				adjacentConstraint.add(i);
				adjacentConstraint.add(j);
				adjacentConstraints.add(adjacentConstraint);
			}
		}
		
		// Construct constraints map
		Map<List<Integer>, Set<List<Integer>>> constraints = new HashMap<>();
		
		for (String[] adjacency : adjacencies) {
			List<Integer> indexList = new ArrayList<>();
			for (String country : adjacency) {
				indexList.add(countryIndex.get(country));
			}
			constraints.put(indexList, adjacentConstraints);
		}
		
		return new Constraint(constraints);
	}

	private static List<Set<Integer>> getDomains(int numberOfCountries, int numberOfColors) {
		List<Set<Integer>> domains = new ArrayList<>(numberOfCountries);
		
		for (int i = 0; i < numberOfCountries; i++) {
			Set<Integer> domain = new HashSet<>(numberOfColors);
			for (int j = 0; j < numberOfColors; j++) {
				domain.add(j);
			}
			domains.add(domain);
		}
		
		return domains;
	}
	
	@Override
	protected String displayAssignment() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < assignment.size(); i++) {
			sb.append("\t");
			sb.append(countryLabel.get(i));
			sb.append(": ");
			sb.append(colorLabel.get(assignment.get(i)));
			sb.append(System.lineSeparator());
		}
		
		return sb.toString();
	}

}
