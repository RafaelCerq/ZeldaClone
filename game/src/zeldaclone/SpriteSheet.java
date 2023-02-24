package zeldaclone;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	public static BufferedImage spritesheet;
	
	public static BufferedImage player_front;

	public static BufferedImage tileWall;
	
	
	public SpriteSheet() {
		try {
			spritesheet = ImageIO.read(getClass().getResource("/spritesheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player_front = SpriteSheet.getSprite(0, 11, 16, 16);
//		tileWall = SpriteSheet.getSprite(200, 221, 16, 16);
	}

	public static BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}
}
