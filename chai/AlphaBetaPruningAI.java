package chai;

public class AlphaBetaPruningAI extends MinimaxAI {
	
	public AlphaBetaPruningAI(int player, int maximumDepth, boolean transpositionTableEnabled) {
		super(player, maximumDepth, true, transpositionTableEnabled);
	}
	
}
