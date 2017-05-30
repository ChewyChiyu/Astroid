import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {
	protected static BufferedImage ship, enemy;
	protected static BufferedImage asteroidBig1, asteroidBig2, asteroidBig3;
	protected Texture(){
		load();
	}
	void load(){
		try{
			ship = ImageIO.read(getClass().getResource("Ship.png"));
			enemy = ImageIO.read(getClass().getResource("Enemy.png"));
			asteroidBig1 = ImageIO.read(getClass().getResource("AsteroidBig1.png"));
			asteroidBig2 = ImageIO.read(getClass().getResource("AsteroidBig2.png"));
			asteroidBig3 = ImageIO.read(getClass().getResource("AsteroidBig3.png"));
		}catch(Exception e) { e.printStackTrace(); }
		
	}
}
