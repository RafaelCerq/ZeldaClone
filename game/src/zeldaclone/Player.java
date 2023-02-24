package zeldaclone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Rectangle {
	
	public int spd = 4;
	public boolean right, up, down, left;
	
	public Player(int x, int y) {
		super(x, y, 32, 32);
	}
	
	public void tick() {
		if (right && World.isFree(x+spd, y)) {
			x+=spd;
		} else if (left && World.isFree(x-spd, y)) {
			x-=spd;
		}
		
		if (up && World.isFree(x, y-spd)) {
			y-=spd;
		} else if (down && World.isFree(x, y+spd)) {
			y+=spd;
		}
	}
	
	public void render(Graphics g) {
//		g.setColor(Color.blue);
//		g.fillRect(x, y, width, height);
		g.drawImage(SpriteSheet.player_front, x, y, 32, 32, null);
	}

}
