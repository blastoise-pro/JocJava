package joc1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class AssetLoader {
    public static BufferedImage playerShip1;
    public static BufferedImage enemyShip1;
    public static List<BufferedImage> explosion1; // 256 x 256
    public static List<BufferedImage> explosion2;

    public static void loadAssets() {
        try {
            BufferedImage explosionAtlas = ImageIO.read(new File("assets/SFX/Explosion Animations/Half Sized/explosion 3.png"));
            explosion1 = loadAtlas(explosionAtlas, 8, 8, 2, 62);
            explosionAtlas = ImageIO.read(new File("assets/SFX/Explosion Animations/Half Sized/explosion 4.png"));
            explosion2 = loadAtlas(explosionAtlas, 8, 8, 1, 63);
            enemyShip1 = ImageIO.read(new File("assets/Ships/Nairan - Battlecruiser - Base.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<BufferedImage> loadAtlas(BufferedImage atlas, int rows, int cols, int offset, int count) {
        List<BufferedImage> sprites = new ArrayList<>();
        int spriteWidth = atlas.getWidth()/cols;
        int spriteHeight = atlas.getHeight()/rows;
        int startCol = offset % cols;
        int startRow = offset / cols;
        int x = startCol * spriteWidth;
        int y = startRow * spriteHeight;

        for (int i = 0; i < count; i++) {
            sprites.add(atlas.getSubimage(x, y, spriteWidth, spriteHeight));
            x += spriteWidth;
            if (x >= spriteWidth * cols) {
                x = 0;
                y += spriteHeight;
            }
        }

        return sprites;
    }

    static List<BufferedImage> loadAtlas(BufferedImage atlas, int rows, int cols, int startx, int starty, int width, int height, int offset, int count) {
        return null;
    }
}
