package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/* originally Player contained all these fields, but I wanted an entity class to inherit from
 * so I copied it all from Player and pasted it into 
*/ 
public abstract class Entity {
	
	// tilemap stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double tilex;
	protected double tiley;
	
	// position and change in position
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// center x and y for hit box
	protected double centerx;
	protected double centery;
	
	// size 
	protected int width;
	protected int height;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean falling;
	protected boolean jumping;
	protected boolean running;
	
	// enemies more than likely can't jump, but in case they can, this is in here
	protected int jumpCount = 2;
	
	// movement
	protected double speed;
	protected double jumpSpeed;
	protected double maxFallSpeed;
	protected double gravity;
	
	// collisions
	protected int cWidth;
	protected int cHeight;
	
	// booleans for corner collision
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean botLeft;
	protected boolean botRight;
	
	// variables used in moving
	protected double destx;
	protected double desty;
	protected double tempx;
	protected double tempy;
	protected int currentRow;
	protected int currentCol;
	
	public Entity(TileMap tile){
		tileMap = tile;
		tileSize = tile.getTileSize();
		
		centerx = width / 2;
		centery = height / 2;
	}
	
	// checks if two rectangles (hitboxes) collide with each other
	public boolean intersects(Entity e){
		Rectangle r1 = getRectangle();
		Rectangle r2 = e.getRectangle();
		return r1.intersects(r2);
	}
	// making hitboxes
	public Rectangle getRectangle(){
		return new Rectangle((int)x - cWidth, (int)y - cHeight, cWidth, cHeight);
	}
	
	// collision using corners
	private void cornerCollision(double x, double y){
		int leftTile = tileMap.getCol((int)(x - centerx));
		int rightTile = tileMap.getCol((int)(x + centerx) - 1);
		int topTile = tileMap.getRow((int)(y - centery));
		int botTile = tileMap.getRow((int)(y + centery) - 1);
		topLeft = tileMap.getTile(topTile, leftTile) == 0;
		topRight = tileMap.getTile(topTile, rightTile) == 0;
		botLeft = tileMap.getTile(botTile, leftTile) == 0;
		botRight = tileMap.getTile(botTile, rightTile) == 0;
	}
	
	public void checkCollision(){
		currentCol = tileMap.getCol((int)x);
		currentRow = tileMap.getRow((int)y);
		
		destx = x + dx;
		desty = y + dy;
		
		tempx = x;
		tempy = y;
		
		cornerCollision(x, desty);
		if(dy < 0){ // if moving up (jumping)
			if(topLeft || topRight){
				dy = 0;
				tempy = currentRow * tileMap.getTileSize() + centery;
			}
			else {
				tempy += dy;
			}
		}
		if(dy > 0){ // if moving down (falling)
			if(botLeft || botRight){ 
				dy = 0;
				falling = false;
				tempy = (currentRow + 1) * tileMap.getTileSize() - centery;
			}
			else{ //freely move on y axis
				tempy+= dy;
			}
		}
		
		cornerCollision(destx, y);
		if(dx < 0){ // if moving left
			if(topLeft || botLeft){
				dx = 0;
				tempx = currentCol * tileMap.getTileSize() + centerx;
				if(right){
					jumpCount = 1;
				}
			}
			else{
				tempx += dx;
			}
		}
		if(dx > 0){ // if moving right
			if(topRight || botRight){
				dx = 0;
				tempx = (currentCol + 1) * tileMap.getTileSize() - centerx;
				if(left){
					jumpCount = 1;
				}
			}
			else{ // freely move
				tempx += dx;
			}
		}
		if(!falling){
			cornerCollision(x, y + 1); // y + 1 is the pixel right below the player
			if(!botLeft && !botRight){
				falling = true;
			}
		}
	}
	public int getx(){
		return (int)x;
	}
	public int gety(){
		return (int)y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public int getCWidth(){ // collision width
		return cWidth;
	}
	public int getCHeight(){ // collision height
		return cHeight;
	}
	
	public void moveEntity(double x, double y){
		this.x = x;
		this.y = y;
	}
	public void setDirection(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	public void setMapLocation(){
		tilex = tileMap.getx();
		tiley = tileMap.gety();

	}
	public void setLeft(boolean bool){
		left = bool;
	}
	public void setRight(boolean bool){
		right = bool;
	}
	public void setRunning(boolean bool){
		running = bool;
	}
	public void setJumping(){
		if(!falling){
			jumping = true;
		}
		else if(falling){
			if(jumpCount > 0){
				jumping = true;
			}
		}
	}
	
	public void draw(Graphics2D g){
		g.fillRect((int)(tilex + x - centerx), (int)(tiley + y - centery), width, height);
	}
	
}
