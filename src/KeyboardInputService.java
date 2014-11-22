import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputService implements KeyListener {
	private static KeyboardInputService keyboardInputService = null;
	public boolean up, down, up2, down2, p1enter, p2enter;

	private KeyboardInputService() {
	}

	public synchronized static KeyboardInputService getInstance() {
		if (keyboardInputService == null) {
			keyboardInputService = new KeyboardInputService();
		}
		return keyboardInputService;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_W) {
			up2 = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			down2 = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			p2enter = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_E) {
			p1enter = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			up2 = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			down2 = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			p2enter = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_E) {
			p1enter = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
