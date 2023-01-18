package joc1;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Joc {
	double startTime = System.currentTimeMillis()/1000.0;

	InputManager input = new InputManager();
	Finestra f;

	Random ran = new Random();

	boolean gamePaused = false;
	Ship playerShip;
	List<Ship> enemies = new ArrayList<>();
	List<Bullet> bullets = new ArrayList<>();

	public static void main(String[] args) {
		Joc j = new Joc();
		j.run();
	}

	public Joc () {
		f = new Finestra(this);
		f.addKeyListener(input);
		f.addMouseListener(input);
	}
	
	public void run () {
		playerShip = new PlayerShip(new Vec2(0, 0));
		for (int i = 0; i < 5; i++) {
			enemies.add(new EnemyShip(Vec2.random(f.l, f.r, f.b, f.t)));
		}

		while (true) {
			double currentTime = System.currentTimeMillis()/1000.0;
			double frameTime = currentTime - startTime;
			if (frameTime < Time.targetFrameTime) {
				try {
					Thread.sleep((long) ((Time.targetFrameTime - frameTime) * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			startTime = System.currentTimeMillis()/1000.0;
			double sleepTime = startTime - currentTime;
			// Per què necessitem mesurar el temps de "descans" en comptes d'utilitzar directament targetFrameTime - frameTime?
			// Perquè la conversió a long provoca pèrdues de precissió importants, provocant que el deltaTime
			// es calculi incorrectament i les físiques es descontrolin. També Thread.sleep() dorm el temps que li dona la gana.
			Time.updateTimes(frameTime + sleepTime);

			// Guardem Inputs
			input.updateMousePosition(f);
			input.updateInputs();

			update();

			// Físiques
			while (Time.unsimulatedTime() >= Time.fixedDeltaTime) {
				fixedUpdate();
				Time.physicsStep();
			}

			// Render
			f.repaint(); // repintar la pantalla
		}
	}

	private void update() {
		if (input.getActionDown(Action.PAUSE)) {
			if (!gamePaused){
				gamePaused = true;
				// Menu...
			}
			else {
				gamePaused = false;
				// Close menu...
			}
		}
		if (!gamePaused) {
			playerShip.pointCannonAt(input.getMousePosition());
		}
	}

	private void fixedUpdate() {
		if (!gamePaused) {
			processPlayerMovement();
			moureElements(); // moure els objectes de pantalla
			// detectar col·lisions
		}
	}

	private void processPlayerMovement() {
		playerShip.thrust(input.getDirection(), Time.fixedDeltaTime);
		if (input.getAction(Action.SHOOT) && (startTime - playerShip.lastShotTime >= 1/playerShip.attackSpeed))
			bullets.add(playerShip.shoot());
	}

	private void moureElements() {
		playerShip.fixedUpdate(Time.fixedDeltaTime);
		for (Ship enemy:enemies) {
			enemy.fixedUpdate(Time.fixedDeltaTime);
		}
		for (Bullet bullet:bullets) {
			bullet.move(Time.fixedDeltaTime);
		}
	}

	AffineTransform getViewMatrix() {
		// TODO: Crear càmera o eliminar viewMatrix
		return new AffineTransform();
	}
}
