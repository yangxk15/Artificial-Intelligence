package csp;

public class MapColoringCSPTest {
	public static void main(String[] args) {
		String[] colors = new String[]{
				"Red",
				"Green",
				"Blue"
		};
		String[] countries = new String[]{
				"Western Australia",
				"Northern Territory",
				"South Australia",
				"New South Wales",
				"Queensland",
				"Victoria",
				"Tasmania"
		};
		String[][] adjacencies = new String[][]{
			{"Western Australia", "Northern Territory"},
			{"Western Australia", "South Australia"},
			{"Northern Territory", "South Australia"},
			{"Northern Territory", "Queensland"},
			{"South Australia", "Queensland"},
			{"South Australia", "New South Wales"},
			{"South Australia", "Victoria"},
			{"Queensland", "New South Wales"},
			{"New South Wales", "Victoria"},
		};
		
		// Brute Force
		new MapColoringCSP(countries, colors, adjacencies, false, false, false).display();
		// Minimum Remaining Value Heuristic Enabled
		new MapColoringCSP(countries, colors, adjacencies, true, false, false).display();
		// Least Constraining Value Heuristic Enabled
		new MapColoringCSP(countries, colors, adjacencies, false, true, false).display();
		// AC3 Inference Enabled
		new MapColoringCSP(countries, colors, adjacencies, false, false, true).display();
	}
}
