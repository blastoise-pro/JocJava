package joc1;

import java.awt.Color;
import java.awt.Graphics;

public class Cotxe {
	Vec2 pos;
	int v;
	int ample=70, alt=40; //minuscules i no majúscules -> variables i no constants

	Cotxe(int x, int y, int v) {
		pos = new Vec2(x, y);
		this.v = v;
	}
	
	void moure(DirectionalInput dirInp) {
		pos.Add(dirInp.getDirection().scale(v));
	}
	
	void pintar (Graphics g) {
		int x = (int) pos.x;
		int y = (int) -pos.y + Finestra.ALÇADA;
		g.setColor(Color.BLACK);
		g.drawRect(x, y, ample, alt);
		g.drawLine((int)(x+ample*0.75), y, (int)(x+ample*0.75), y+alt);
	}
	
}
