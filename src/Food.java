
public class Food extends Entity implements
java.io.Serializable{
	protected boolean eaten = false;
	
	public static final double RADIUS = 3;
	public static final int ENERGY = 500;
	
	public Food(double aX,double aY){
		x = aX;
		y = aY;
	}
	

	public boolean isEaten() {
		return eaten;
	}

	public void setEaten(boolean eaten) {
		this.eaten = eaten;
	}

}
