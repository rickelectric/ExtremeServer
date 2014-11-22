import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class Sprite extends Entity 
{
	protected Image image;
	protected float opacity; 
	
	/**
	 * Constructor Method
	 * @param name
	 * @param imageName
	 * @param x
	 * @param y
	 */
	public Sprite(String imageName, float x, float y)
	{
		super(x, y); 
		this.image = loadImage("images/" + imageName + ".png"); 
		this.width = image.getWidth(null); 
		this.height = image.getHeight(null); 
		this.opacity = 1.0f; 
		
	} 
	
	public Sprite(String imageName, float x, float y, float opacity)
	{
		super(x, y); 
		this.image = loadImage("images/" + imageName + ".png"); 
		this.width = image.getWidth(null); 
		this.height = image.getHeight(null); 
		this.opacity = opacity; 
	} 

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void draw(Graphics2D g2d) 
	{
		//set alphacomposite to our opacity 
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2d.drawImage(this.image, (int) x, (int) y, null); 
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
	
	/**
	 * Retrieves an image from disk 
	 * @param ref
	 * @return
	 */
	private Image loadImage(String ref) 
	{ 	
	   	Image tempImage = null; 
		BufferedImage sourceImage = null;
			
		try {			
			URL url = this.getClass().getClassLoader().getResource(ref);
				
			if (url == null) {
				System.out.println("Can't find ref: "+ref);
			}			
			sourceImage = ImageIO.read(url);
	                       
		} 
		catch (IOException e) {
			System.out.println("Failed to load: "+ref);
		}
			
	    //creates an accelerated image
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		tempImage = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.TRANSLUCENT);
			
		// draw source image into the accelerated image
		tempImage.getGraphics().drawImage(sourceImage,0,0,null);
		
		return tempImage; 
			
	}
	 

}
