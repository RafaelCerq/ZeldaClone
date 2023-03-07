package com.space.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.space.entities.Player;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4, 70, 8);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int)((Player.life/Player.maxLife)*70), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,8));
		g.drawString((int)Player.life + "/" + (int)Player.maxLife, 30, 11);
	}
}
