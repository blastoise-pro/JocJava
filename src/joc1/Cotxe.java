package joc1;

import java.awt.Color;
import java.awt.Graphics;

public class Cotxe {
	int x,y,v;
	int ample=70, alt=40; //minuscules i no majúscules -> variables i no constants

	Cotxe(int x, int y, int v) {
		this.x = x;
		this.y = y;
		this.v = v;
	}
	
	void moure(int direct) {
		if (dir & )
		x+=v;
	}
	
	void pintar (Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(x, y, ample, alt);
		g.drawLine((int)(x+ample*0.75), y, (int)(x+ample*0.75), y+alt);
	}
	
}
