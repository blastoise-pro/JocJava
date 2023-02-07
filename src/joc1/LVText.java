package joc1;

import java.awt.*;

class LVText extends GUIText {
    LVText(Joc j, Vec2 position, float rotation, Vec2 anchor, Color color, Font font, GUIElement parent) {
        super(j, position, rotation, anchor, "", color, font, parent);
    }

    void update() {
        updateText("Level " + j.playerShip.level);
    }
}
