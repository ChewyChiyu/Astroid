import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Projectile extends GameObject{
	double angle;
	protected Projectile(double x, double y, double angle, double dx, double dy) {
		super(x, y);
		this.angle = angle;
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform a = g2d.getTransform();
		g2d.translate(x, y);
		g2d.rotate(angle);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 3, 10);
		g2d.setTransform(a);
	}

}
