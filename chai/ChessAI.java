package chai;

import chesspresso.position.Position;

public interface ChessAI {
	int MAX_VALUE = new Position("4k3/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").getMaterial();
	public short getMove(Position position);
	public default int evaluate(Position position) {
		return position.getMaterial();
	}
}
