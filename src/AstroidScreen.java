import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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
				player.shoot();
			}

		});
	}
	void add(){
		sprites.add(player);
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
							if(o instanceof Projectile){
								if(o.getX() < 0 || o.getX() > W || o.getY() < 0 || o.getY() > H){
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
		repaint();
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
