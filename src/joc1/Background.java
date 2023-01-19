package joc1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Background extends GameObject {
    BufferedImage bg = null;

    Background(Joc j, String path) {
        super(j, new Vec2(-50, -50), 0, new Vec2(1, 1));
        try {
            bg = ImageIO.read(new File(path));
        }
        catch (IOException e) {
            e.printStackTrace();
            j.destroy(this);
        }
    }

    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());
        g.drawImage(bg, PVM, null);
    }
}
