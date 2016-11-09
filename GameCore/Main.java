import javax.swing.JFrame;

public class Main{

	public static void main(String args[]){
	
		JFrame frame = new JFrame("Space Dodger");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new GameFrame());
		frame.setResizable(false);
		frame.setVisible(true);
	
	}

}