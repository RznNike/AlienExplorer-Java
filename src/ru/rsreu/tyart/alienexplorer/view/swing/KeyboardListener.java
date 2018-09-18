package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.controller.GameController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private GameController _controller;

    public KeyboardListener(GameController controller) {
        _controller = controller;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        sendCommand(key, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        sendCommand(key, false);
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    private void sendCommand(int key, boolean isThisACommandStart) {
        switch (key) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_SPACE:
                _controller.sendCommand(ControllerCommand.UP, isThisACommandStart);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                _controller.sendCommand(ControllerCommand.LEFT, isThisACommandStart);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                _controller.sendCommand(ControllerCommand.DOWN, isThisACommandStart);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                _controller.sendCommand(ControllerCommand.RIGHT, isThisACommandStart);
                break;
            case KeyEvent.VK_ESCAPE:
                _controller.sendCommand(ControllerCommand.ESC, isThisACommandStart);
                break;
            case KeyEvent.VK_ENTER:
                _controller.sendCommand(ControllerCommand.OK, isThisACommandStart);
                break;
        }
    }
}
