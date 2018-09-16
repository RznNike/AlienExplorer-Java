package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class ModelDrawer {
    private ModelDrawer() {}

    static void drawBackground(IModel model, ResourcesContainer resources, JLabel layer) {
        // TODO fix bug with layout on start
        if ((layer == null) || (layer.getWidth() == 0) || (layer.getHeight() == 0)) {
            return;
        }
        Image result = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // TODO take background accordingly to model type
        Image backgroundTile = resources.getBackground();
        result.getGraphics().drawImage(backgroundTile, 0, 0, null);
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
