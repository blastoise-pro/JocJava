package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.*;
import java.util.List;

public class Joc {
	public final static boolean DRAW_HITBOXES = false;
	public final static boolean DRAW_BULLETSPAWNS = true;
	private double startTime = System.currentTimeMillis()/1000.0;

	private final Finestra f;
	//Image bufferImage;
	//Graphics2D g2buff;

	final Random ran = new Random();

	SceneManager sceneManager;

	private final List<GameObject> gameObjects = new ArrayList<>();
	private final List<Collider> colliders = new ArrayList<>();
	private Set<UnorderedPair<Collider>> activeCollisions = new HashSet<>();
	private final List<GUIElement> GUIElements = new ArrayList<>();
	private final List<MouseReciever> GUIInteractables = new ArrayList<>();
	private List<MouseReciever> currentContains = new ArrayList<>();
	private final Queue<GameObject> instatiateQueue = new ArrayDeque<>();
	private final Queue<GameObject> destroyQueue = new ArrayDeque<>();

	Camera camera;
	GameController gameController;
	PlayerShip playerShip;

	public static void main(String[] args) {
		Joc j = new Joc();
		j.run();
	}

	public Joc () {
		AssetLoader.loadAssets();
		sceneManager = new SceneManager(this, Scene.GAMEPLAY);
		f = new Finestra(this);
		Input.initInput();
		Input input = new Input();
		f.addKeyListener(input);
		f.addMouseListener(input);
	}
	
	public void run () {
		camera = new Camera(this, new Vec2(), f);
		gameController = new GameController(this);

		new Background(this);
		playerShip = new PlayerShip(this, new Vec2(0, 0));
		for (int i = 0; i < 5; i++) {
			new Bomber(this, Vec2.randomWithRadius(200));
			new Frigate(this, Vec2.randomWithRadius(200));
		}
		new Battlecruiser(this, new Vec2(100, 0));
		new Fighter(this, new Vec2(0, 200));
		new Scout(this, new Vec2());

		new GUIText(this, f, new Vec2(0f, 0.5f), 0, new Vec2(0f, 0.5f), "Hola lol", Color.white, new Font("Arial", Font.PLAIN, 30), null);
		while (true) {
			double currentTime = System.currentTimeMillis()/1000.0;
			double frameTime = currentTime - startTime;
			//System.out.println("Should sleep for: " + (Time.targetFrameTime - frameTime));
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

			//System.out.println("Slept for: " + sleepTime);
			//System.out.println("DeltaTime: " + Time.deltaTime());
			//System.out.println("RealDeltaTime: " + Time.unscaledDeltaTime());
			//System.out.println("Time: " + Time.time());
			//System.out.println("RealTime: " + Time.unscaledTime());

			// Guardem Inputs
			Input.updateMousePosition(f, camera);
			Input.updateInputs();

			// Interacció amb GUI
			checkGUIInteraction();

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
			Graphics2D g2 = (Graphics2D) f.bstrat.getDrawGraphics();
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			render(g2);
			g2.dispose();
			f.bstrat.show();
			//render(g2buff);
			//f.fGraphics.drawImage(bufferImage, 0, 0, null);

			destroyObjects();
			//System.out.println("\n\n");
		}
	}

	private void update() {
		for (GameObject obj:gameObjects) {
			if (!obj.destroying){
				obj.update();
			}
		}
		for (GUIElement gui:GUIElements) {
			if (!gui.destroying) {
				gui.update();
			}
		}
	}

	private void fixedUpdate() {
		for (GameObject obj:gameObjects) {
			if (!obj.destroying){
				obj.fixedUpdate();
			}
		}
		for (GUIElement gui:GUIElements) {
			if (!gui.destroying) {
				gui.fixedUpdate();
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
				col2 = colliders.get(j);
				if (!col2.colliderIsActive() || col1.getCollisionMask().contains(col2.getLabel()) || col2.getCollisionMask().contains(col1.getLabel())) {
					continue;
				}
				area = new Area(col1.getCollider());
				area.intersect(new Area(col2.getCollider()));
				if (!area.isEmpty()) {
					collisionPair = new UnorderedPair<>(col1, col2);
					if (!lastStepCollisions.contains(collisionPair)){
						col1.onColliderEnter(col2);
						col2.onColliderEnter(col1);
					}
					else {
						col1.onColliderStay(col2);
						col2.onColliderStay(col1);
					}
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
		for (GUIElement gui:GUIElements) {
			if (!gui.destroying) {
				gui.lateUpdate();
			}
		}
	}

	void checkGUIInteraction() {
		List<MouseReciever> lastContains = currentContains;
		currentContains = new ArrayList<>();
		Vec2 mousePos = Input.getGUIMousePosition();
		for (MouseReciever mr:GUIInteractables) {
			if (mr.containsCoords(mousePos)) {
				if (!lastContains.contains(mr)) {
					mr.onMouseEnter();
				}
				currentContains.add(mr);
				if (Input.getActionDown(Action.MENU_OK)) {
					mr.onMouseClick();
				}
			}
		}
		lastContains.removeAll(currentContains);
		for (MouseReciever mr:lastContains) {
			mr.onMouseExit();
		}
		if (Input.getActionDown(Action.MENU_OK)) {
		}
	}

	void addObject(GameObject obj) {
		//System.out.println("Added new object.");
		instatiateQueue.add(obj);
	}

	void destroy(GameObject obj) {
		//System.out.println("Destroying object...");
		destroyQueue.add(obj);
		obj.destroying = true;
	}

	void processNewObjects() {
		while (!instatiateQueue.isEmpty()) {
			GameObject next = instatiateQueue.remove();
			if (next instanceof GUIElement) {
				GUIElements.add((GUIElement) next);
				if (next instanceof MouseReciever) {
					GUIInteractables.add((MouseReciever) next);
				}
				continue;
			}
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
			if (next instanceof GUIElement) {
				GUIElements.remove((GUIElement) next);
				if (next instanceof MouseReciever) {
					GUIInteractables.remove(next);
				}
				continue;
			}
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
		//g2.setColor(Color.black);
		//g2.fillRect(0, 0, f.winWidth, f.winHeight);

        AffineTransform PVMatrix = new AffineTransform(camera.projectionMatrix);
        camera.updateViewMatrix();
        PVMatrix.concatenate(camera.viewMatrix);

        for (GameObject obj : gameObjects) {
			if (!obj.isChild) {
				obj.pintar(g2, PVMatrix);
			}
        }

		for (GUIElement elm:GUIElements) {
			if (!elm.isChild) {
				elm.pintar(g2, Camera.projectionMatrixGUI);
			}
		}

        AffineTransform savedT = g2.getTransform();
        g2.setColor(Color.gray);
        g2.scale(3, 3);
		g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString(Double.toString(Time.fps()), 0, 20);
        g2.setTransform(savedT);
    }
}
