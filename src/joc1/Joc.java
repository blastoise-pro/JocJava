package joc1;

import java.awt.Color;
import java.awt.event.*;

public class Joc {
	
	Finestra f;
	InputManager input = new InputManager();
	Cotxe[] c = new Cotxe [3]; //aquí no estem creant cotxes, només guardant espai per un apuntador a 3 cotxes
	Joc (Finestra f) { // Fem això per lligar les classes finestra i joc
		this.f=f;
		f.addKeyListener(input);
	}
	
	void run () {
		// inicialitzacio dels objectes del joc (naus...)
//		y=50;
		c[0] = new Cotxe (300,0,1); // l'ideal seria una funció inicialització i deixar el codi principal net
		c[1] = new Cotxe (50,100,2);
		c[2] = new Cotxe (10,200,4);
		
		while (true) { // bucle infinit que pararem amb l'stop
			moureElements(input.dir); // moure els objectes de pantalla
			// detectar col·lisions
			repintaPantalla(); // repintar la pantalla

			if (input.space) {
				System.out.println("Shoot");
			}

			try { 
				Thread.sleep(50); // afegim un retard de 50 ms per no sobrecarregar la màquina
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	void moureElements(DirectionalInput dir) {
//		y++;
		for (Cotxe cotxe:c) //for de tipus "foreach", alternativa més senzilla que i=0, i<c.length, i++ c[i].moure... cotxe és variable de tipus Cotxe. c una llista. 
			cotxe.moure(dir);
	}
	
	void repintaPantalla() {
		f.g.setColor(Color.WHITE);
		f.g.fillRect(0,0,f.AMPLADA,f.ALÇADA); //rectangle
		f.g.setColor(Color.RED);
		//f.g.drawLine(50, 50, 500, 500);
		//f.g.drawRect(0, 0, f.AMPLADA, f.ALÇADA);
		for (Cotxe cotxe:c) 
			cotxe.pintar(f.g);
		f.repaint(); // obliga la paint a executar-se. veurem el canvi a pantalla
	}
}
