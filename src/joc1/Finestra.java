package joc1;

import java.awt.*;

public class Finestra extends Frame {

	Joc j; //variables globals
	Image im;
	Graphics g; //pintarem aquest graphics
	final int ALÇADA = 600, AMPLADA = 1000;
	
	public static void main(String[] args) {
		new Finestra();
	
	}
	
	Finestra() {
		super("Joc");
		setSize(AMPLADA,ALÇADA);
		setVisible(true); // en algun moment el vicenç em va dir de posar-ho entre j=newJoc i j.run perquè així no se'm queixava, però ara alla no compilava i aquí perfecte

		im = createImage(AMPLADA,ALÇADA);
		g = im.getGraphics(); //és el graphics de la imatge, no de la pantalla!
		// després el copiarem per representar-lo (així evitem parpellejos...)
		j = new Joc(this); //Passar la pròpia finestra
		j.run();
	}
	
	public void paint (Graphics g) { // la crida el sistema de manera implícita
		g.drawImage(im,0,0,null);
	}
	
	public void update(Graphics g) {
		paint(g);
	}

}
