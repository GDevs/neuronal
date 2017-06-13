import java.awt.*;

import javax.swing.*;

public class GraphicalWorld extends JPanel {

	protected World world;


	public GraphicalWorld() {
		super();
		world = new World();
		this.setBounds(0, 0, 500, 500);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = this.getWidth();
		int height = this.getHeight();
		//System.out.println(width+" "+height);
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.gray);
		g.drawString("Generation: " + world.getGeneration(), 0, g
				.getFontMetrics().getHeight());
		g.drawString("Lifetime (Generation): " + world.getGenerationLifetime(),
				0, g.getFontMetrics().getHeight() * 2);
		g.drawString("Lifetime (Total): " + world.getTotalLifetime(), 0, g
				.getFontMetrics().getHeight() * 3);
		for (Cell c : world.getCells()) {
			//if (c.getStrength() > 0) {
			//	g.setColor(Color.red);
			//} else {
				g.setColor(Color.green);
//			}
			g.fillOval((int) Math.round(c.getX() - c.RADIUS),
					(int) Math.round(c.getY() - c.RADIUS),
					(int) Math.round(c.RADIUS * 2),
					(int) Math.round(c.RADIUS * 2));
			g.setColor(Color.yellow);
			g.drawLine((int) Math.round(c.getX()), (int) Math.round(c.getY()),
					(int) Math.round(c.getX() + Math.cos(c.getAngle()*Math.PI/180)*20),
					(int) Math.round(c.getY() + Math.sin(c.getAngle()*Math.PI/180)*20));
			for (int i = 0; i < Cell.NUMBER_OF_FUEHLER; i++) {
				g.setColor(Color.green);
				g.drawArc(
						(int) Math.round(c.getX() - c.getFuehlerInput()[i][0]
								* 100),
						(int) Math.round(c.getY() - c.getFuehlerInput()[i][0]
								* 100),
						(int) Math.round(c.getFuehlerInput()[i][0] * 100 * 2),
						(int) Math.round(c.getFuehlerInput()[i][0] * 100 * 2),
						-(c.getAngle() + i * 360 / Cell.NUMBER_OF_FUEHLER),
						-360 / Cell.NUMBER_OF_FUEHLER);
				g.setColor(Color.red);
				g.drawArc(
						(int) Math.round(c.getX() - c.getFuehlerInput()[i][1]
								* 100),
						(int) Math.round(c.getY() - c.getFuehlerInput()[i][1]
								* 100),
						(int) Math.round(c.getFuehlerInput()[i][1] * 100 * 2),
						(int) Math.round(c.getFuehlerInput()[i][1] * 100 * 2),
						-(c.getAngle() + i * 360 / Cell.NUMBER_OF_FUEHLER),
						-360 / Cell.NUMBER_OF_FUEHLER);
				g.setColor(Color.cyan);
				g.drawArc(
						(int) Math.round(c.getX() - c.getFuehlerInput()[i][2]
								* 100),
						(int) Math.round(c.getY() - c.getFuehlerInput()[i][2]
								* 100),
						(int) Math.round(c.getFuehlerInput()[i][2] * 100 * 2),
						(int) Math.round(c.getFuehlerInput()[i][2] * 100 * 2),
						-(c.getAngle() + i * 360 / Cell.NUMBER_OF_FUEHLER),
						-360 / Cell.NUMBER_OF_FUEHLER);
			}
		}
		for (Food f : world.getFoods()) {
			g.setColor(Color.CYAN);
			g.fillOval((int) Math.round(f.getX() - f.RADIUS),
					(int) Math.round(f.getY() - f.RADIUS),
					(int) Math.round(f.RADIUS * 2),
					(int) Math.round(f.RADIUS * 2));
		}
	}

	public void move() {
		// long startTime = System.currentTimeMillis();
		world.move();
		this.getParent().repaint();
		// System.out.println("Needed "
		// + (System.currentTimeMillis() - startTime) + " ms");
	}
	

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
