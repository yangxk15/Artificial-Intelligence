package csp;

public class CircuitBoardLayoutCSPTest {
	public static void main(String[] args) {
		String[] board = new String[]{
			"..........",
			"..........",
			"..........",
		};
		String[][] components = new String[][]{
			{
				"bbbbb",
				"bbbbb",
			},
			{
				"cc",
				"cc",
				"cc",
			},
			{
				"aaa",
				"aaa",
			},
			{
				"eeeeeee",
			},
		};
		new CircuitBoardLayoutCSP(board, components, false, false, false).display();
		new CircuitBoardLayoutCSP(board, components, true, false, false).display();
		new CircuitBoardLayoutCSP(board, components, false, true, false).display();
		new CircuitBoardLayoutCSP(board, components, false, false, true).display();
	}
}
