package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObjectType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

class ModelDrawer {
    private static final Color COLOR_MENU_SHADOW = new Color(31, 68, 82, 180);
    private static final Color COLOR_TEXT_PRIMARY = Color.CYAN;
    private static final Color COLOR_TEXT_HEADER = Color.BLUE;
    private static final Color COLOR_TEXT_SELECTION = Color.YELLOW;

    private static ResourcesContainer _resources;

    private ModelDrawer() {}

    static void drawBackground(IModel model, JLabel layer) {
        if (_resources == null) {
            _resources = ResourcesLoader.loadResources();
        }

        Image result = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = result.getGraphics();
        Image backgroundTile = _resources.getBackground(model.getRoom().getType().ordinal());
        int imageHeight = backgroundTile.getHeight(layer);
        int imageWidth = backgroundTile.getWidth(layer);
        for (int x = 0; x < layer.getWidth(); x += imageWidth) {
            for (int y = 0; y < layer.getHeight(); y += imageHeight) {
                graphics.drawImage(backgroundTile, x, y, null);
            }
        }

        setImageToLayer(layer, result);
    }

    static void drawLevel(IModel model, JLabel layer) {
    }

    static void drawUI(IModel model, JLabel layer) {
    }

    static void drawMenu(IModel model, JLabel layer) {
        Image result = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = result.getGraphics();
        graphics.setColor(COLOR_MENU_SHADOW);
        graphics.fillRect(0, 0, layer.getWidth(), layer.getHeight());
        // TODO custom font
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        List<UIObject> uiObjects = model.getRoom().getUIObjects();
        int offsetY = 0;
        for (UIObject object : uiObjects) {
            offsetY += 50;
            String text;
            if (object.getType() == UIObjectType.TEXT) {
                text = object.getText();
            } else {
                text = object.getType().toString();
            }
            if (object.getState() == 0) {
                graphics.setColor(COLOR_TEXT_PRIMARY);
            } else {
                graphics.setColor(COLOR_TEXT_SELECTION);
            }
            graphics.drawString(text, 100, offsetY);
        }

        setImageToLayer(layer, result);
    }

    private static void setImageToLayer(JLabel layer, Image result) {
        layer.setIcon(new ImageIcon(result));
    }
}
