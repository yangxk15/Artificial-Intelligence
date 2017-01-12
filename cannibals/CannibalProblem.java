// Author: Xiankai Yang
// Stub provided by Devin Balkcom

package cannibals;

import java.util.ArrayList;
import java.util.Arrays;

public class CannibalProblem extends UUSearchProblem {
	private int goalm, goalc, goalb;
	private int totalMissionaries, totalCannibals; 

	public CannibalProblem(int sm, int sc, int sb, int gm, int gc, int gb) {
		startNode = new CannibalNode(sm, sc, sb, 0);
		goalm = gm;
		goalc = gc;
		goalb = gb;
		totalMissionaries = sm;
		totalCannibals = sc;
	}
	
	private class CannibalNode implements UUSearchNode {
		private final static int BOAT_SIZE = 2;
		
		private int[] state; 
		private int depth;  

		public CannibalNode(int m, int c, int b, int d) {
			state = new int[3];
			
			this.state[0] = m;
			this.state[1] = c;
			this.state[2] = b;
			
			depth = d;
		}

		/**
		 * @author Xiankai Yang
		 * 
		 * Retrieve all the possible nodes from this cannibal node
		 * and find out which of them are safe to reach
		 * 
		 * @return a list of safe and reachable nodes from this cannibal node
		 */
		public ArrayList<UUSearchNode> getSuccessors() {
			ArrayList<UUSearchNode> successors = new ArrayList<UUSearchNode>();
			// direction defines whether it is a
			// subtraction (boat is left -> right) or
			//    addition (boat is right -> left)
			int direction = state[2] == 1 ? -1 : 1;
			int missionariesMax = state[2] == 1 ? state[0] : totalMissionaries - state[0];
			int cannibalsMax = state[2] == 1 ? state[1] : totalCannibals - state[1];
			// i is the number of missionaries who will be on the boat
			for (int i = 0; i <= missionariesMax; i++) {
				// j is the number of cannibals who will be on the boat
				for (int j = 0; j <= cannibalsMax; j++) {
					if (i + j == 0) {
						// Someone has to be on the boat
						continue;
					} else if (i + j > BOAT_SIZE) {
						// Already exceeds, stop incrementing
						break;
					}
					// check the whether the state is safe or not
					if (isSafeState(state[0] + direction * i, state[1] + direction * j)) {
						successors.add(new CannibalNode(
								state[0] + direction * i,
								state[1] + direction * j,
								state[2] + direction,
								depth + 1));
					}
				}
			}
			//System.out.println(this + "'s successors are " + successors);
			return successors;
		}
		
		@Override
		public boolean goalTest() {
			return state[0] == goalm && state[1] == goalc && state[2] == goalb;
		}

		@Override
		public boolean equals(Object other) {
			return Arrays.equals(state, ((CannibalNode) other).state);
		}

		@Override
		public int hashCode() {
			return state[0] * 100 + state[1] * 10 + state[2];
		}

		@Override
		public String toString() {
			return "(" + state[0] + "," + state[1] + "," + state[2] + ")";
		}

		@Override
		public int getDepth() {
			return depth;
		}

		/**
		 * @author Xiankai Yang
		 * 
		 * Determine the safety of both sides of the river
		 * @param missionaries 
		 * @param cannibals
		 * 
		 * @return whether the current state is safe or not
		 */
		private boolean isSafeState(int missionaries, int cannibals) {
			boolean leftSafe = missionaries == 0 || missionaries >= cannibals;
			boolean rightSafe = totalMissionaries - missionaries == 0
					|| totalMissionaries - missionaries >= totalCannibals - cannibals;
			return leftSafe && rightSafe;
		}

	}
}
