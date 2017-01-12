package motionplanning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import motionplanning.rrt.MobileRobotConfiguration;
import motionplanning.rrt.RapidlyExploringRandomTree;

public class Drawer extends JPanel {
	private static final long serialVersionUID = 1L;

	MotionPlanningProblem problem;
	
	List<Configuration> trajectory;
	Iterator<Configuration> iterator;
	
	public Drawer(MotionPlanningProblem problem) {
		this.problem = problem;
		
		trajectory = problem.GreedySearch();
		if (trajectory != null) {
			System.out.println("Found a way!");
			iterator = trajectory.iterator();
		} else {
			System.out.println("No way!");
		}
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (Shape obstacle : Obstacles.obstacles) {
			g2.fill(obstacle);
		}
		
		if (problem instanceof RapidlyExploringRandomTree) {
			Set<MobileRobotConfiguration> configurations = ((RapidlyExploringRandomTree) problem).configurations;
			for (MobileRobotConfiguration configuration : configurations) {
				for (Configuration neighbour : configuration.getSuccessors()) {
					MobileRobotConfiguration nbr = (MobileRobotConfiguration) neighbour;
					g2.drawLine(configuration.getX(), configuration.getY(), nbr.getX(), nbr.getY());
				}
			}
		}
		
		g2.setColor(new Color(102, 204, 0));
		g2.setStroke(new BasicStroke(10));
		
		if (trajectory != null) {
			if (iterator.hasNext()) {
				List<Shape> shapes = iterator.next().getShapes();
				for (Shape shape : shapes) {
					g2.draw(shape);
				}
			} else {
				iterator = trajectory.iterator();
			}
		}
	}
	
	public void draw() {
		JFrame frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT);
		frame.setResizable(false);
        frame.getContentPane().setBackground(Color.WHITE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridLayout(1, 1, 0, 0));
		
		new Timer(100, e -> { this.repaint(); }).start();
		
		frame.add(this);
		
		frame.setVisible(true);
	}
}
