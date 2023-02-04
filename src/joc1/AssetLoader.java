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
    public static BufferedImage enemyBattlecruiser;
    public static BufferedImage enemyBomber;
    public static BufferedImage enemyFighter;
    public static BufferedImage enemyFrigate;
    public static List<BufferedImage> explosionSmall;
    public static List<BufferedImage> explosionBig; // 256 x 256
    public static List<BufferedImage> explosionHuge;

    public static void loadAssets() {
        try {
            BufferedImage explosionAtlas = ImageIO.read(new File("assets/SFX/Explosion Animations/Half Sized/explosion 3.png"));
            explosionBig = loadAtlas(explosionAtlas, 8, 8, 2, 62);
            explosionAtlas = ImageIO.read(new File("assets/SFX/Explosion Animations/Half Sized/explosion 4.png"));
            explosionHuge = loadAtlas(explosionAtlas, 8, 8, 1, 63);
            explosionAtlas = ImageIO.read(new File("assets/SFX/Explosion Animations/Half Sized/explosion 1.png"));
            explosionSmall = loadAtlas(explosionAtlas, 8, 8, 2, 62);
            enemyBattlecruiser = ImageIO.read(new File("assets/Ships/Nairan - Battlecruiser - Base.png"));
            enemyBomber = ImageIO.read(new File("assets/Ships/Nairan - Bomber - Base.png"));
            enemyFighter = ImageIO.read(new File("assets/Ships/Nairan - Fighter - Base.png"));
            enemyFrigate = ImageIO.read(new File("assets/Ships/Nairan - Frigate - Base.png"));
            outline(enemyBomber, Color.white);
            outline(enemyFighter, Color.white);
            outline(enemyBattlecruiser, Color.white);
            outline(enemyFrigate, Color.white);
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

    static void printRGB(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                System.out.print(img.getRGB(x, y) + " ");
            }
            System.out.println();
        }
    }

    static void outline(BufferedImage img, Color color) {
        int borderRGB = color.getRGB();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                //System.out.print("(" + x + "," + y + ") ");
                if (img.getRGB(x, y) != 0 && img.getRGB(x,y) != borderRGB) {
                    for (Direction4 dir:Direction4.values()) {
                        if (img.getRGB(x + (int) dir.vector().x, y + (int) dir.vector().y) == 0) {
                            img.setRGB(x + (int) dir.vector().x, y + (int) dir.vector().y, borderRGB);
                        }
                    }
                }
            }
            System.out.println();
        }
    }

}
