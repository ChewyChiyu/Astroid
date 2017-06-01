import java.awt.Color;
import java.awt.Graphics;

public class BorderMask {
	private int x,y;
	private int W, H;
	
	void setSize(int w, int h){
		W = w;
		H = h;
	}
	void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	
	void draw(Graphics g){
		g.setColor(Color.WHITE);
		g.drawOval(x, y, W, H);
	}
	boolean isTouching(BorderMask b){	
			double deltaX = (x+W/2)-(b.x+b.W/2);
			double deltaY = (y+H/2)-(b.y+b.H/2);
			double distanceSquared = deltaX * deltaX + deltaY * deltaY;
			boolean collision = distanceSquared < (W/2 + b.W/2) * ((W/2 + b.W/2));
			return collision;

	}
}
