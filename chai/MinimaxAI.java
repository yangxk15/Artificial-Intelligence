package chai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;
class Node {
	int depth;
	int value;
	public Node(int depth, int value) {
		this.depth = depth;
		this.value = value;
	}
}
public class MinimaxAI implements ChessAI {

	private final int player;
	
	private final int maximumDepth;

	private final boolean alphaBetaPruningEnabled;
	
	private final boolean transpositionTableEnabled;
	
	private final Random r = new Random(1527);
	
	private int visitedPositions = 0;

	private HashMap<Long, Node> transpositionTable = new HashMap<>();
	
	public MinimaxAI(int player, int maximumDepth, boolean transpositionTableEnabled) {
		this(player, maximumDepth, false, transpositionTableEnabled);
	}
	
	protected MinimaxAI(int player, int maximumDepth, boolean alphaBetaPruningEnabled, boolean transpositionTableEnabled) {
		this.player = player;
		this.maximumDepth = maximumDepth;
		this.alphaBetaPruningEnabled = alphaBetaPruningEnabled;
		this.transpositionTableEnabled = transpositionTableEnabled;
	}

	@Override
	public short getMove(Position inputPosition) {
		Position position = new Position(inputPosition);
		
		List<Short> bestMoves = new ArrayList<>();
		
		int bestValue = -ChessAI.MAX_VALUE;
		
		for (int depth = 1; depth <= maximumDepth; depth++) {
			for (short move : position.getAllMoves()) {
				try {
					position.doMove(move);
				} catch (IllegalMoveException e) {
					e.printStackTrace();
					return 0;
				}
				
				int value = getValue(position, depth - 1, bestValue);
				
				if (value == ChessAI.MAX_VALUE) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return move;
				}
				
				if (value >= bestValue) {
					if (value > bestValue) {
						bestMoves.clear();
						bestValue = value;
					}
					
					bestMoves.add(move);
				}
				
				position.undoMove();
			}
		}
		
		if (player == Chess.WHITE)
			System.out.print("Visited positions: " + visitedPositions + "; Maximum depth: " + maximumDepth + " ");
		return bestMoves.get(r.nextInt(bestMoves.size()));
	}
	
	
	private int getValue(Position position, int maxDepth, int preValue) {
		
		visitedPositions++;
		
		if (transpositionTableEnabled) {
			Node node = transpositionTable.get(position.getHashCode());
			if (node != null && node.depth >= maxDepth) {
				return node.value;
			}
		}
		
		int sign = position.getToPlay() == player ? 1 : -1;
		
		if (position.isTerminal()) {
			int value = position.isMate() ? -sign * ChessAI.MAX_VALUE : 0;
			
			if (transpositionTableEnabled) {
				transpositionTable.put(position.getHashCode(), new Node(Integer.MAX_VALUE, value));
			}
			
			return value;
		}
		
		if (maxDepth <= 0) {
			int value = sign * evaluate(position);
			
			if (transpositionTableEnabled) {
				transpositionTable.put(position.getHashCode(), new Node(maxDepth, value));
			}
			
			return value;
		}
		
		int bestValue = -sign * ChessAI.MAX_VALUE;
		
		for (short move : getMoves(position)) {
			
			// Alpha-Beta-Pruning
			// a > b <==> -a < -b
			if (alphaBetaPruningEnabled && sign * preValue < sign * bestValue) {
				return bestValue;
			}
			
			try {
				position.doMove(move);
			} catch (IllegalMoveException e) {
				e.printStackTrace();
				return 0;
			}
			
			// Math.min(a, b) <==> -Math.max(-a, -b)
			// careful about the overflow
			
			bestValue = sign * Math.max(sign * bestValue, sign * getValue(position, maxDepth - 1, bestValue));
			position.undoMove();
		}

		if (transpositionTableEnabled) {
			transpositionTable.put(position.getHashCode(), new Node(maxDepth, bestValue));
		}
		
		return bestValue;
	}
	
	private List<Short> getMoves(Position position) {
		int sign = position.getToPlay() == player ? 1 : -1;
		
		List<Short> moves = new ArrayList<>();
		
		for (short move : position.getAllMoves()) {
			moves.add(move);
		}
		
		if (transpositionTableEnabled) {
			Collections.sort(moves, new Comparator<Short>() {

				@Override
				public int compare(Short o1, Short o2) {
					Node n1 = transpositionTable.get(o1);
					Node n2 = transpositionTable.get(o2);
					int i1 = n1 == null ? 0 : n1.value;
					int i2 = n2 == null ? 0 : n1.value;
					return sign * (i2 - i1);
				}
				
			});
		}
		
		return moves;
	}
}
