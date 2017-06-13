import java.util.Timer;
/**
 * 
 */
import java.util.TimerTask;

/**
 * @author adrian
 * 
 */
public class Main {

	private static Timer timer = new Timer();
	public static final GraphicalWorld grWorld = new GraphicalWorld();
	public static final FrmMain frmMain = new FrmMain();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		frmMain.setVisible(true);
		frmMain.addGraphicalWorld(grWorld);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				grWorld.move();
			}
		}, 0, 25);
	}

}
