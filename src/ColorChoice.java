import java.awt.Graphics2D;


public class ColorChoice extends Entity {
	private Sprite p1chooser,p2chooser;
	private int buttontimer=0,c1=0,c2=0;
	
	private static final int[] cpts = new int[] { 223, 367, 515, 663 };
	
	public ColorChoice(float x, float y) {
		super(x, y);
		
		c1=1;
		c2=4;
		
		p1chooser = new Sprite("choosep1",223,322);
		p2chooser = new Sprite("choosep2",663,355);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		c1 = GameServer.getInstance().getPlayer(1).color;
		p1chooser.setX(cpts[c1-1]);
		
		c2 = GameServer.getInstance().getPlayer(2).color;
		p2chooser.setX(cpts[c2-1]);
		
	}

	@Override
	public void draw(Graphics2D g2d) {
		// TODO Auto-generated method stub
		p1chooser.draw(g2d);
		p2chooser.draw(g2d);
	}
	

}
