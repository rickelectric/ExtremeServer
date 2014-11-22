import java.awt.Graphics2D;
import java.awt.Rectangle;


public abstract class Entity 
{
	protected float x; 
	protected float y; 
	protected float width; 
	protected float height;  
	protected Rectangle boundingRect; 
	
	public Entity(float x, float y)
	{
		this.x = x; 
		this.y = y; 
		this.boundingRect = new Rectangle(); 
	}
	
	public Entity ( float x, float y, float width, float height)
	{
		this(x,y); 
		
		this.width = width; 
		this.height = height;  
		this.boundingRect.setBounds((int) x, (int)y, (int)width, (int)height); 
	}
	
	//update 
	public abstract void update(); 
	
	//draw 
	public abstract void draw(Graphics2D g2d); 
	
	

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Rectangle getBoundingRect() {
		return boundingRect;
	}

	
	
	
	

}
