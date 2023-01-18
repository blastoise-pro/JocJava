package joc1;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Joc {
	double startTime = System.currentTimeMillis()/1000.0;

	Finestra f;

	Random ran = new Random();

	boolean gamePaused = false;
	List<GameObject> gameObjects = new ArrayList<>();
	List<PhysicsObject> physicsObjects = new ArrayList<>();
	List<Collider> colliders = new ArrayList<>();
	List<GameObject> objectQueue = new ArrayList<>();

	public static void main(String[] args) {
		Joc j = new Joc();
		j.run();
	}

	public Joc () {
		f = new Finestra(this);
		Input.initInput();
		Input input = new Input();
		f.addKeyListener(input);
		f.addMouseListener(input);
	}
	
	public void run () {
		new PlayerShip(this, new Vec2(0, 0));
		for (int i = 0; i < 5; i++) {
			new EnemyShip(this, Vec2.random(f.l, f.r, f.b, f.t));
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
			Input.updateMousePosition(f);
			Input.updateInputs();

			update();
			processNewObjects();

			// Físiques
			while (Time.unsimulatedTime() >= Time.fixedDeltaTime) {
				fixedUpdate(); // Càlculs i moviments de cada objecte
				// collider    // Col·lisions (update hitbox + checks)
				Time.physicsStep();
			}

			// Render
			f.repaint(); // repintar la pantalla
		}
	}

	private void update() {
		if (Input.getActionDown(Action.PAUSE)) {
			if (!gamePaused){
				gamePaused = true;
				Time.timeScale = 0;
				// Menu...
			}
			else {
				gamePaused = false;
				Time.timeScale = 1;
				// Close menu...
			}
		}
		for (GameObject obj:gameObjects) {
			obj.update();
		}
	}

	private void fixedUpdate() {
		for (PhysicsObject obj:physicsObjects) {
			obj.fixedUpdate();
		}
	}

	void addObject(GameObject obj) {
		System.out.println("Added new object.");
		objectQueue.add(obj);
	}

	void processNewObjects() {
		while (objectQueue.size() > 0) {
			GameObject next = objectQueue.get(0);
				gameObjects.add(next);
				if (next instanceof PhysicsObject)
					physicsObjects.add((PhysicsObject) next);
				if (next instanceof Collider)
					colliders.add((Collider) next);
				objectQueue.remove(0);
		}
	}

	AffineTransform getViewMatrix() {
		// TODO: Crear càmera o eliminar viewMatrix
		return new AffineTransform();
	}
}
