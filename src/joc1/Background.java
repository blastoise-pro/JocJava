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
        super(j, new Vec2(0, 0), 0, new Vec2(.01f, .01f));
        try {
            bg = ImageIO.read(new File(path));
        }
        catch (IOException e) {
            e.printStackTrace();
            j.destroy(this);
        }
        setPosition(new Vec2(j.camera.l, j.camera.t));
        setScale(new Vec2((j.camera.r - j.camera.l)/bg.getWidth(), -(j.camera.t - j.camera.b)/bg.getHeight()));
    }

    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());
        g.drawImage(bg, PVM, null);
    }
}
