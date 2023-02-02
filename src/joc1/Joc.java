package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;

public class Joc {
	private double startTime = System.currentTimeMillis()/1000.0;

	private final Finestra f;
	Image bufferImage;
	Graphics2D g2buff;

	final Random ran = new Random();

	private final List<GameObject> gameObjects = new ArrayList<>();
	private final List<Collider> colliders = new ArrayList<>();
	private Set<UnorderedPair<Collider>> activeCollisions = new HashSet<>();
	private final Queue<GameObject> instatiateQueue = new ArrayDeque<>();
	private final Queue<GameObject> destroyQueue = new ArrayDeque<>();

	Camera camera;
	PlayerShip playerShip;
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
		camera = new Camera(this, new Vec2(), f);

		//new Background(this, "assets/BG/web_first_images_release.png");
		playerShip = new PlayerShip(this, new Vec2(0, 0));
		for (int i = 0; i < 5; i++) {
			new EnemyShip(this, Vec2.random(camera.l, camera.r, camera.b, camera.t));
		}

		while (true) {
			double currentTime = System.currentTimeMillis()/1000.0;
			double frameTime = currentTime - startTime;
			System.out.println("Should sleep for: " + (Time.targetFrameTime - frameTime));
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
			// es calculi incorrectament i les físiques es descontrolin. També Thread.sleep() no es exacte en el temps que dorm.
			Time.updateTimes(frameTime + sleepTime);

			System.out.println("Slept for: " + sleepTime);
			System.out.println("DeltaTime: " + Time.deltaTime());
			//System.out.println("RealDeltaTime: " + Time.unscaledDeltaTime());
			//System.out.println("Time: " + Time.time());
			//System.out.println("RealTime: " + Time.unscaledTime());

			// Guardem Inputs
			Input.updateMousePosition(f, camera);
			Input.updateInputs();

			// Lògica
			update();

			processNewObjects();

			// Físiques
			int steps = 0;
			Time.callingFromFixedUpdate = true;
			while (Time.unsimulatedTime() >= Time.fixedDeltaTime) {
				fixedUpdate(); // Càlculs i moviments de cada objecte
				testCollisions();    // Col·lisions (update hitbox + checks)

				processNewObjects();
				Time.physicsStep();
				steps++;
			}
			Time.callingFromFixedUpdate = false;
			//System.out.println("Physics steps taken: " + steps);
			//System.out.println("Unsimulated time: " + Time.unsimulatedTime());

			// Lògica (principalment càmera)
			lateUpdate();

			// Render
			if (f.fullscreen) {
				Graphics2D g2 = (Graphics2D) f.bstrat.getDrawGraphics();
				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
				render(g2);
				g2.dispose();
				f.bstrat.show();
			}
			else {
				render(g2buff);
				f.fGraphics.drawImage(bufferImage, 0, 0, null);
			}
			destroyObjects();
			System.out.println("\n\n");
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
			if (!obj.destroying){
				obj.update();
			}
		}
	}

	private void fixedUpdate() {
		for (GameObject obj:gameObjects) {
			if (!obj.destroying){
				obj.fixedUpdate();
			}
		}
	}

	void testCollisions() {
		Set<UnorderedPair<Collider>> lastStepCollisions = activeCollisions;
		activeCollisions = new HashSet<>();
		UnorderedPair<Collider> collisionPair;
		Collider col1;
		Collider col2;
		Area area;
		for (int i = 0; i < colliders.size(); i++) {
			if (!colliders.get(i).colliderIsActive()) {
				continue;
			}
			col1 = colliders.get(i);
			for (int j = i+1; j < colliders.size(); j++) {
				if (!colliders.get(j).colliderIsActive()) {
					continue;
				}
				area = new Area(col1.getCollider());
				col2 = colliders.get(j);
				area.intersect(new Area(col2.getCollider()));
				if (!area.isEmpty()) {
					col1.onColliderEnter(col2);
					col2.onColliderEnter(col1);
					collisionPair = new UnorderedPair<>(col1, col2);
					activeCollisions.add(collisionPair);
				}
			}
		}
		lastStepCollisions.removeAll(activeCollisions);
		for (UnorderedPair<Collider> pair:lastStepCollisions) {
			pair.first.onColliderExit(pair.second);
			pair.second.onColliderExit(pair.first);
		}
	}

	private void lateUpdate() {
		for (GameObject obj:gameObjects) {
			if (!obj.destroying){
				obj.lateUpdate();
			}
		}
	}

	void addObject(GameObject obj) {
		System.out.println("Added new object.");
		instatiateQueue.add(obj);
	}

	void destroy(GameObject obj) {
		System.out.println("Destroying object...");
		destroyQueue.add(obj);
		obj.destroying = true;
	}

	void processNewObjects() {
		while (!instatiateQueue.isEmpty()) {
			GameObject next = instatiateQueue.remove();
			gameObjects.add(next);
			if (next instanceof Collider) {
				colliders.add((Collider) next);
			}
			next.start();
		}
	}

	void destroyObjects() {
		while (!destroyQueue.isEmpty()) {
			GameObject next = destroyQueue.remove();
			gameObjects.remove(next);
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

	void render(Graphics2D g2) {
		g2.setColor(Color.black);
		g2.fillRect(0, 0, f.winWidth, f.winHeight);
		AffineTransform savedT = g2.getTransform();
		g2.setColor(Color.gray);
		g2.transform(AffineTransform.getScaleInstance(3, 3));
		g2.drawString(Double.toString(Time.fps()), 0, 20);
		g2.setTransform(savedT);

		AffineTransform PVMatrix = new AffineTransform(camera.projectionMatrix);
		camera.updateViewMatrix();
		PVMatrix.concatenate(camera.viewMatrix);

		for (GameObject obj : gameObjects) {
			obj.pintar(g2, PVMatrix);
		}
	}
}
