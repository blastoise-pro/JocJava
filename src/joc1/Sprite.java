package joc1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Sprite {
    private BufferedImage image;
    private final int width, height;
    private Vec2 scale;
    private Vec2 anchor;

    Vec2 getAnchor() {
        return anchor.clone();
    }

    void setAnchor(Vec2 anchor) {
        this.anchor = anchor.clone();
    }

    float getWidth() {
        return width * scale.x;
    }

    float getHeight() {
        return height * scale.y;
    }

    Vec2 getScale() {
        return scale.clone();
    }

    void setScale(Vec2 scale) {
        this.scale = scale.clone();
    }

    Sprite(String path) {
        try {
            image = ImageIO.read(new File(path));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        width = image.getWidth();
        height = image.getHeight();
        anchor = new Vec2(0.5f, 0.5f);
        scale = new Vec2(1, 1);
    }

    Sprite(String path, Vec2 scale) {
        try {
            image = ImageIO.read(new File(path));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        width = image.getWidth();
        height = image.getHeight();
        anchor = new Vec2(0.5f, 0.5f);
        setScale(scale);
    }

    Sprite(String path, Vec2 scale, Vec2 anchor) {
        try {
            image = ImageIO.read(new File(path));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (anchor.x > 1 || anchor.x < 0 || anchor.y > 1 || anchor.y < 0) {
            throw new IllegalArgumentException("The anchor must have both components in range [0,1]");
        }
        width = image.getWidth();
        height = image.getHeight();
        setAnchor(anchor);
        setScale(scale);
    }

    public void pintar(Graphics2D g, AffineTransform PVMMatrix) {
        AffineTransform imageTrans = new AffineTransform(PVMMatrix);
        imageTrans.scale(scale.x, -scale.y);
        imageTrans.translate(-(anchor.x * width), -(anchor.y * height));
        g.drawImage(image, imageTrans, null);
    }
}
