package game;

import java.awt.*;

// class does NOT work currently
// credit for most of the structure goes to Andy
public class Bullet extends Entity{

	
	private int speed = 5;
	
	Bullet(TileMap tile){
		super(tile);
	}
	public void fire(int i, int j, boolean bool){
		height = 10;
		width = 10;
		cWidth = 5;
		cHeight = 5;
		
		setPosition(i, j);
		setDirection(bool);
		System.out.println("SHOOT");
	}
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setDirection(boolean bool){
		if(bool){
			dx = speed;
		}
		else if(!bool){
			dx = -speed;
		}
	}
	public void update(){
		x += dx;
		
	}
	public void draw(Graphics2D g){
		g.setColor(Color.RED);
		super.draw(g);
	}

}
