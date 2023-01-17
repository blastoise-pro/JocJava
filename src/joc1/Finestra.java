package joc1;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

class Finestra extends Frame {
	private final Joc j;
	Image im;
	Graphics2D g2;
	int winHeight = 600, winWidth = 1000;
	// Parametres del "frustum" de la projecció ortogràfica (quines coordenades a view space són visibles)
	// Només necessitem els límits esquerre i dret, el superior i inferior es calculen respectant l'aspect ratio
	// de la finestra
	float l = -100, r = 100, t, b;
	AffineTransform projectionMatrix;
	AffineTransform viewMatrix;
	AffineTransform inversePVMatrix;
	
	Finestra(Joc joc) {
		super("Joc");
		setSize(winWidth, winHeight);
		setVisible(true);
		j = joc;

		im = createImage(winWidth, winHeight);
		g2 = (Graphics2D)im.getGraphics(); //és el graphics de la imatge, no de la pantalla!
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		// Per dibuixar amb subpixel accuracy

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				winHeight = e.getComponent().getHeight();
				winWidth = e.getComponent().getWidth();
				im = createImage(winWidth, winHeight);
				g2 = (Graphics2D)im.getGraphics();

				updateProjectionMatrix();
			}
		});

		updateProjectionMatrix();
		updateViewMatrix();
		updateInversePVMatrix();
	}

	private void updateProjectionMatrix() {
		float aspectRatio = (float) winHeight/winWidth;
		t =  (r-l)*aspectRatio/2;
		b = -(r-l)*aspectRatio/2;
		projectionMatrix = new AffineTransform();
		projectionMatrix.translate(-winWidth*l/(r-l), winHeight*t/(t-b));
		projectionMatrix.scale(winWidth/(r-l), -winHeight/(t-b));
		System.out.println("t: " + t + " b: " + b);
		System.out.println("winHeight: " + winHeight + " winWidth: " + winWidth);
	}

	private void updateViewMatrix() {
		// Update view matrix for new camera position
		viewMatrix = j.getViewMatrix();
	}

	void updateInversePVMatrix() {
		AffineTransform PVMatrix = new AffineTransform(projectionMatrix);
		PVMatrix.concatenate(viewMatrix);
		try {
			inversePVMatrix = PVMatrix.createInverse();
		}
		catch (NoninvertibleTransformException e) {
			throw new AssertionError("Transformació no té inversa"); // No pot passar
		}
	}
	
	public void paint(Graphics g) { // la crida el sistema de manera implícita
		g.drawImage(im,0,0,null);
	}

	public void update(Graphics g){
		g2.setColor(Color.black);
		g2.fillRect(0, 0, winWidth, winHeight);

		AffineTransform PVMatrix = new AffineTransform(projectionMatrix);
		updateViewMatrix();
		PVMatrix.concatenate(viewMatrix);

		j.playerShip.pintar(g2, PVMatrix);
		for (Ship ship:j.enemies)
			ship.pintar(g2, PVMatrix);

		for (Bullet bullet:j.bullets) {
			bullet.pintar(g2, PVMatrix);
		}

		paint(g);
	}
}
