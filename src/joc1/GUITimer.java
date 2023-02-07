package joc1;

import java.awt.*;

public class GUITimer extends GUIText {

    GUITimer(Joc j, Vec2 position, Vec2 anchor) {
        super(j, position, 0, anchor, "", Color.white, new Font("Arial", Font.BOLD, 30), null);
    }

    void update() {
        updateText(Time.getFormattedTime(Time.time() - j.gameController.startTime));
    }
}
