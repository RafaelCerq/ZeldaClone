package com.space.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.space.entities.Entity;
import com.space.entities.Player;
import com.space.graficos.Spritesheet;


public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	//Variables
	public static JFrame frame;
	private boolean isRunning = true;
	private Thread thread;
	private final int WIDTH = 240, HEIGHT = 160, SCALE = 3;
	
	private BufferedImage image;
	
	public List<Entity> entities;
	public Spritesheet spritesheet;
	
	private Graphics g;
	//private Graphics2D g2;
	/***/
	
	//Construtor
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//Inicializando Objetos
		image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		
		Player player = new Player(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
	}
	
	//Cria��o da Janela
	public void initFrame() {
		frame = new JFrame("New Window");
		frame.add(this);
		frame.setResizable(false);//Usu�rio n�o ir� ajustar janela
		frame.pack();
		frame.setLocationRelativeTo(null);//Janela inicializa no centro
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Fechar o programa por completo
		frame.setVisible(true);//Dizer que estar� vis�vel
	}
	
	//Threads
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e instanceof Player) {
				//Ticks do Player
			}
			e.tick();
		}
	}
	
	//O que ser� mostrado em tela
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();//Sequ�ncia de buffer para otimizar a renderiza��o, lidando com performace gr�fica
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		g = image.getGraphics();//Renderizar imagens na tela
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		
		/*Render do jogo*/
		//g2 = (Graphics2D) g; //Transformei em um tipo g e foi feito um cast com o Graphics2D
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			
			e.render(g);
		}
		
		/***/
		
		g.dispose();//Limpar dados de imagem n�o usados
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE,null);
		bs.show();
	}
	
	//Controle de FPS
	public void run() {
		//Variables
		long lastTime = System.nanoTime();//Usa o tempo atual do computador em nano segundos, bem mais preciso
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;//Calculo exato de Ticks
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		//Ruuner Game
		while (isRunning == true) {
			//System.out.println("My game is Running");
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick(); render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop(); //Garante que todas as Threads relacionadas ao computador foram terminadas, para garantir performance.
		
	}
}
