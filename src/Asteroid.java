import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Asteroid extends GameObject{
	protected int W;
	protected int H;
	protected int type;
	boolean big;
	protected BufferedImage img;
	double angle = 0;
	protected Asteroid(double x, double y, double dx, double dy, boolean big, BufferedImage img) {
		super(x, y);
		this.big = big;
		this.img = img;
		this.dx = dx;
		this.dy = dy;
		if(big){
			//max is like 2
			if(dx+dy<=.3){
				W = (int) (img.getWidth()*1.2);
				H = (int) (img.getHeight()*1.2);
			}else{
				W = (int) (img.getWidth()*.7);
				H = (int) (img.getHeight()*.7);
		    }
		
		}
		Thread motion = new Thread(new Runnable(){
			public void run(){
				while(this!=null){
					if((angle+=Math.PI/3000) > Math.PI*2){
						angle = 0;
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
		g2d.drawImage(img, -W/2 ,-H/2, W, H, null);
		g2d.setTransform(a);
	}
	
}
