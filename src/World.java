import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class World implements
java.io.Serializable{
	protected ArrayList<Cell> cells = new ArrayList<Cell>();
	protected ArrayList<Cell> deadCells = new ArrayList<Cell>();
	protected ArrayList<Food> foods = new ArrayList<Food>();

	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	public static final byte NUMBER_OF_CELLS = 40;
	public static final int NUMBER_OF_FOOD = 200;
	public static final byte DARWIN_NUMBER = 10; // The Number of Cells, which
													// will reproduce
	public static final double CHANGE_FACTOR = 0.9;
	public static final double CHANGE_FACTOR_2 = 0.1; // varianz =
														// CHANGE_FAKTOR^(generation*CHANGE_FACTOR_2)

	public String data_directory = "/simulation/";

	private int generation = 1;
	private int generationLifetime = 0;
	private long totalLifetime = 0;

	public World() {
		// /*
		createInitialPopulation();
		/*
		 * ///* cells.add(new Cell(250, 250, 0)); //cells.add(new Cell(240, 240,
		 * 0)); //cells.get(1).setAngle(45); foods.add(new Food(280, 250));//
		 */
	}

	private int calculateAngle(double aX, double aY, double bX, double bY,
			int angle) {
		double dX = (aX - bX);
		double dY = (aY - bY);
		int direction = 0;
		if (dX != 0 && dY != 0)
			direction = ((int) Math.round(Math.toDegrees(Math.atan(dY / dX)))) % 360;
		if (dX > 0) {
			if (dY > 0) {// Quadrant I
				direction = direction;
			} else if (dY < 0) {// QUadrant II
				direction = direction + 270;
			} else if (dY == 0) {
				direction = 0;
			}
		} else if (dX < 0) {
			if (dY > 0) {// Quadrant IV
				direction = direction + 90;
			} else if (dY < 0) {// QUadrant III
				direction = direction + 180;
			} else if (dY == 0) {
				direction = 180;
			}
		} else if (dX == 0) {
			if (dY > 0) {
				direction = 90;
			} else if (dY < 0) {
				direction = 270;
			} else if (dY == 0) {
				direction = 0;
			}
		}
		direction = direction - angle;
		while (direction < 0) {
			direction += 360;
		}
		// System.out.println((aY - bY) + " " + (aX - bX) + " " + direction);
		return direction;
	}

	public void move() {
		for (Cell c : cells) {
			double[][] sense = new double[c.NUMBER_OF_FUEHLER][c.NUMBER_OF_FUEHLER_INPUT];
			for (byte a = 0; a < sense.length; a++) {
				for (byte b = 0; b < sense[a].length; b++) {
					sense[a][b] = 0;
				}
			}
			if (c.isAlive()) {
				for (Cell d : cells) {
					if (d != c) {// Check for Pointer, because it could be the
									// *same* and not just equal Objects
						double distance = Math.sqrt((d.getX() - c.getX())
								* (d.getX() - c.getX()) + (d.getY() - c.getY())
								* (d.getY() - c.getY()));
						if (distance < 50) {
							int direction = calculateAngle(d.getX(), d.getY(),
									c.getX(), c.getY(), c.getAngle());
							while (direction < 0) {
								direction = direction + 360;// Duerfte maximal
															// einmal passieren
							}
							/*
							 * if (d.getStrength() > 0) { if (sense[(int)
							 * Math.floor(direction / (360.0 /
							 * c.NUMBER_OF_FUEHLER))][1] < 1 / distance) {
							 * sense[(int) Math.floor(direction / (360.0 /
							 * c.NUMBER_OF_FUEHLER))][1] = 1 / distance; } //
							 * Auf gefressen pruefen if ((distance < Cell.RADIUS
							 * * 2)) { if ((d.getStrength() > 0 && d.isAlive())
							 * && c.getStrength() == 0) { c.setAlive(false); } }
							 * } else {
							 */
							if (sense[(int) Math.floor(direction
									/ (360.0 / c.NUMBER_OF_FUEHLER))][0] < 1 / distance) {
								sense[(int) Math.floor(direction
										/ (360.0 / c.NUMBER_OF_FUEHLER))][0] = 1 / distance;
							}
							/*
							 * if ((c.getStrength() > 0 && c.isAlive()) &&
							 * distance < Cell.RADIUS * 2) { d.setAlive(false);
							 * } }
							 */
						}
					}
				}
				c.move();
				while (c.getX() > WIDTH || c.getX() < 0) {
					c.setX((c.getX() + WIDTH) % WIDTH);
				}
				while (c.getY() > HEIGHT || c.getY() < 0) {
					c.setY((c.getY() + HEIGHT) % HEIGHT);
				}
			}
			if (c.isAlive()) {
				for (Food f : foods) {
					if (!f.isEaten()) {
						double distance = Math.sqrt((f.getX() - c.getX())
								* (f.getX() - c.getX()) + (f.getY() - c.getY())
								* (f.getY() - c.getY()));
						if (distance < 50) {
							int direction = calculateAngle(f.getX(), f.getY(),
									c.getX(), c.getY(), c.getAngle());
							while (direction < 0) {
								direction = direction + 360;// Duerfte maximal
															// einmal passieren
							}
							if (sense[(int) Math.floor(direction
									/ (360.0 / c.NUMBER_OF_FUEHLER))][2] < 1 / distance) {
								sense[(int) Math.floor(direction
										/ (360.0 / c.NUMBER_OF_FUEHLER))][2] = 1 / distance;
							}
							if (distance < c.RADIUS + f.RADIUS) {
								c.eat(f.ENERGY);
								f.setEaten(true);
							}
						}
					}
				}
			}
			c.setFuehlerInput(sense);
			c.compute_neurons();
			c.move();
		}
		for (int i = cells.size() - 1; i >= 0; i--) {
			if (!cells.get(i).isAlive()) {
				deadCells.add(0, cells.get(i));
				foods.add(new Food(cells.get(i).getX(), cells.get(i).getY()));
				cells.remove(i);
			}
		}
		for (int i = foods.size() - 1; i >= 0; i--) {
			if (foods.get(i).isEaten()) {
				foods.remove(i);
			}
		}
		generationLifetime++;
		totalLifetime++;
		if (cells.size() == 0) {
			System.out.println("All Members of the generation " + generation
					+ " are dead\n" + generationLifetime
					+ " Steps in this Generation\n" + totalLifetime
					+ " Steps total");
			generateGenerationPopulation();
		}
	}

	private void generateFood() {
		foods.clear();
		for (int i = 1; i <= NUMBER_OF_FOOD; i++) {
			foods.add(new Food(Math.random() * WIDTH, Math.random() * HEIGHT));
		}
	}

	private void createInitialPopulation() {
		for (int i = 1; i <= NUMBER_OF_CELLS; i++) {
			Cell c = new Cell(Math.random() * WIDTH, Math.random() * HEIGHT,
					generation);
			Matrix.fillRandom(c.getFuehlerToInput());
			Matrix.fillRandom(c.getInputToHidden1());
			Matrix.fillRandom(c.getHidden1ToHidden2());
			Matrix.fillRandom(c.getHidden2ToOutput());
			cells.add(c);
		}
		generateFood();
	}

	private void generateGenerationPopulation() {
		System.out.println("Now generation Generation " + (generation + 1)
				+ "!");
		cells.clear();//just to be sure
		generation++;
		generationLifetime = 0;
		for (int i = 0; i < DARWIN_NUMBER - 1; i++) {
			Cell a = new Cell(Math.random() * WIDTH, Math.random() * HEIGHT,
					generation);
			Cell d = deadCells.get(i);
			a.setFuehlerToInput(d.getFuehlerToInput());
			a.setInputToHidden1(d.getInputToHidden1());
			a.setHidden1ToHidden2(d.getHidden1ToHidden2());
			a.setHidden2ToOutput(d.getHidden2ToOutput());
			cells.add(a);
			for (int j = 0; j < NUMBER_OF_CELLS / DARWIN_NUMBER; j++) {
				Cell c = new Cell(Math.random() * WIDTH,
						Math.random() * HEIGHT, generation);
				d = deadCells.get(i);
				Matrix.fillRandom(c.getFuehlerToInput(), -1, 1);
				c.setFuehlerToInput(Matrix.add(
						Matrix.multiply(
								c.getFuehlerToInput(),
								Math.pow(CHANGE_FACTOR, generation
										* CHANGE_FACTOR_2)),
						d.getFuehlerToInput()));
				Matrix.fillRandom(c.getInputToHidden1(), -1, 1);
				c.setInputToHidden1(Matrix.add(
						Matrix.multiply(
								c.getInputToHidden1(),
								Math.pow(CHANGE_FACTOR, generation
										* CHANGE_FACTOR_2)),
						d.getInputToHidden1()));
				Matrix.fillRandom(c.getHidden1ToHidden2(), -1, 1);
				c.setHidden1ToHidden2(Matrix.add(
						Matrix.multiply(
								c.getHidden1ToHidden2(),
								Math.pow(CHANGE_FACTOR, generation
										* CHANGE_FACTOR_2)),
						d.getHidden1ToHidden2()));
				Matrix.fillRandom(c.getHidden2ToOutput(), -1, 1);
				c.setHidden2ToOutput(Matrix.add(
						Matrix.multiply(
								c.getHidden2ToOutput(),
								Math.pow(CHANGE_FACTOR, generation
										* CHANGE_FACTOR_2)),
						d.getHidden2ToOutput()));
				cells.add(c);
			}
		}
		deadCells.clear();
		generateFood();
	}

	public void saveDeadCells() {
		try {
			FileOutputStream fos = new FileOutputStream(generation + ".deadcells.data");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(deadCells);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void loadDeadCells(String filename){
		try
        {
			if(!filename.matches("(C:)?(.+)(\\d+)\\.deadcells\\.data")){
				System.err.println("Filename »"+filename+"« Invalid");
				return;
			}
			
			
			generation = 5;
			
			FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            deadCells = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
         }catch(IOException ioe){
             ioe.printStackTrace();
             return;
          }catch(ClassNotFoundException c){
             System.out.println("Class not found");
             c.printStackTrace();
             return;
          }
		generateGenerationPopulation();
	}

	public ArrayList<Cell> getCells() {
		return cells;
	}

	public ArrayList<Cell> getDeadCells() {
		return deadCells;
	}

	public ArrayList<Food> getFoods() {
		return foods;
	}

	public int getGeneration() {
		return generation;
	}

	public int getGenerationLifetime() {
		return generationLifetime;
	}

	public long getTotalLifetime() {
		return totalLifetime;
	}

	public String getData_directory() {
		return data_directory;
	}

	public void setData_directory(String data_directory) {
		this.data_directory = data_directory;
	}

}
