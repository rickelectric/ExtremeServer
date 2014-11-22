import java.awt.Point;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Observable;

public class GameServer extends Observable implements Runnable {

	private static GameServer thisInst;
	private static Thread gameThread;
	private static Thread serverThread;

	public static GameServer getInstance() {
		return thisInst == null ? thisInst = new GameServer() : thisInst;
	}

	public static final int WAITING = 1, SELECTING = 2, READY = 3, RUNNING = 4;
	public int state;

	private DatagramSocket serverSock;
	private PlayerInfo[] players;
	private Point ball;

	public static void main(String[] args) throws Exception {
		GameSystem gs = GameSystem.getInstance();
		getInstance().addObserver(gs);
		gameThread = new Thread(gs);
		serverThread = new Thread(getInstance());
		gameThread.start();
		serverThread.start();
	}

	private GameServer() {
		players = new PlayerInfo[] { null, null };
		ball = new Point();
		setState(WAITING);
	}

	private void setState(int state) {
		this.state = state;
		setChanged();
		notifyObservers(state);
	}

	public void run() {
		try {
			serverSock = new DatagramSocket(9000);
			waitForPlayers();
			sendInit();
			while (true) {
				DatagramPacket pl = receive();
				String[] rData = new String(pl.getData()).trim().split(",");
				if (rData[0].equals("EXTREME_PONG")) {
					int pid = Integer.parseInt(rData[1]);
					float y = Float.parseFloat(rData[2]);
					players[pid - 1].playerY = y;
					sendGameData(pid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void waitForPlayers() throws Exception {
		int connectedPlayers = 0;
		while (connectedPlayers < 2) {
			System.out.println("Recieving Player HELO...");
			DatagramPacket rcv = receive();
			String helo = new String(rcv.getData()).trim();
			if (!helo.equals("EXTREME_PONG"))
				continue;
			System.out.println("Valid HELO Recieved...");
			players[connectedPlayers] = new PlayerInfo();
			players[connectedPlayers].ip = rcv.getAddress();
			players[connectedPlayers].port = rcv.getPort();
			players[connectedPlayers].ID = connectedPlayers + 1;
			send(players[connectedPlayers].ip, players[connectedPlayers].port,
					"EXTREME," + players[connectedPlayers].ID);

			connectedPlayers++;
			System.out.println("Player " + connectedPlayers + " ("
					+ players[connectedPlayers - 1].ip.getHostAddress() + ":"
					+ players[connectedPlayers - 1].port + ") Registered");
		}
		// Both Players Connected And Know Their IDs. Switch To "Select Color"
		// Mode
		setState(SELECTING);
		for (PlayerInfo p : players) {
			send(p.ip, p.port, "" + state);
		}
		System.out.println("Server Switched To Select Color");
	}

	// State: Selecting. Wait Till Both Players Ready State=1, Then Send.
	public void sendInit() throws Exception {
		int readyStates = 0;
		while (readyStates < 2) {
			DatagramPacket rcv = receive();
			String[] color = new String(rcv.getData()).trim().split(",");
			int pid = Integer.parseInt(color[0]);// Player ID
			int colorID = Integer.parseInt(color[1]);// Color ID
			int readyState = Integer.parseInt(color[2]);// Ready State (0=false,
														// 1=true)
			if (players[pid - 1].ready == 0){
				
				players[pid - 1].color = colorID;
				players[pid - 1].ready = readyState;
				if (readyState == 1) {
					readyStates++;
				}
				// Send Each Player's Color And Ready State To The Other.
				send(players[0].ip, players[0].port, "EXTREME,"
						+ players[1].color + "," + players[1].ready);
				send(players[1].ip, players[1].port, "EXTREME,"
						+ players[0].color + "," + players[0].ready);
			}
		}
		setState(READY);
		System.out.println("Server Ready");
		for (PlayerInfo pi : players) {
			send(pi.ip, pi.port, "" + state);
		}
	}

	public void sendGameData(int pid) throws Exception {
		send(players[pid - 1].ip, players[pid - 1].port, ""
				+ players[pid == 1 ? 1 : 0].playerY);
		send(players[pid - 1].ip, players[pid - 1].port, ball.getX() + ","
				+ ball.getY());
		send(players[pid - 1].ip, players[pid - 1].port, players[0].points
				+ "," + players[1].points);
	}

	public void send(InetAddress IP, int port, String data) throws Exception {
		DatagramPacket snd = new DatagramPacket(data.getBytes(),
				data.getBytes().length, IP, port);
		serverSock.send(snd);
	}

	public DatagramPacket receive() throws Exception {
		DatagramPacket rcv = new DatagramPacket(new byte[1024], 1024);
		serverSock.receive(rcv);
		return rcv;
	}

	public float getLocationOf(int playerID) {
		return players[playerID - 1].playerY;
	}

	public PlayerInfo getPlayer(int pid) {
		return players[pid - 1];
	}
}

class PlayerInfo {

	int ready;
	int ID;
	float playerY;
	int points;
	int color;

	InetAddress ip;
	int port;

	public PlayerInfo() {
		ID = 0;
		playerY = 0;
		points = 0;
		color = 1;
		ip = null;
		port = 0;
		ready = 0;
	}
}
