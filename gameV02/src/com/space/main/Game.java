package com.space.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.space.entities.BulletShoot;
import com.space.entities.Enemy;
import com.space.entities.Entity;
import com.space.entities.Player;
import com.space.graficos.Spritesheet;
import com.space.graficos.UI;
import com.space.world.Camera;
import com.space.world.World;


public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	// Variables
	public static JFrame frame;
	private boolean isRunning = true;
	private Thread thread;
	public static final int WIDTH = 240, HEIGHT = 160, SCALE = 3;

	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	private Graphics g;


	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;

	public static World world;
	public static Player player;
	
	public static Random random;
	
	public UI ui;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public Menu menu;

	// private Graphics2D g2;
	/***/

	// Construtor
	public Game() {
		Sound.musicBackground.loop();
		random = new Random();
		// Para que os eventos de teclado funcionem
		addKeyListener(this);
		addMouseListener(this);

		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		// Inicializando Objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets =  new ArrayList<BulletShoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		menu = new Menu();
	}

	// Criação da Janela
	public void initFrame() {
		frame = new JFrame("New Window");
		frame.add(this);
		frame.setResizable(false);// Usuário não irá ajustar janela
		frame.pack();
		frame.setLocationRelativeTo(null);// Janela inicializa no centro
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Fechar o programa por completo
		frame.setVisible(true);// Dizer que estará visível
	}

	// Threads
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public void tick() {
		if (gameState == "NORMAL") {
			restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}
			
			if(enemies.size() <= 0) {
				//System.out.println("Next Level");
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}			
		} else if (gameState == "GAME_OVER") {
			
			// Piscar mensagem de comando na tela de game over
			framesGameOver++;
			if (framesGameOver == 30) {
				framesGameOver = 0;
				if (showMessageGameOver) {
					showMessageGameOver = false;
				} else {
					showMessageGameOver = true;
				}
			}
			
			if (restartGame) {
				restartGame = false;
				gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
		} else if (gameState == "MENU") {
			menu.tick();
		}
	}

	// O que ser� mostrado em tela
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();// Sequência de buffer para otimizar a renderização, lidando com performace gráfica
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		g = image.getGraphics();// Renderizar imagens na tela
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

		/* Render do jogo */
		// g2 = (Graphics2D) g; //Transformei em um tipo g e foi feito um cast com o
		// Graphics2D
		
		world.render(g);
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		ui.render(g);

		/***/

		g.dispose();// Limpar dados de imagem não usados
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.ammo, 580, 20);
		
		if (gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 36));
			g.setColor(Color.white);
			g.drawString("Game Over", (WIDTH * SCALE) / 2 - 90, (HEIGHT * SCALE) / 2 - 20);
			g.setFont(new Font("arial", Font.BOLD, 30));
			if (showMessageGameOver) {
				g.drawString("> Pressione ENTER para reiniciar <", (WIDTH * SCALE) / 2 - 210, (HEIGHT * SCALE) / 2 + 40);				
			}
		} else if (gameState == "MENU") {
			menu.render(g);
		}
		
		bs.show();
	}

	// Controle de FPS
	public void run() {
		// Variables
		long lastTime = System.nanoTime();// Usa o tempo atual do computador em nano segundos, bem mais preciso
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;// Calculo exato de Ticks
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		// Ruuner Game
		while (isRunning == true) {
			// System.out.println("My game is Running");
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}

		stop(); // Garante que todas as Threads relacionadas ao computador foram terminadas, para garantir performance.

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
		
		// Esquerda e Direita
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		// Cima e Baixo
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if (gameState == "MENU") {
				this.menu.up = true;
			}

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if (gameState == "MENU") {
				this.menu.down = true;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
		
//		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//			restartGame = true;
//		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Esquerda e Direita
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;

		}

		// Cima e Baixo
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false	;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if (gameState == "MENU") {
				this.menu.enter = true;
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() / 3);
		player.my = (e.getY() / 3);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
