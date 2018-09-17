package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.controller.GameController;
import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEvent;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;
import ru.rsreu.tyart.alienexplorer.view.ModelEventListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame implements KeyListener, ModelEventListener {
    private static final int LAYERS_COUNT = 4;
    private static final int BACKGROUND_LAYER = 3;
    private static final int LEVEL_LAYER = 2;
    private static final int UI_LAYER = 1;
    private static final int MENU_LAYER = 0;

    private JPanel _canvas;
    private List<JLabel> _layers;
    private ResourcesContainer _resources;
    private GameController _controller;

    public MainForm(GameController controller) {
        _controller = controller;
        _controller.subscribeViewToModel(this);

        setContentPane(_canvas);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        addKeyListener(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initLayers();

        _resources = ResourcesLoader.loadResources();
    }

    private void initLayers() {
        JLayeredPane _layeredCanvas = getLayeredPane();
        _layers = new ArrayList<JLabel>();
        for (int i = 0; i < LAYERS_COUNT; i++) {
            JLabel layer = new JLabel();
            layer.setSize(_canvas.getSize());
            _layers.add(layer);
            _layeredCanvas.add(layer);
            layer.setVisible(true);
        }
        for (int i = 0; i < _layers.size(); i++) {
            _layeredCanvas.setLayer(_layers.get(i), i);
        }
    }

    @Override
    public void onModelEvent(ModelEvent event) {
        IModel model = event.getSender();
        ModelEventType eventType = event.getEventType();
        try {
            switch (eventType) {
                case LEVEL_LOADED:
                    ModelDrawer.drawBackground(model, _resources, _layers.get(BACKGROUND_LAYER));
                    ModelDrawer.drawLevel(model, _resources, _layers.get(LEVEL_LAYER));
                    ModelDrawer.drawUI(model, _resources, _layers.get(UI_LAYER));
                    _layers.get(MENU_LAYER).setVisible(false);
                    break;
                case MENU_LOADED:
                    ModelDrawer.drawMenu(model, _resources, _layers.get(MENU_LAYER));
                    _layers.get(MENU_LAYER).setVisible(true);
                    break;
                case LEVEL_CHANGED:
                    ModelDrawer.drawLevel(model, _resources, _layers.get(LEVEL_LAYER));
                    break;
                case UI_CHANGED:
                    ModelDrawer.drawUI(model, _resources, _layers.get(UI_LAYER));
                    break;
                case CONTEXT_MENU_LOADED:
                    ModelDrawer.drawMenu(model, _resources, _layers.get(MENU_LAYER));
                    _layers.get(MENU_LAYER).setVisible(true);
                    break;
                case CONTEXT_MENU_CHANGED:
                    ModelDrawer.drawMenu(model, _resources, _layers.get(MENU_LAYER));
                    break;
                case CONTEXT_MENU_CLOSED:
                    _layers.get(MENU_LAYER).setVisible(false);
                    break;
            }
        } catch (IllegalArgumentException e) {
            for (JLabel layer : _layers) {
                layer.setSize(_canvas.getSize());
            }
            onModelEvent(event);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_SPACE:
                _controller.sendCommand(ControllerCommand.UP);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                _controller.sendCommand(ControllerCommand.LEFT);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                _controller.sendCommand(ControllerCommand.DOWN);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                _controller.sendCommand(ControllerCommand.RIGHT);
                break;
            case KeyEvent.VK_ESCAPE:
                _controller.sendCommand(ControllerCommand.ESC);
                break;
            case KeyEvent.VK_ENTER:
                _controller.sendCommand(ControllerCommand.OK);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}