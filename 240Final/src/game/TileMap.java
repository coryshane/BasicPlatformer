package game;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import game.Tile;

public class TileMap {

	private int x;
	private int y;
	
	private int mapWidth;
	private int mapHeight;
		
	private int tileSize;
	private int[][] map;
	
	// minimum and max for camera purposes. 
	private int minx;
	private int miny;
	private int maxx = 0;
	private int maxy = 0;

	private int numTiles;	
	private BufferedImage tiles;
	private Tile[] block;

	//Color transparent = new Color(0, 0, 0, 0);
	
	// CONSTRUCTOR
	public TileMap(String mapFile, int tileSize){
		this.tileSize = tileSize;

		try{
			InputStream is = getClass().getResourceAsStream(mapFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			mapHeight= Integer.parseInt(br.readLine());
			mapWidth = Integer.parseInt(br.readLine());
			
			map = new int[mapHeight][mapWidth];
			
			// these formulas I found on a tutorial to keep the camera centered
			minx = GamePanel.WIDTH - mapWidth * tileSize;
			miny = GamePanel.HEIGHT - mapHeight * tileSize;
			
			String delimiter = "\\s+"; // uses all spaces as delimiters.
			for(int row = 0; row < mapHeight; row++){
				String line = br.readLine();
				String[] temp = line.split(delimiter);
				for(int col = 0; col < mapWidth; col++){
					map[row][col] = Integer.parseInt(temp[col]);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	// load the tiles from files. Have to convert from BufferedImage to tile
	public void loadTiles(String s){ 
		try{
			tiles = ImageIO.read(getClass().getResourceAsStream(s));
			numTiles = tiles.getWidth() / tileSize;
			
			block = new Tile[numTiles];
			
			BufferedImage sub; 
			for(int col = 0; col < numTiles; col++){
				// getSubimage(x, y, w, h) -> x would be the column times the tileSize for each, 0 for y since the row doesn't change
				sub = tiles.getSubimage(col * tileSize, 0, tileSize, tileSize);
				block[col] = new Tile(sub);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setx(int i){
		x = i;
		if(x < minx){
			x = minx;
		}
		if(x > maxx){
			x = maxx;
		}
	}
	public void sety(int i){
		y = i;
		if(y < miny){
			y = miny;
		}
		if(y > maxy){
			y = maxy; 
		}
	}
	public int getx(){
		return x;
	}
	public int gety(){
		return y;
	}
	public int getCol(int x){
		return x / tileSize;
	}
	public int getRow(int y){
		return y / tileSize;
	}
	public int getTile(int row, int col){
		return map[row][col];		
	}
	public int getTileSize(){
		return tileSize;
	}
	public void update(){
		// Doesn't need to update 
	}
	public void draw(Graphics2D g){
		for(int row = 0; row < mapHeight; row++){ // for each row
			for(int col = 0; col < mapWidth; col++){ // for each col
				int loc = map[row][col]; // current location
	
				if(loc == 0){
					g.drawImage(block[0].getImage(), (int)x + col * tileSize,(int)y + row * tileSize, null);
				}
				else if(loc == 1){
					g.setColor(Color.BLACK);
				}
				else if(loc == 2){ // blocks that look solid, but you can pass through
					g.drawImage(block[0].getImage(), (int)x + col * tileSize,(int)y + row * tileSize, null);
				}
				else if(loc == 3){ // powerup marker
					g.drawImage(block[1].getImage(), (int)x + col * tileSize,(int)y + row * tileSize, null);
				}
				else if(loc == 4){ // the current goal marker
					g.drawImage(block[2].getImage(), (int)x + col * tileSize,(int)y + row * tileSize, null);
				}
				else if(loc == 5){ // unused block, half red, half black
					g.drawImage(block[3].getImage(), (int)x + col * tileSize,(int)y + row * tileSize, null);
				}
			}
		}
	}
}
