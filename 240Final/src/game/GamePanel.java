package game;

import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements KeyListener, Runnable{

	public static final int WIDTH = 512;
	public static final int HEIGHT = 480;
		
	private TileMap tileMap;
	public static Player player;
	public ArrayList<Enemy> enemies;
	
	// main thread
	private Thread thread;
	public static boolean keepGoing;
	
	//private Tile block;
	private BufferedImage image;
	private Graphics2D g;
	
	private int FPS = 1000 / 30; // 1000 updates per second, divided by desired frame rate
		
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	
	public void init(){
		keepGoing = true;		
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		tileMap = new TileMap("/Maps/level1Map.txt", 32);
		tileMap.loadTiles("/Tiles/brick.png");
		//block = new Tile("/Tiles/brick.png");
		player = new Player(tileMap);
		
		// set starting position 54, 368
		player.moveEntity(54, 368);
		spawnEnemies();
	}
	
	private void spawnEnemies(){
		enemies = new ArrayList<Enemy>();
		
		Enemy e;
		
		// array of points was not my idea, credit goes to another article
		Point[] spawnPoints = new Point[] {
			new Point(100, 435),
			new Point(312 ,435),
			new Point(532, 83),
			new Point(1820, 435)
		};
		
		for(int i = 0; i < spawnPoints.length; i++){
			e = new Enemy(tileMap);
			e.moveEntity(spawnPoints[i].x, spawnPoints[i].y);
			enemies.add(e);
		}
		BossEnemy b;
		b = new BossEnemy(tileMap);
		b.moveEntity(1700, 286);
		enemies.add(b);
	}
	public void run(){
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(keepGoing){
			// Brian helped me walk through the game loop and how it should work with waiting and 
			// calculating the time it should wait using system time
			start = System.nanoTime();
			
			updateGame();
			renderGame();
			drawGame();
			
			elapsed = (System.nanoTime() - start) / 1000000;
			wait = FPS - elapsed;
			if(wait < 0){
				wait = 0;
			}
			
			try{
				Thread.sleep(wait);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}	
	
	private void updateGame(){	
		tileMap.update();
		player.update();
			
		// run through various checks
		player.checkEnemyCollisions(enemies);
		player.checkWinCondition();
		player.getGameOver();
		player.checkBounds();
		// update all the enemies
		for(int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()){
				enemies.remove(i);
				i--;
			}
		}
		
		// camera will follow player, but not go below the "ground level"
		tileMap.setx((int)(GamePanel.WIDTH / 2 - player.getx()));
		tileMap.sety((int)(GamePanel.HEIGHT - 32 / 2 - player.gety()));
	}
	
	private void renderGame(){
		
		// clear the screen
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// draw the player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).draw(g);
		}
		
		//draw the tilemap
		tileMap.draw(g);
		
		// display lives in top left corner
		g.setColor(Color.RED);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
		g.drawString("Lives " + player.getLives() + "/" + player.getMaxLives(), 20, 20);
		
		if(player.getGameOver()){
			gameOverMessage();
		}
		if(player.checkWinCondition()){
			drawMessage();
		}
	}
	
	private void drawGame(){	
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	
	private void restartGame(){ // not currently working
		player.setGameOver(false);
		player.setLives(3);
	}
	
	private void drawMessage(){
		g.setColor(Color.BLUE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
		g.drawString("Congratulations!", GamePanel.WIDTH / 2 + 16, GamePanel.HEIGHT / 2);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
	}
	private void gameOverMessage(){
		g.setColor(Color.RED);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
		g.drawString("Game Over", GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
	}
	
	public void keyTyped(KeyEvent e){
	}
	
	public void keyPressed(KeyEvent e){
		int button = e.getKeyCode();
		if(!player.getGameOver()){ // if gameOver, we don't want to accept any input
			if(button == KeyEvent.VK_A){
				player.setLeft(true);
			}
			if(button == KeyEvent.VK_D){
				player.setRight(true);
			}
			if(button == KeyEvent.VK_SHIFT){
				player.setRunning(true);
			}
			if(button == KeyEvent.VK_SPACE){
				player.setJumping();
			}
			if(button == KeyEvent.VK_P){
				restartGame();
			}
			if(button == KeyEvent.VK_F){
				player.shoot();
			}
			if(button == KeyEvent.VK_L){
				player.loseLife();
			}
		}
		
	}
	
	public void keyReleased(KeyEvent e){
		int button = e.getKeyCode();
		if(button == KeyEvent.VK_A){
			player.setLeft(false);
		}
		if(button == KeyEvent.VK_D){
			player.setRight(false);
		}
		if(button == KeyEvent.VK_SHIFT){
			player.setRunning(false);
		}
	}
	
}
