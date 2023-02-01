package joc1;

import java.awt.*;
import java.awt.event.*;

class Finestra extends Frame {
	private final Joc j;
	private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	Graphics2D fGraphics;

	int winHeight = 600, winWidth = 1000;
	private boolean fullscreen = false;

	Finestra(Joc joc) {
		super("Joc");
		setSize(winWidth, winHeight);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
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
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F11) {
					toggleFullscreen();
				}
			}
		});
	}

	private void toggleFullscreen() {
		if (fullscreen) {
			device.setFullScreenWindow(null);
			fullscreen = false;
		}
		else {
			if (device.isFullScreenSupported()) {
				setIgnoreRepaint(true);
				setResizable(false);
				device.setFullScreenWindow(this);
				System.out.println(device.getDisplayMode());
				fullscreen = true;
			}
		}
		/*
		if (maximized){
			dispose();
			setUndecorated(false);
			setExtendedState(Frame.NORMAL);
			setSize(widthMinimized, heightMinimized);
			setLocation(lastPos);
			setVisible(true);
			winHeight = getHeight();
			winWidth = getWidth();
			maximized = false;
		}
		else {
			widthMinimized = getWidth();
			heightMinimized = getHeight();
			lastPos = getLocation();

			dispose();
			setUndecorated(true);
			setExtendedState(Frame.MAXIMIZED_BOTH);
			setVisible(true);

			winHeight = getHeight();
			winWidth = getWidth();
			maximized = true;
		}*/
	}
	
	public void paint(Graphics g) { // la crida el sistema de manera implícita
		g.drawImage(j.bufferImage,0,0,null);
	}

	public void update(Graphics g){
		paint(g);
	}
}
