package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.controller.GameController;
import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEvent;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;
import ru.rsreu.tyart.alienexplorer.view.ModelEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame implements ModelEventListener {
    private static final int LAYERS_COUNT = 4;
    private static final int BACKGROUND_LAYER = 3;
    private static final int LEVEL_LAYER = 2;
    private static final int UI_LAYER = 1;
    private static final int MENU_LAYER = 0;

    private JPanel _canvas;
    private List<JLabel> _layers;
    private ModelEventHandler _modelEventHandler;

    public MainForm(GameController controller) {
        controller.subscribeViewToModel(this);

        setContentPane(_canvas);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        addKeyListener(new KeyboardListener(controller));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initLayers();
        hideCursor();

        ModelDrawer.prepareDrawer(Toolkit.getDefaultToolkit().getScreenSize());

        _modelEventHandler = new ModelEventHandler(this);
    }

    private void initLayers() {
        JLayeredPane layeredCanvas = getLayeredPane();
        _layers = new ArrayList<JLabel>();
        for (int i = 0; i < LAYERS_COUNT; i++) {
            JLabel layer = new JLabel();
            layer.setSize(_canvas.getSize());
            _layers.add(layer);
            layeredCanvas.add(layer);
            layer.setVisible(true);
        }
        for (int i = 0; i < _layers.size(); i++) {
            layeredCanvas.setLayer(_layers.get(i), LAYERS_COUNT - i);
        }
    }

    private void hideCursor() {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        this.getLayeredPane().setCursor(blankCursor);
    }

    @Override
    public void onModelEvent(ModelEvent event) {
        _modelEventHandler.addEvent(event);
    }

    void processEvent(ModelEvent event) {
        IModel model = event.getSender();
        ModelEventType eventType = event.getEventType();

        if ((_layers.get(0).getSize().width == 0) || (_layers.get(0).getSize().height == 0)) {
            for (JLabel layer : _layers) {
                layer.setSize(_canvas.getSize());
            }
        }

        switch (eventType) {
            case MENU_LOADED:
                ModelDrawer.drawBackground(model, _layers.get(BACKGROUND_LAYER));
                ModelDrawer.drawMenu(model, _layers.get(MENU_LAYER));
                _layers.get(LEVEL_LAYER).setVisible(false);
                _layers.get(UI_LAYER).setVisible(false);
                _layers.get(MENU_LAYER).setVisible(true);
                break;
            case MENU_CHANGED:
                ModelDrawer.drawMenu(model, _layers.get(MENU_LAYER));
                break;
            case LEVEL_LOADED:
                ModelDrawer.drawBackground(model, _layers.get(BACKGROUND_LAYER));
                ModelDrawer.drawLevel(model, _layers.get(LEVEL_LAYER));
                ModelDrawer.drawUI(model, _layers.get(UI_LAYER));
                _layers.get(LEVEL_LAYER).setVisible(true);
                _layers.get(UI_LAYER).setVisible(true);
                _layers.get(MENU_LAYER).setVisible(false);
                break;
            case LEVEL_CHANGED:
                ModelDrawer.drawLevel(model, _layers.get(LEVEL_LAYER));
                break;
            case UI_CHANGED:
                ModelDrawer.drawUI(model, _layers.get(UI_LAYER));
                break;
            case CONTEXT_MENU_LOADED:
                ModelDrawer.drawMenu(model, _layers.get(MENU_LAYER));
                _layers.get(MENU_LAYER).setVisible(true);
                break;
            case CONTEXT_MENU_CLOSED:
                _layers.get(MENU_LAYER).setVisible(false);
                break;
        }
    }
}
