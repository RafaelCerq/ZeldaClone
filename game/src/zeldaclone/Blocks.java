package zeldaclone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blocks extends Rectangle {
	
	public Blocks(int x, int y) {
		super(x, y, 32, 32);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setColor(Color.WHITE);
		g.drawRect(x, y, width, height);
		
		// Utilizar sprite
		// g.drawImage(SpriteSheet.tileWall, x, y, 32, 32, null);
		
	}

}
