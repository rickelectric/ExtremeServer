import java.awt.Graphics2D;

public class Paddle2 extends Entity {
	private Sprite paddle;

	private float paddleheight;
	private float paddlewidth;
	private int move;
	private int color;

	public Paddle2(float x, float y, int colorChoice) {
		super(x, y);
		// TODO Auto-generated constructor stub
		color = colorChoice;
		if (color == 1)
			paddle = new Sprite("paddle", this.x, this.y);
		if (color == 2)
			paddle = new Sprite("paddle2", this.x, this.y);
		if (color == 3)
			paddle = new Sprite("paddle3", this.x, this.y);
		if (color == 4)
			paddle = new Sprite("paddle4", this.x, this.y);

		this.paddleheight = 150;
		this.paddlewidth = 68;
	}

	@Override
	public void update() {
		move = 0;
		float py = GameServer.getInstance().getLocationOf(2);

		if (py < y)
			move = -2;
		else if (py > y)
			move = +2;

		this.y = py;
		paddle.setY(this.y);
	}

	@Override
	public void draw(Graphics2D g2d) {
		// TODO Auto-generated method stub
		paddle.draw(g2d);
	}

	public float getPaddleWidth() {
		return paddlewidth;
	}

	public float getPaddleHeight() {
		return paddleheight;
	}

	public int getPlayerMove() {
		return move;
	}

}
