package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class ModelDrawer {
    private ModelDrawer() {}

    static void drawBackground(IModel model, ResourcesContainer resources, JLabel layer)
            throws IllegalArgumentException {
        Image result = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = result.getGraphics();
        Image backgroundTile = resources.getBackground(model.getRoomType().ordinal());
        int imageHeight = backgroundTile.getHeight(layer);
        int imageWidth = backgroundTile.getWidth(layer);
        for (int x = 0; x < layer.getWidth(); x += imageWidth) {
            for (int y = 0; y < layer.getHeight(); y += imageHeight) {
                graphics.drawImage(backgroundTile, x, y, null);
            }
        }
        if (layer.getIcon() == null) {
            layer.setIcon(new ImageIcon(result));
        } else {
            ((ImageIcon)layer.getIcon()).setImage(result);
        }
    }

    static void drawLevel(IModel model, ResourcesContainer resources, JLabel layer) {
    }

    static void drawUI(IModel model, ResourcesContainer resources, JLabel layer) {
    }

    static void drawMenu(IModel model, ResourcesContainer resources, JLabel layer) {
    }
}
