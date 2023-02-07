package joc1;

import java.awt.*;
import java.awt.image.BufferedImage;

class StartButton extends Panel {
    private final static BufferedImage normalImage = AssetLoader.border1;
    private final static BufferedImage hoverImage = AssetLoader.border2;
    private final static Font fontStart = new Font("Arial", Font.BOLD, 30);

    StartButton(Joc j, Vec2 position, float rotation, Vec2 dimensions, Vec2 anchor, GUIElement parent) {
        super(j, position, rotation, dimensions, anchor, normalImage, parent);
        new GUIText(j, new Vec2(0.5f, 0.45f), 0, new Vec2(0.5f, 0.5f), "Començar", Color.white, fontStart, this);
    }

    @Override
    public void onMouseClick() {
        j.sceneManager.hideMainMenu();
        j.sceneManager.showGame();
    }

    @Override
    public void onMouseEnter() {
        image = hoverImage;
    }

    @Override
    public void onMouseExit() {
        image = normalImage;
    }
}
