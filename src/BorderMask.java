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
		g.drawRect(x, y, W, H);
	}
	boolean isTouching(BorderMask b){	
		  if ((x < b.x + b.W && x + W > b.x && y < b.y + b.H && y + H > b.y)){
			  return true;
		  }
		  return false;
	}
}
