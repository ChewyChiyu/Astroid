import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
@SuppressWarnings("serial")
public class AstroidScreen extends JPanel implements Runnable{
	Thread game;
	boolean gameLoop;
	final int W = Toolkit.getDefaultToolkit().getScreenSize().width;
	final int H = Toolkit.getDefaultToolkit().getScreenSize().height;
	final int MAX_ASTEROID = 30;
	int livesLeft = 5;
	Ship player = new Ship(W/2,H/2);
	protected static ArrayList<GameObject> sprites = new ArrayList<GameObject>();

	protected AstroidScreen(){
		panel();
		add();
		keys();
		start();
	}
	void keys(){
		getInputMap().put(KeyStroke.getKeyStroke("A"), "A");
		getInputMap().put(KeyStroke.getKeyStroke("released A"), "rA");

		getInputMap().put(KeyStroke.getKeyStroke("D"), "D");
		getInputMap().put(KeyStroke.getKeyStroke("released D"), "rD");

		getInputMap().put(KeyStroke.getKeyStroke("W"), "W");
		getInputMap().put(KeyStroke.getKeyStroke("released W"), "rW");

		getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "SPACE");
		getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "rSPACE");

		getActionMap().put("A", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.angleShiftL = true;
			}

		});
		getActionMap().put("rA", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.angleShiftL = false;
			}

		});
		getActionMap().put("D", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.angleShiftR = true;
			}

		});
		getActionMap().put("rD", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.angleShiftR = false;
			}

		});
		getActionMap().put("W", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.thrust = true;
			}

		});
		getActionMap().put("rW", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.thrust = false;
			}

		});
		getActionMap().put("SPACE", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(player.shooting && sprites.contains(player)){
					player.shooting = false;
					player.shoot();
				}
			}

		});
		getActionMap().put("rSPACE", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.shooting = true;
			}

		});
	}
	void add(){
		sprites.add(player);
		//sprites.add(new Asteroid(W/2,H/2,Math.random()+.1,Math.random()+.1,true,Texture.asteroidBig1));
	}
	synchronized void start(){
		game = new Thread(this);
		gameLoop = true;
		game.start();
		manageThread();
	}
	void manageThread(){
		Thread manage = new Thread(new Runnable(){
			public void run(){
				while(gameLoop){
					//border
					synchronized(sprites){
						for(int index = 0; index < sprites.size(); index++){
							GameObject o = sprites.get(index);	
							if(o instanceof Projectile || o instanceof Asteroid){
								if(o.getX() < -100 || o.getX() > W+100 || o.getY() < -100 || o.getY() > H+100){
									sprites.remove(o);
								}
							}

							if(o instanceof Ship){
								if(o.getX()<0){
									o.x = W;
								}
								if(o.getX()>W){
									o.x = 0;
								}
								if(o.getY()<0){
									o.y = H;
								}
								if(o.getY()>H){
									o.y = 0;
								}
							}
						}
					}
					//System.out.println(sprites.size());

					try{
						Thread.sleep(1);
					}catch(Exception e) { }
				}
			}
		});
		Thread addAsteroid = new Thread(new Runnable(){
			public void run(){
				while(gameLoop){
					if(numberOfAsteroids()<=MAX_ASTEROID){
						for(int index = 0; index < 3; index++){

							BufferedImage img = null;
							int initialX = 0, initialY = 0; double initialDX = 0, initialDY = 0;
							switch((int)(Math.random()*3)){
							case 0:
								img = Texture.asteroidBig1;
								break;
							case 1:
								img = Texture.asteroidBig2;
								break;
							case 2:
								img = Texture.asteroidBig3;
								break;
							}
							switch((int)(Math.random()*4)){
							case 0:
								//top
								initialY = 1;
								initialX = (int)(Math.random()*W);
								initialDX = ((int)(Math.random()*2)==1)?Math.random()*.1:-Math.random()+.1;
								initialDY = Math.random()+.1;
								break;
							case 1:
								//floor
								initialY = H-1;
								initialX = (int)(Math.random()*W);
								initialDX = ((int)(Math.random()*2)==1)?Math.random()*.1:-Math.random()+.1;
								initialDY = -Math.random()+.1;
								break;
							case 2:
								//left side
								initialY = (int)(Math.random()*H);
								initialX = 1;
								initialDX = Math.random()+.1;
								initialDY = ((int)(Math.random()*2)==1)?Math.random()*.1:-Math.random()+.1;
								break;
							case 3:
								//right side
								initialY = (int)(Math.random()*H);
								initialX = W-1;
								initialDX = -Math.random()-.1;
								initialDY = ((int)(Math.random()*2)==1)?Math.random()*.1:-Math.random()+.1;
								break;
							}
							boolean big;
							if(initialDX+initialDY<=.3){
								big = true;
							}else{
								big = false;
							}
							sprites.add(new Asteroid(initialX,initialY,initialDX,initialDY,big,img));


						}
						try{
							Thread.sleep(2000);
						}catch(Exception e) { }
					}
				}
			}
		});
		Thread boundCheck = new Thread(new Runnable(){
			public void run(){
				while(gameLoop){
					synchronized(sprites){
						for(int index = 0; index < sprites.size(); index++){
							if(sprites.get(index) instanceof Asteroid){
								Asteroid o = (Asteroid) sprites.get(index);
								//asteroid bounce check
								for(int index2 = 0; index2 < sprites.size(); index2++){
									if(sprites.get(index2) instanceof Asteroid){
										Asteroid o2 = (Asteroid) sprites.get(index2);
										if(o.equals(o2)){
											continue;
										}
										if(o.getBounds().intersects(o2.getBounds()) || o2.getBounds().intersects(o.getBounds()) ){		
											if (o.big&&!o2.big){
												o2.setDX(o2.getDX()+(o.getDX()*.5));
												o2.setDY(o2.getDY()+(o.getDY()*.5));
												o.setDX(o.getDX()*.5);
												o.setDY(o.getDY()*.5);
											}else{
												o2.setDX(o2.getDX()+(o.getDX()*.4));
												o2.setDY(o2.getDY()+(o.getDY()*.4));
												o.setDX(o.getDX()*.6);
												o.setDY(o.getDY()*.6);
											}
										}
									}
								}
								
								//check for player hit
								if(o.getBounds().intersects(player.getBounds())){
								sprites.remove(player);
								if(livesLeft-->0)
								respawn();
								}
							}
							
							
							//laser detection
							if(sprites.get(index) instanceof Projectile){
								Projectile p = (Projectile) sprites.get(index);
								for(int index2 = 0; index2 < sprites.size(); index2++){
									if(sprites.get(index2) instanceof Asteroid){
										Asteroid a = (Asteroid) sprites.get(index2);
										if(p.getBounds().intersects(a.getBounds())){
											sprites.remove(p);
											if(a.big){
											sprites.add(new Asteroid(a.getX()+a.W/2,a.getY(),a.getDX(),a.getDY(),false,a.img));
											sprites.add(new Asteroid(a.getX(),a.getY(),a.getDX(),a.getDY(),false,a.img));
											}
											sprites.remove(a);
											
										}
									}
								}
							}
							
							
							
							
						}
					}



					try{
						Thread.sleep(1);
					}catch(Exception e) { }
				}
			}
		});
		boundCheck.start();
		addAsteroid.start();
		manage.start();
	}
	void respawn(){
		player = new Ship(W/2,H/2);
		Rectangle spawnRegion = new Rectangle((int)(W*.3),(int)(H*.3),(int)(W*.3),(int)(H*.3));
		synchronized(sprites){
			for(int index = sprites.size()-1; index >= 0; index--){
				if(sprites.get(index) instanceof Asteroid){
				if(sprites.get(index).getBounds().intersects(spawnRegion.getBounds())){
					sprites.remove(index);
				}
				}
			}
		sprites.add(player);

		}
	}
	void panel(){
		JFrame frame = new JFrame();
		frame.add(this);
		frame.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		setBackground(Color.BLACK);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



	}
	public void run(){
		while(gameLoop){
			update();
			try{
				Thread.sleep(10);
			}catch(Exception e) { }
		}
	}
	void update(){
		move();
		repaint();
	}
	
	int numberOfAsteroids(){
		int index = 0;
		for(int a = 0; a < sprites.size(); a++){
			GameObject o = sprites.get(a);
			if(o instanceof Asteroid){
				index++;
			}
		}
		return index;
	}
	void move(){
		for(int index = 0; index < sprites.size(); index++){
			GameObject o = sprites.get(index);
			o.move();
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		
		int xBuffer = 10;
		for(int index = 0; index < livesLeft; index++){
			g.drawImage(Texture.ship.getScaledInstance(Texture.ship.getWidth()*2, Texture.ship.getHeight()*2, Image.SCALE_DEFAULT), xBuffer+=Texture.ship.getWidth()*2, 10,Texture.ship.getWidth()*2,Texture.ship.getHeight()*2, null);
		}
		
		for(int index = 0; index < sprites.size(); index++){
			GameObject o = sprites.get(index);
			o.draw(g);
		}
	}
}
