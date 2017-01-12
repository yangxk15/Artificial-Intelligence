package csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CircuitBoardLayoutCSP extends ConstraintSatisfactionProblem {
	/**
	 *  M * N rectangular circuit board
	 *  Assume the UPPER LEFT corner has coordinate (0, 0)
	 *  For point (x, y), index is x * N + y, with x in [0, M), and y in [0, N)
	 */
	char[][] board;
	
	List<String[]> componentLabel = new ArrayList<>();
	
	protected CircuitBoardLayoutCSP(String[] board, String[][] components,
			boolean minimumRemainingValueEnabled, boolean leastConstrainingValueEnabled, boolean AC3Enabled) {
		super(getDomains(board, components), getConstraints(board, components), minimumRemainingValueEnabled, leastConstrainingValueEnabled, AC3Enabled);
		
		this.board = new char[board.length][board[0].length()];
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[0].length; j++) {
				this.board[i][j] = '.';
			}
		}
		
		for (String[] component : components) {
			componentLabel.add(component);
		}
	}
	
	private static Constraint getConstraints(String[] board, String[][] components) {
		Map<List<Integer>, Set<List<Integer>>> constraints = new HashMap<>();
		List<Integer> indexList = new ArrayList<>();

		for (int i = 0; i < components.length; i++) {
			indexList.add(0, i);
			
			Set<Integer> area1 = getArea(board, components[i]);
			
			for (int j = i + 1; j < components.length; j++) {
				indexList.add(1, j);
				
				Set<Integer> area2 = getArea(board, components[j]);
				
				Set<List<Integer>> possibleCombinations = new HashSet<>();
				List<Integer> valueList = new ArrayList<>();
				
				for (Integer p1 : area1) {
					valueList.add(0, p1);
					
					for (Integer p2 : area2) {
						valueList.add(1, p2);
						
						if (!overlap(board, components[i], p1, components[j], p2)) {
							possibleCombinations.add(new ArrayList<>(valueList));
						}
						
						valueList.remove(1);
					}
					
					valueList.remove(0);
				}
				
				constraints.put(new ArrayList<>(indexList), possibleCombinations);
				
				indexList.remove(1);
			}
			indexList.remove(0);
		}
		
		return new Constraint(constraints);
	}

	private static List<Set<Integer>> getDomains(String[] board, String[][] components) {
		List<Set<Integer>> domains = new ArrayList<>();
		
		for (String[] component : components) {
			domains.add(getArea(board, component));
		}
		
		return domains;
	}
	
	private static Set<Integer> getArea(String[] board, String[] component) {
		Set<Integer> area = new HashSet<>();
		for (int i = 0; i <= board.length - component.length; i++) {
			for (int j = 0; j <= board[0].length() - component[0].length(); j++) {
				area.add(i * board[0].length() + j);
			}
		}
		return area;
	}


	private static boolean overlap(String[] board, String[] s1, Integer p1, String[] s2, Integer p2) {
		int x1 = p1 / board[0].length();
		int y1 = p1 % board[0].length();
		int x2 = p2 / board[0].length();
		int y2 = p2 % board[0].length();
		return x1 + s1.length > x2 && x2 + s2.length > x1 && y1 + s1[0].length() > y2 && y2 + s2[0].length() > y1;
	}
	
	@Override
	protected String displayAssignment() {
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < assignment.size(); k++) {
			String[] component = componentLabel.get(k);
			Integer location = assignment.get(k);
			int x = location / board[0].length;
			int y = location % board[0].length;
			for (int i = x; i < x + component.length; i++) {
				for (int j = y; j < y + component[0].length(); j++) {
					board[i][j] = component[i - x].charAt(j - y);
				}
			}
		}
		for (int i = 0; i < board.length; i++) {
			sb.append('\t');
			for (int j = 0; j < board[0].length; j++) {
				sb.append(board[i][j]);
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
}
