import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Random;

public class Ball extends Entity {
	private Sprite ball;
	private int xSpeed, ySpeed, width;
	private Rectangle rect, rect2;
	private Random rand;
	private int directionx, directiony;
	private int countCollisions = 0;
	private int p1score = 0, p2score = 0;

	public Ball(float x, float y) {
		super(x, y);
		rand = new Random();
		setBall();
		this.width = 70;
		// TODO Auto-generated constructor stub
		ball = new Sprite("ball", this.x, this.y);
		rect = new Rectangle();
		rect2 = new Rectangle();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		if (this.x < 50) {
			this.x = 500;
			this.y = 270;
			setBall();
			p2score++;
			GameSystem.getInstance().setPlayer2Score(p2score);
		}
		if (this.x + this.width > GameSystem.getInstance().getScreenWidth() - 50) {
			this.x = 500;
			this.y = 270;
			setBall();
			p1score++;
			GameSystem.getInstance().setPlayer1Score(p1score);
		}
		if (countCollisions == 5) {
			if (xSpeed > 0)
				xSpeed += 1;
			else
				xSpeed -= 1;
			countCollisions = 0;
		}
		if ((this.y + ySpeed < 0)
				|| (this.y + this.width + ySpeed > GameSystem.getInstance()
						.getScreenHeight()))
			ySpeed = ySpeed * -1;

		this.x += xSpeed;
		this.y += ySpeed;

		ball.setX(this.x);
		ball.setY(this.y);

	}

	@Override
	public void draw(Graphics2D g2d) {
		// TODO Auto-generated method stub
		ball.draw(g2d);
	}

	public void reflect(Shape obstacle) {
		if (rect2.x + rect2.width / 2 < obstacle.getBounds().x
				+ obstacle.getBounds().width / 2
				&& rect2.y + rect2.height / 2 < obstacle.getBounds().y
						+ obstacle.getBounds().height / 2) {
			// Top Left
			int tmp = -Math.abs(xSpeed);
			xSpeed = Math.abs(ySpeed);
			ySpeed = tmp;
		} else if (rect2.x + rect2.width / 2 < obstacle.getBounds().x
				+ obstacle.getBounds().width / 2
				&& rect2.y + rect2.height / 2 > obstacle.getBounds().y
						+ obstacle.getBounds().height / 2) {
			// Bottom Left
			int tmp = Math.abs(xSpeed);
			xSpeed = Math.abs(ySpeed);
			ySpeed = tmp;
		} else if (rect2.x + rect2.width / 2 > obstacle.getBounds().x
				+ obstacle.getBounds().width / 2
				&& rect2.y + rect2.height / 2 < obstacle.getBounds().y
						+ obstacle.getBounds().height / 2) {
			// Top Right
			int tmp = -Math.abs(xSpeed);
			xSpeed = Math.abs(ySpeed);
			ySpeed = tmp;
		} else {
			// Bottom Right
			int tmp = Math.abs(xSpeed);
			xSpeed = -Math.abs(ySpeed);
			ySpeed = tmp;
		}
	}

	public void checkCollision(float px, float py, float pwidth, float pheight,
			int move) {
		rect.setBounds((int) px, (int) py, (int) pwidth, (int) pheight);
		rect2.setBounds((int) (this.x + this.xSpeed),
				(int) (this.y + this.ySpeed), (int) this.width,
				(int) this.width);
		if (rect.getBounds().intersects(rect2)) {
			xSpeed = xSpeed * -1;
			ySpeed += move;
			countCollisions++;
		}

		rect2.setBounds((int) (this.x + this.xSpeed),
				(int) (this.y + (2 * this.ySpeed)), (int) this.width,
				(int) this.width);
		if (rect.getBounds().intersects(rect2)) {
			xSpeed = xSpeed * -1;
			ySpeed = ySpeed * -1;
			countCollisions++;
		}
	}

	public void setBall() {
		directionx = rand.nextInt(2);
		if (directionx == 0) {
			this.xSpeed = 5;
		} else {
			this.xSpeed = -5;
		}
		directionx = rand.nextInt(2);
		if (directiony == 0) {
			this.ySpeed = -rand.nextInt(5) + 5;
		} else {
			this.ySpeed = rand.nextInt(5) + 5;
		}
	}

	public Rectangle getBoundingRect() {
		return rect2;
	}

}
