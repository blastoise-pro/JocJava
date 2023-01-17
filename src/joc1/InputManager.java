package joc1;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

enum Action {
    MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN,
    SHOOT, DODGE,
    PAUSE,
    MENU_OK, MENU_BACK
}

class InputManager implements KeyListener, MouseListener {
    private record Key(int keyCode, boolean isMouse) { }
    private final static int[] keys = {
            KeyEvent.VK_A,      // 0
            KeyEvent.VK_D,      // 1
            KeyEvent.VK_W,      // 2
            KeyEvent.VK_S,      // 3
            KeyEvent.VK_SPACE,  // 4
            KeyEvent.VK_ESCAPE, // 5
            KeyEvent.VK_ENTER,  // 6
            KeyEvent.VK_E       // 7
    };
    private final static int[] mouseButtons = {
            MouseEvent.BUTTON1, // 8
            MouseEvent.BUTTON2, // 9
    };
    private final Map<Action,Key> keyBinds = new HashMap<>();

    private final Map<Key, Boolean> lastUpdateKeyState = new HashMap<>();
    private final Map<Key, Boolean> keyState = new HashMap<>();
    private final Map<Key, Boolean> keyDown = new HashMap<>();
    private final DirectionalInput dir = new DirectionalInput();
    private Vec2 mousePosition = new Vec2();

    InputManager() {
        // Creació dels objectes Key
        for (int key:keys) {
            Key keyObj = new Key(key, false);
            lastUpdateKeyState.put(keyObj, false);
            keyState.put(keyObj, false);
            keyDown.put(keyObj, false);
        }
        for (int mouseButton:mouseButtons) {
            Key keyObj = new Key(mouseButton, true);
            lastUpdateKeyState.put(keyObj, false);
            keyState.put(keyObj, false);
            keyDown.put(keyObj, false);
        }

        // Assignem cada acció a un objecte Key. Després podrem mirar si l'acció està sent activada buscant l'estat
        // de la tecla vinculada.
        // Gameplay
        keyBinds.put(Action.MOVE_LEFT,  new Key(KeyEvent.VK_A,      false));
        keyBinds.put(Action.MOVE_RIGHT, new Key(KeyEvent.VK_D,      false));
        keyBinds.put(Action.MOVE_UP,    new Key(KeyEvent.VK_W,      false));
        keyBinds.put(Action.MOVE_DOWN,  new Key(KeyEvent.VK_S,      false));
        keyBinds.put(Action.SHOOT,      new Key(MouseEvent.BUTTON1, true));
        keyBinds.put(Action.DODGE,      new Key(MouseEvent.BUTTON2, true));
        keyBinds.put(Action.PAUSE,      new Key(KeyEvent.VK_ESCAPE, false));
        // Menu
        keyBinds.put(Action.MENU_OK,   new Key(MouseEvent.BUTTON1, true));
        keyBinds.put(Action.MENU_BACK, new Key(MouseEvent.BUTTON2, true));
    }

    void updateInputs() {
        keyDown.replaceAll( // La tecla ha sigut pulsada si abans no ho estava, i ara sí.
                (k, v) -> !lastUpdateKeyState.get(k) && keyState.get(k)
        );
        lastUpdateKeyState.putAll(keyState);

        dir.clear();
        if (keyState.get(keyBinds.get(Action.MOVE_LEFT))) dir.addInput(Direction.LEFT);
        if (keyState.get(keyBinds.get(Action.MOVE_RIGHT))) dir.addInput(Direction.RIGHT);
        if (keyState.get(keyBinds.get(Action.MOVE_UP))) dir.addInput(Direction.UP);
        if (keyState.get(keyBinds.get(Action.MOVE_DOWN))) dir.addInput(Direction.DOWN);
    }

    void updateMousePosition(Finestra f) {
        Point mousePos = f.getMousePosition();
        if (mousePos == null) {
            return;
        }
        f.updateInversePVMatrix();
        f.inversePVMatrix.transform(mousePos, mousePos);
        mousePosition = new Vec2(mousePos.x, mousePos.y);
    }

    boolean getActionDown(Action action) {
        return keyDown.get(keyBinds.get(action));
    }

    boolean getAction(Action action) {
        return keyState.get(keyBinds.get(action));
    }

    Vec2 getDirection() {
        return dir.getDirection();
    }

    Vec2 getRawDirection() {
        return dir.getRawDirection();
    }

    Vec2 getMousePosition() {
        return mousePosition.clone();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyState.replace(new Key(e.getKeyCode(), false), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyState.replace(new Key(e.getKeyCode(), false), false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        keyState.replace(new Key(e.getButton(), true), true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        keyState.replace(new Key(e.getButton(), true), false);
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
