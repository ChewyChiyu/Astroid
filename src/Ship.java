import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Ship extends GameObject{
	final int W = Texture.ship.getWidth()*2;
	final int H = Texture.ship.getHeight()*2;
	final int SPEED = 10;
	protected double angle = 0;

	boolean angleShiftR = false;
	boolean angleShiftL = false;
	boolean thrust = false;

	protected Ship(int x, int y) {
		super(x, y);
		Thread motion = new Thread(new Runnable(){
			public void run(){
				while(this!=null){


					if(angleShiftR){
						angle+=Math.PI/500;
						if(angle>=360){
							angle = 0;
						}
					}
					if(angleShiftL){
						angle-=Math.PI/500;
						if(angle<=-360){
							angle = 0;
						}
					}

					if(thrust){
						if(dx!=SPEED*Math.sin(angle)){
							if(dx>SPEED*Math.sin(angle))
								dx-=.02;
							else
								dx+=.02;
						}
						if(dy!=-SPEED*Math.cos(angle)){
							if(dy>-SPEED*Math.cos(angle))
								dy-=.02;
							else
								dy+=.02;
						}
						
					}else{
						if(dx!=0){
							if(dx>0)
								dx-=.02;
							else
								dx+=.02;
						}
						if(dy!=0){
							if(dy>0)
								dy-=.02;
							else
								dy+=.02;
						}
					}

					try{
						Thread.sleep(1);
					}catch(Exception e) { }
				}
			}
		});
		motion.start();
	}

	@Override
	void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform a = g2d.getTransform();
		g2d.translate(x + W/2, y + H/2);
		g2d.rotate(angle);
		g2d.drawImage(Texture.ship.getScaledInstance(Texture.ship.getWidth()*2, Texture.ship.getHeight()*2, Image.SCALE_DEFAULT),  - W/2,  - H/2, null);
		g2d.setTransform(a);
	}


}
