package com.space.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {

	public String[] options = { "novo jogo", "carregar jogo", "sair" };
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	public boolean up, down, enter;
	public boolean pause = false;

	public void tick() {
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}

		if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}

		if (enter) {
			enter = false;
			if (options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			} else if (options[currentOption] == "sair") {
				System.exit(1);
			}
		}
	}

	public void render(Graphics g) {
		// Background do Menu
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 190));
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

		// T�tulo do Menu
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString("> Game 01 <", (Game.WIDTH * Game.SCALE) / 2 - 105, (Game.HEIGHT * Game.SCALE) / 2 - 150);

		// Op��es do Menu
		g.setFont(new Font("arial", Font.BOLD, 24));
		if (!pause) {
			g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 105, 200);
		} else {
			g.drawString("Continuar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 105, 200);
		}

		g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 105, 250);
		g.drawString("Sair do Jogo", (Game.WIDTH * Game.SCALE) / 2 - 105, 300);

		// Dire��o do Menu
		if (options[currentOption] == "novo jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 150, 200);
		} else if (options[currentOption] == "carregar jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 150, 250);
		} else if (options[currentOption] == "sair") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 150, 300);
		}
	}
}
