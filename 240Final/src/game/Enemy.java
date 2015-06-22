package game;

import java.awt.*;

public class Enemy extends Entity{
	private boolean dead;
	private int health;
	private int maxHealth;

	// CONSTRUCTORS
	public Enemy(TileMap tile){
		super(tile);
		
		width = 25;
		height = 25;
		
		// collision
		cWidth = 20;
		cHeight = 20;
	
		// I make centerx and centery because I prefer to think of the center position of a charcter
		// and not the top left where boxes are normally positioned
		centerx = width / 2;
		centery = height / 2;

		speed = 3;
		health = 1;
		maxHealth = 1;
		gravity = 5;
		
		// have to set a direction to true so the enemy will move right away
		right = true;
	}
	
	public boolean isDead(){
		return dead;
	}
	public void checkNextPosition(){
		if(left){
			dx = -speed;
		}
		else if(right){
			dx = speed;
		}
		// Enemies could possibly fall?
		if(falling){
			dx -= .2;
			if(dx < 0){
				dx = 0;
			}
			dy = gravity;
		}
	}
	public int getHealth(){
		return health;
	}
	public int getMaxHealth(){
		return maxHealth;
	}
	public void takeDamage(int damage){
		if(!dead){
			health -= damage;
		}
		if(health < 0){
			health = 0;
		}
		if(health == 0){
			dead = true;	
		}
	}
	public void update(){
		checkNextPosition();
		checkCollision();
		moveEntity(tempx, tempy);
		
		// reverse directions when colliding with a wall
		if(right && dx == 0){
			right = false;
			left = true;
		}
		else if(left && dx == 0){
			left = false;
			right = true;
		}
				
	}
	public void draw(Graphics2D g){
		setMapLocation();
		
		g.setColor(Color.YELLOW);
		super.draw(g);
	}

}
