import java.awt.Graphics;
import java.awt.Image;

public class Enemy extends GameObject{
	protected final int W = Texture.enemy.getWidth();
	protected final int H = Texture.enemy.getHeight();

	protected Enemy(double x, double y) {
		super(x, y);
		r.setSize(W, H);
		dx = 1;
		dy = 1;
	}


	@Override
	void draw(Graphics g) {
		g.drawImage(Texture.enemy.getScaledInstance(W, H, Image.SCALE_DEFAULT), (int)x, (int)y, null);
	}

}
