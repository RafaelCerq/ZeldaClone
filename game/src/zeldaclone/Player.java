package zeldaclone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Rectangle {
	
	public int spd = 4;
	public boolean right, up, down, left;
	
	public int curAnimation = 0;
	public int curFrames = 0, targetFrames = 15;
	
	public Player(int x, int y) {
		super(x, y, 32, 32);
	}
	
	public void tick() {
		boolean moved = false;
		if (right && World.isFree(x+spd, y)) {
			x+=spd;
			moved = true;
		} else if (left && World.isFree(x-spd, y)) {
			x-=spd;
			moved = true;
		}
		
		if (up && World.isFree(x, y-spd)) {
			y-=spd;
			moved = true;
		} else if (down && World.isFree(x, y+spd)) {
			y+=spd;
			moved = true;
		}
		
		if (moved) {
			curFrames++;
			if (curFrames == targetFrames) {
				curFrames = 0;
				curAnimation++;
				if (curAnimation == SpriteSheet.player_front.length) {
					curAnimation = 0;
				}
			}			
		}
	}
	
	public void render(Graphics g) {
//		g.setColor(Color.blue);
//		g.fillRect(x, y, width, height);
		g.drawImage(SpriteSheet.player_front[curAnimation], x, y, 32, 32, null);
	}

}
