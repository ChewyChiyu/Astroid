import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
	final int MAX_ASTEROID = 15;
	double FRICTION = .02;
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
				if(player.shooting){
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
					if(numberOfAsteroids()>=MAX_ASTEROID){
						continue;
					}
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
		});
		addAsteroid.start();
		manage.start();
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
		bound();
		repaint();
	}
	void bound(){
		synchronized(sprites){
			for(int index = 0; index < sprites.size(); index++){
				GameObject o = sprites.get(index);
				if(o instanceof Asteroid){
					for(int index2 = 0; index2 < sprites.size(); index2++){
						GameObject o2 = sprites.get(index2);
						if(o.equals(o2)){
							continue;
						}
						if(o2 instanceof Asteroid){
							if(o.getBounds().intersects(o2.getBounds())){
								//space them apart first
								o.setX(-o.getDX());
								o2.setX(-o2.getDX());
								o.setY(-o.getDY());
								o2.setY(-o2.getDY());


								if(((Asteroid)o).big){
									FRICTION = .08;
								}else{
									FRICTION = .06;
								}

								if(o.getDX()!=0){
									if(o.getDX()>0){
										o.setDX(o.getDX()-FRICTION);
									}
									if(o.getDX()<0){
										o.setDX(o.getDX()+FRICTION);
									}
								}
								if(o.getDY()!=0){
									if(o.getDY()>0){
										o.setDY(o.getDY()-FRICTION);
									}
									if(o.getDY()<0){
										o.setDY(o.getDY()+FRICTION);
									}
								}
								if(((Asteroid)o2).big){
									FRICTION = .08;
								}else{
									FRICTION = .06;
								}

								if(o2.getDX()!=0){
									if(o2.getDX()>0){
										o2.setDX(o2.getDX()-FRICTION);
									}
									if(o2.getDX()<0){
										o2.setDX(o2.getDX()+FRICTION);
									}
								}
								if(o2.getDY()!=0){
									if(o2.getDY()>0){
										o2.setDY(o2.getDY()-FRICTION);
									}
									if(o2.getDY()<0){
										o2.setDY(o2.getDY()+FRICTION);
									}
								}

								if(((Asteroid) o2).big&&!((Asteroid) o).big){
									o.setDX(-o.getDX());
									o.setDY(-o.getDY());
								}else{
									o.setDX(-o.getDX()/2);
									o2.setDX(-o2.getDX()/2);
									o.setDY(-o.getDY());
									o2.setDY(-o2.getDY());

								}



							}
						}
					}
				}
			}
		}
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
		for(int index = 0; index < sprites.size(); index++){
			GameObject o = sprites.get(index);
			o.draw(g);
		}
	}
}
