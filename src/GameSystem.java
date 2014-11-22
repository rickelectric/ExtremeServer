import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

public class GameSystem implements Runnable, Observer {
	private static GameSystem gameSystem;

	private int screenWidth;
	private int screenHeight;

	private GameFrame gameFrame;

	private Sprite background;
	private Sprite ChooseColorBackground;

	private Paddle player1;
	private Paddle2 player2;

	private Polygon obstacle;

	private Ball ball;

	private int GameOption = 3;

	private int player1score = 0, player2score = 0;

	private ColorChoice colorchoice;

	private int choice = 0, choice2 = 0;

	/**
	 * Private Constructor
	 */
	private GameSystem() {
		screenWidth = 1024;
		screenHeight = 668;

		// Create new window for game
		gameFrame = new GameFrame("Extreme Pong Server (Spectator)", screenWidth, screenHeight);

		gameFrame.getArea().addKeyListener(KeyboardInputService.getInstance());

		background = new Sprite("background", 0, 0);
		ChooseColorBackground = new Sprite("choose", 0, 0);

		ball = new Ball(500, 270);

		obstacle = new Polygon();
		obstacle.addPoint(screenWidth / 2 - 60, screenHeight / 2);
		obstacle.addPoint(screenWidth / 2, screenHeight / 2 - 60);
		obstacle.addPoint(screenWidth / 2 + 60, screenHeight / 2);
		obstacle.addPoint(screenWidth / 2, screenHeight / 2 + 60);

		colorchoice = new ColorChoice(0, 0);
		gameFrame.getArea().requestFocus();

	}

	/**
	 * Get instance method. - implements Singleton pattern
	 * 
	 * @return
	 */
	public synchronized static GameSystem getInstance() {
		if (gameSystem == null) {
			gameSystem = new GameSystem();
		}
		return gameSystem;
	}

	/**
	 * Run method. Contains game loop.
	 */
	public void run() {
		BufferStrategy bs = gameFrame.getBufferStrategy();

		while (true) {
			update();
			draw((Graphics2D) bs.getDrawGraphics());

			bs.show();

			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Update
	 */
	private void update() {
		if (GameOption == 0) {
			colorchoice.update();
			if (choice != 0 && choice2 != 0)
				player1 = new Paddle(0, 270, choice);
			player2 = new Paddle2(954, 270, choice2);
		} else if (GameOption == 1) {
			player1.update();
			player2.update();

			ball.checkCollision(player1.getX(), player1.getY(),
					player1.getPaddleWidth(), player1.getPaddleHeight(),
					player1.getPlayerMove());
			ball.checkCollision(player2.getX(), player2.getY(),
					player2.getPaddleWidth(), player2.getPaddleHeight(),
					player2.getPlayerMove());

			if (obstacle.intersects(ball.getBoundingRect())) {
				ball.reflect(obstacle);
			}

			ball.update();
		}
	}

	/**
	 * Draw
	 * 
	 * @param g2d
	 */
	private void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, screenWidth, screenHeight);

		if (GameOption == 3) {
			g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			g2d.setColor(Color.orange);
			g2d.drawString("Clients Connecting...", screenWidth / 2 - 40,
					screenHeight / 2);
		} else if (GameOption == 5) {
			// Draw Without Updating
			background.draw(g2d);

			g2d.setColor(Color.orange);
			g2d.fill(obstacle);

			player1.draw(g2d);
			player2.draw(g2d);
			ball.draw(g2d);

			g2d.setColor(Color.blue);
			g2d.setFont(new Font(null, Font.BOLD, 25));
			g2d.drawString("Player 1: " + player1score, 20, 20);
			g2d.drawString("Player 2: " + player2score, 870, 20);
		} else if (GameOption == 0) {
			colorchoice.draw(g2d);
			ChooseColorBackground.draw(g2d);
		} else if (GameOption == 1) {
			background.draw(g2d);

			g2d.setColor(Color.orange);
			g2d.fill(obstacle);

			player1.draw(g2d);
			player2.draw(g2d);
			ball.draw(g2d);

			g2d.setColor(Color.blue);
			g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
			g2d.drawString("Player 1: " + player1score, 20, 20);
			g2d.drawString("Player 2: " + player2score, 870, 20);
		}
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setPlayer1Score(int x) {
		player1score = x;
	}

	public void setPlayer2Score(int x) {
		player2score = x;
	}

	public void setColorChoice(int val) {
		choice = val;
	}

	public void setColorChoice2(int val) {
		choice2 = val;
	}

	public void setGameOption(int val) {
		GameOption = val;
	}

	/**
	 * Observer Implementation
	 */
	public void update(Observable o, Object state) {
		System.out.println("Observer Updated");
		switch ((Integer) state) {
		case GameServer.WAITING:
			System.out.println("State: WAITING");
			GameOption = 3;
			break;
		case GameServer.SELECTING:
			System.out.println("State: SELECTING");
			GameOption = 0;
			break;
		case GameServer.READY:
			System.out.println("State: READY");
			GameOption = 5;
			break;
		case GameServer.RUNNING:
			System.out.println("State: RUNNING");
			GameOption = 1;
			break;
		}
	}

}
