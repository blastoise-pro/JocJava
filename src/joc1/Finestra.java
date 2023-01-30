package joc1;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Finestra extends Frame {
	private final Joc j;
	Graphics2D fGraphics;
	int winHeight = 600, winWidth = 1000;

	Finestra(Joc joc) {
		super("Joc");
		setSize(winWidth, winHeight);
		setVisible(true);
		fGraphics = (Graphics2D) getGraphics();
		fGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		fGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		j = joc;

		j.bufferImage = createImage(winWidth, winHeight);
		j.g2buff = (Graphics2D)j.bufferImage.getGraphics(); //és el graphics de la imatge, no de la pantalla!
		j.g2buff.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		j.g2buff.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
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
				fGraphics = (Graphics2D) getGraphics(); // Aquest objecte canvia al canviar el tamany de la finestra
				fGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				fGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				j.bufferImage = createImage(winWidth, winHeight);
				j.g2buff = (Graphics2D)j.bufferImage.getGraphics();

				j.camera.updateProjectionMatrix();
			}
		});
	}
	
	public void paint(Graphics g) { // la crida el sistema de manera implícita
		g.drawImage(j.bufferImage,0,0,null);
	}

	public void update(Graphics g){
		paint(g);
	}
}
