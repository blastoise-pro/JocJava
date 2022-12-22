package joc1;

import java.awt.event.*;

public class InputManager implements KeyListener {
    DirectionalInput dir = new DirectionalInput();
    boolean space = false;

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> dir.addInput(Direction.LEFT);
            case KeyEvent.VK_D -> dir.addInput(Direction.RIGHT);
            case KeyEvent.VK_S -> dir.addInput(Direction.DOWN);
            case KeyEvent.VK_W -> dir.addInput(Direction.UP);
            case KeyEvent.VK_SPACE -> space = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> dir.removeInput(Direction.LEFT);
            case KeyEvent.VK_D -> dir.removeInput(Direction.RIGHT);
            case KeyEvent.VK_S -> dir.removeInput(Direction.DOWN);
            case KeyEvent.VK_W -> dir.removeInput(Direction.UP);
            case KeyEvent.VK_SPACE -> space = false;
        }
    }
}
