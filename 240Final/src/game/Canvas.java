package game;

import javax.swing.JFrame;

public class Canvas {
	
	public static void main(String[] args){
		
		JFrame window = new JFrame("Baby's First Platformer");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
	}

}
