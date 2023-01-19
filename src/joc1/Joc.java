package joc1;

import java.awt.geom.AffineTransform;
import java.util.*;

public class Joc {
	double startTime = System.currentTimeMillis()/1000.0;

	final Finestra f;

	final Random ran = new Random();

	final List<GameObject> gameObjects = new ArrayList<>();
	final List<Collider> colliders = new ArrayList<>();
	final Queue<GameObject> instatiateQueue = new ArrayDeque<>();
	final Queue<GameObject> destroyQueue = new ArrayDeque<>();

	boolean gamePaused = false;

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
		new Background(this, "assets/BG/web_first_images_release.png");
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

				processNewObjects();
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
		for (GameObject obj:gameObjects) {
			obj.fixedUpdate();
		}
	}

	void addObject(GameObject obj) {
		System.out.println("Added new object.");
		instatiateQueue.add(obj);
	}

	void destroy(GameObject obj) {
		System.out.println("Destroying object...");
		destroyQueue.add(obj);
	}

	void processNewObjects() {
		while (!instatiateQueue.isEmpty()) {
			GameObject next = instatiateQueue.remove();
			synchronized (gameObjects){
				gameObjects.add(next);
			}
			if (next instanceof Collider) {
				colliders.add((Collider) next);
			}
			next.start();
		}

		while (!destroyQueue.isEmpty()) {
			GameObject next = destroyQueue.remove();
			synchronized (gameObjects){
				gameObjects.remove(next);
			}
			if (next instanceof Collider)
				colliders.remove(next);
		}

		/*
		while (!destroyQueue.isEmpty()) {
			gameObjects.removeAll(destroyQueue);
			colliders.removeAll(destroyQueue);
		}
		 */
	}

	AffineTransform getViewMatrix() {
		// TODO: Crear càmera o eliminar viewMatrix
		return new AffineTransform();
	}
}
