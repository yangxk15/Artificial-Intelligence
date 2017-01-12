package hmm;

public class Driver {
	public static void main(String[] args) {
		// Test case 1
		new Distribution(
				new char[][]{
					{'r','g','b'},
					{'g','b','r'},
					{'b','r','g'},
				},
				new int[]{0, 0})
				.apply("East", "East", "East", "South", "South", "South");
//		// Test case 2
//		new Distribution(
//				new char[][]{
//					{'r','r','r','r'},
//					{'r','r','r','r'},
//					{'r','r','r','r'},
//					{'r','r','r','r'},
//				},
//				new int[]{3, 0})
//				.apply("East", "North", "East", "North");
//		// Test case 3
//		new Distribution(
//				new char[][]{
//					{'r','y','b','r'},
//					{'b','b','y','g'},
//					{'b','r','g','b'},
//					{'r','y','y','b'},
//				},
//				new int[]{3, 0})
//				.apply("East", "North", "East", "North", "North", "East");
//		// Test case 4
//		new Distribution(
//				new char[][]{
//					{'r','r','b','r'},
//					{'b','b','y','g'},
//					{'W','W','g','b'},
//					{'y','W','b','b'},
//				},
//				new int[]{3, 0})
//				.apply("East", "North", "East", "North", "North", "East");
	}
}
