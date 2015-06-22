package game;

import java.awt.*;
import java.util.ArrayList;

/*
 * NOTE TO SELF: BOT Y BOUND AND RIGHT X BOUND ARE FINNICKY
 * IF YOU FORGET TO ADD 1 TO THE X VAL WHEN MOVING RIGHT
 * OR 1 TO THE Y VALUE WHEN FALLING, YOU "BOUNCE" BECAUSE
 * THE HIT BOX IS SCREWED UP
 * AND IF YOU CALCULATE THE CURRENT SQUARE FOR COLLISION WHEN FALLING
 * YOU WILL ALWAYS BE "FALLING" EVEN IF IT DOESN'T LOOK LIKE IT SO YOU 
 * CAN'T JUMP ANYMORE
*/
public class Player extends Entity{

	private boolean gameOver = false;
	private boolean won = false;
	private int lives;
	private int maxLives;
	private double runningSpeed;
	
	private int NUM_BULLETS = 20;
	private Bullet[] bullets;
	private int currentBullet = 0;
	private double shootDelay = 1;
	private int bulletDamage = 1;
	
	// facing is for animations and for shooting. Have to know which way to shoot the bullets
	private boolean facingRight;
	
	/* shooting
	private boolean shootingEnabled;
	private ArrayList<Bullet> bullets;
	*/
	
	// CONSTRUCTOR
	public Player(TileMap tile){
		super(tile);
		lives = 3;
		maxLives = 3;
		
		bullets = new Bullet[NUM_BULLETS];
		for(int i = 0; i < NUM_BULLETS; i++){
			bullets[i] = new Bullet(tile);
		}
		
		width = 20;
		height = 20;
		cWidth = 20;
		cHeight = 20;
		centerx = width / 2;
		centery = height / 2;
		
		speed = 5;
		runningSpeed = 10.0;
		jumpSpeed = -15.0;	
		maxFallSpeed = 10.0;
		gravity = 1;
		
		facingRight = true;
	}
	
	public void checkNextPosition(){
		// MOVEMENT
		if(!running){
			if(left){
				dx = -speed;
				facingRight = false;
			}
			else if(right){
				dx = speed;
				facingRight = true;
			}
			else{
				dx = 0;
			}
		}
		if(running){
			if(left){
				dx = -runningSpeed;
				facingRight = false;
			}
			else if(right){
				dx = runningSpeed;
				facingRight = true;
			}
			else{
				dx = 0;
			}
		}
		
		if(jumping){
			dy = jumpSpeed;
			falling = true;
			jumping = false;
			jumpCount--; // to prevent infinite jumping
		}
		if(falling){
			dy += gravity;
			if(dy > maxFallSpeed){
				dy = maxFallSpeed;
			}
		}
		else{
			dy = 0;
			jumpCount = 2;
		}
	}
	
	// still doesn't work
	public void shoot(){
		long start = System.nanoTime();
		long elapsed = System.nanoTime() - start;
		
		if(elapsed > shootDelay){
			currentBullet++;
			if(currentBullet > NUM_BULLETS - 1){
				currentBullet = 0;
			}
			bullets[currentBullet].fire((int)x, (int)centery, facingRight);
			start = System.nanoTime();
		}
	}

	public boolean checkWinCondition(){
		if(getx() == 2314 && gety() == 86){
			won = true;
		}
		return won;
	}
	public boolean setGameOver(boolean bool){
		gameOver = bool;
		return gameOver;
	}
	public boolean getGameOver(){
		return gameOver;
	}
	public int setLives(int i){
		lives = i;
		return lives;
	}
	public int getCenterx(){
		return (int)centerx;
	}
	public int getCentery(){
		return (int)centery;
	}

	public void checkBounds(){
		if((int)y >= 475 && dy > 1){
			loseLife();
		}
	}
	public boolean getWin(){
		return won;
	}
	public boolean gameOver(){
		gameOver = true;
		return gameOver;
	}
	
	public void loseLife(){
		lives--;
		if(lives < 0){
			lives = 0;
		}
		if(lives == 0){
			gameOver();
		}
		this.moveEntity(54, 368); // reset position
	}
	public int getLives(){
		return lives;
	}
	public int getMaxLives(){
		return maxLives;
	}
	public void checkEnemyCollisions(ArrayList<Enemy> enemies){
		for(int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			if(intersects(e)){
				loseLife();
			}
			
			for(int j = 0; j < NUM_BULLETS; j++){
				if(bullets[j].intersects(e)){
					e.takeDamage(bulletDamage);
					
					// deal damage here
					// and make sure to destroy the bullet so there aren't
					// too many bullets flying around
				}
			}
			
		}
		
	}
	public void update(){
		checkNextPosition();
		checkCollision();
		moveEntity(tempx, tempy);
		
		// update bullets
		for(int i = 0; i < NUM_BULLETS; i++){
			bullets[i].update();
		}
	}

	public void draw(Graphics2D g){
		setMapLocation();
		
		g.setColor(Color.BLUE);
		super.draw(g);
	}
}
