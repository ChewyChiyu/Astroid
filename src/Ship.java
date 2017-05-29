import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Ship extends GameObject{
	final int W = Texture.ship.getWidth()*2;
	final int H = Texture.ship.getHeight()*2;
	final int SPEED = 9;
	final double ACCEL = .02;
	final double FRICTION = ACCEL;
	protected double angle = 0;

	boolean angleShiftR = false;
	boolean angleShiftL = false;
	boolean thrust = false;
	boolean shooting = true;
	protected Ship(int x, int y) {
		super(x, y);
		r.setSize(W, H);
		Thread motion = new Thread(new Runnable(){
			public void run(){
				while(this!=null){


					if(angleShiftR){
						angle+=Math.PI/500;
						if(angle>=Math.PI*2){
							angle = 0;
						}
					}
					if(angleShiftL){
						angle-=Math.PI/500;
						if(angle<=-Math.PI*2){
							angle = 0;
						}
					}

					if(thrust){
						if(dx!=SPEED*Math.sin(angle)){
							if(dx>SPEED*Math.sin(angle))
								dx-=ACCEL;
							else
								dx+=ACCEL;
						}
						if(dy!=-SPEED*Math.cos(angle)){
							if(dy>-SPEED*Math.cos(angle))
								dy-=ACCEL;
							else
								dy+=ACCEL;
						}
						
					}else{
						if(dx!=0){
							if(dx>0)
								dx-=FRICTION;
							else
								dx+=FRICTION;
						}
						if(dy!=0){
							if(dy>0)
								dy-=FRICTION;
							else
								dy+=FRICTION;
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
	void shoot(){
		AstroidScreen.sprites.add(new Projectile(x+W/2,y+(H*.3),angle,SPEED*Math.sin(angle),-SPEED*Math.cos(angle)));
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
	@Override
	Rectangle getBounds() {
		r.setLocation((int)x, (int)y);
		return r;
	}

}
