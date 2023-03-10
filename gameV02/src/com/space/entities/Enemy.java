package com.space.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.space.main.Game;
import com.space.world.Camera;
import com.space.world.World;

public class Enemy extends Entity {

	private double speed = 0.4;

	private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;
	private int frames = 0, maxFrames = 25, index = 0, maxIndex = 1;
	private BufferedImage[] sprites;
	private int life = 10;
	private boolean isDamage = false;
	private int damageFrames = 10, damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);

		// Frames de Animação
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	}

	public void tick() {
		if (this.isColliddingWithPlayer() == false) {

			if ((int)x < Game.player.getX() && World.isFree((int)(x + speed), this.getY())
					&& !isCollidding((int)(x + speed), this.getY())) {
				x += speed;
			} else if ((int)x > Game.player.getX() && World.isFree((int)(x - speed), this.getY())
					&& !isCollidding((int)(x - speed), this.getY())) {
				x -= speed;
			}

			if ((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed))
					&& !isCollidding(this.getX(), (int)(y + speed))) {
				y += speed;
			} else if ((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y - speed))
					&& !isCollidding(this.getX(), (int)(y - speed))) {
				y -= speed;
			}
		} else {
			// Estamos colidindo
			if (Game.random.nextInt(100) < 10) {
				Game.player.life -= Game.random.nextInt(3);
				Game.player.isDamaged = true;
			}
		}

		// Animation
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}

		collidingBullet();
		if (life <= 0) {
			destroySelf();
			return;
		}
		
		if(isDamage) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamage = false;
			}
		}
	}

	public void destroySelf() {
		Game.entities.remove(this);
	}

	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if (e instanceof BulletShoot) {
				if (Entity.isCollidding(this, e)) {
					isDamage = true;
					life--;
					Game.bullets.remove(i);
					return;
				}
			}
		}
	}

	public boolean isColliddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskW, maskH);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemyCurrent.intersects(player);
	}

	public boolean isCollidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskX, ynext + maskY, maskW, maskH); // Classe que cria retangulos fictícios para testar colisões.
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);

			if (e == this) {
				continue;
			}
//			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}

		}

		return false;
	}

	public void render(Graphics g) {
		if(!isDamage) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
	}
}
