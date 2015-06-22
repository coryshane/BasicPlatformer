package game;

import java.awt.*;

public class BossEnemy extends Enemy{

	private boolean dead;
	private int health;
	private int maxHealth;

	// CONSTRUCTORS
	public BossEnemy(TileMap tile){
		super(tile);
		
		width = 68;
		height = 68;
		
		// collision
		cWidth = 68;
		cHeight = 68;
		centerx = width / 2;
		centery = height / 2;

		speed = 2;
		health = 5;
		maxHealth = 5;
		gravity = 1;
		right = true;
	}
	public boolean isDead(){
		return dead;
	}

	public void getNextPosition(){
		super.checkNextPosition();
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
		super.update();
	}
	public void draw(Graphics2D g){
		setMapLocation();
		super.draw(g);
	}
}
