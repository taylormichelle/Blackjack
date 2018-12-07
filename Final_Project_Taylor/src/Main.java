
public class Main implements Runnable {
	
	public static boolean sentinel = false;
	public static int pWins = 0;
	public static int dWins = 0;
	
	GUI gui = new GUI();
	
	public static void main(String[] args) {
		new Thread(new Main()).start();
	}
	
	@Override
	public void run() {
		while(sentinel == false) {
				gui.refresher();
				gui.repaint();
		}
	}
	
}