package joc1;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

class Finestra extends Frame {
	private final Joc j;
	private final static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	BufferStrategy bstrat;
	//Graphics2D fGraphics;
	boolean changing = false;

	int winHeight = 900, winWidth = 1500;
	boolean fullscreen = false;

	Finestra(Joc joc) {
		super("Joc");
		setSize(winWidth, winHeight);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		setIgnoreRepaint(true);
		setVisible(true);
		createBufferStrategy(2);
		// Alerta: Si tens targeta Nvidia recomano desactivar la sincronització vertical o seleccionar el mode ràpid
		// al panell de control per disminuir l'input lag en pantalla completa
		bstrat = getBufferStrategy();
		System.out.println(device.isFullScreenSupported());
		System.out.println(bstrat.getCapabilities().isPageFlipping());
		j = joc;

		/*
		fGraphics = (Graphics2D) getGraphics();
		fGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		fGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		j.bufferImage = createImage(winWidth, winHeight);
		j.g2buff = (Graphics2D)j.bufferImage.getGraphics(); //és el graphics de la imatge, no de la pantalla!
		j.g2buff.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		j.g2buff.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		// Per dibuixar amb subpixel accuracy
		*/

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
				windowResized(e);
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

		Camera.updateGUIMatrices(this);
		System.out.println(getInsets().left);
	}

	private void windowResized(ComponentEvent e) {
		winHeight = e.getComponent().getHeight();
		winWidth = e.getComponent().getWidth();
		/*
		fGraphics = (Graphics2D) getGraphics(); // Aquest objecte canvia al canviar el tamany de la finestra
		fGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		fGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		j.bufferImage = createImage(winWidth, winHeight);
		j.g2buff = (Graphics2D)j.bufferImage.getGraphics();
		*/
		repaint();

		j.camera.updateProjectionMatrix();
		Camera.updateGUIMatrices(this);
	}

	private void toggleFullscreen() {
		if (fullscreen) {
			changing = true;
			dispose();
			setUndecorated(false);
			device.setFullScreenWindow(null);
			setResizable(true);
			setVisible(true);
			fullscreen = false;
			repaint();
			changing = false;
		}
		else {
			if (device.isFullScreenSupported()) {
				changing = true;
				dispose();
				setUndecorated(true);
				device.setFullScreenWindow(this);
				setResizable(false);
				fullscreen = true;
				repaint();
				changing = false;
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

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		j.render(g2);
	}
}
