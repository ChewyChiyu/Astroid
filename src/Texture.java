import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {
	protected static BufferedImage ship;
	protected Texture(){
		load();
	}
	void load(){
		try{
			ship = ImageIO.read(getClass().getResource("Ship.png"));
		}catch(Exception e) { e.printStackTrace(); }
		
	}
}
