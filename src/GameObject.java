import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
	double x,y,dx,dy;
	Rectangle r = new Rectangle();
	protected GameObject(double x, double y){
		this.x = x ;
		this.y = y;
	}
	double getX(){
		return x;
	}
	double getY(){
		return y;
	}
	double getDX(){
		return dx;
	}
	double getDY(){
		return dy;
	}
	void setX(double i){
		x+=i;
	}
	void setY(double i){
		y+=i;
	}
	void setDX(double i){
		dx = i;
	}
	void setDY(double i){
		dy = i;
	}
	void move(){
		x += dx;
		y += dy;
	}
	abstract Rectangle getBounds();
	abstract void draw(Graphics g);
	
}
