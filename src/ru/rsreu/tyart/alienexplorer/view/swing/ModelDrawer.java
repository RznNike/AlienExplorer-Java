package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.model.object.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class ModelDrawer {
    private static final Color COLOR_TRANSPARENT = new Color(0, 0, 0, 0);
    private static final Color COLOR_MENU_SHADOW = new Color(31, 68, 82, 180);
    private static final Color COLOR_TEXT_PRIMARY = Color.WHITE;
    private static final Color COLOR_TEXT_HEADER = new Color(173, 216, 230);
    private static final Color COLOR_TEXT_SUBHEADER = new Color(255, 255, 0);
    private static final Color COLOR_TEXT_SELECTION = new Color(124, 252, 0);

    private static final float FONT_NORMAL_DIVIDER = 16;
    private static final float FONT_BIG_DIVIDER = 8;
    private static final double HEADER_HEIGHT = 0.2;

    private static final double MAX_VISIBLE_MENU_ITEMS = 7;

    private static final int CAMERA_WIDTH = 15;

    private static ResourcesContainer _resources;
    private static Font _fontNormal;
    private static Font _fontBig;

    private ModelDrawer() {}

    static void prepareDrawer(Dimension screenSize) {
        _resources = ResourcesLoader.loadResources();
        _fontNormal = _resources.getFont().deriveFont(Font.PLAIN, screenSize.height / FONT_NORMAL_DIVIDER);
        _fontBig = _resources.getFont().deriveFont(Font.PLAIN, screenSize.height / FONT_BIG_DIVIDER);
    }

    static void drawBackground(IModel model, JLabel layer) {
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

        layer.setIcon(new ImageIcon(result));
    }

    static void drawLevel(IModel model, JLabel layer) {
        Image result = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = result.getGraphics();
        graphics.setColor(COLOR_TRANSPARENT);
        graphics.fillRect(0, 0, layer.getWidth(), layer.getHeight());

        int blockSize = layer.getWidth() / CAMERA_WIDTH;

        List<GameObject> objectsToDraw = new ArrayList<GameObject>();
        objectsToDraw.addAll(model.getRoom().getLevelObjects());
        objectsToDraw.addAll(model.getRoom().getDoors());
        objectsToDraw.addAll(model.getRoom().getEnemies());
        objectsToDraw.add(model.getRoom().getPlayer());
        for (GameObject object : objectsToDraw) {
            drawGameObjectSprite(graphics, layer.getHeight(), blockSize, object);
        }

        // level
//        List<LevelObject> levelObjects = model.getRoom().getLevelObjects();
//        for (LevelObject object : levelObjects) {
//            drawGameObjectSprite(graphics, layer.getHeight(), blockSize, object);
//        }
//        // doors
//        List<LevelObject> doors = model.getRoom().getDoors();
//        for (LevelObject object : levelObjects) {
//            drawGameObjectSprite(graphics, layer.getHeight(), blockSize, object);
//        }
//        // enemies
//        List<EnemyObject> enemies = model.getRoom().getEnemies();
//        for (EnemyObject enemy : enemies) {
//            drawGameObjectSprite(graphics, layer.getHeight(), blockSize, enemy);
//        }
//        // player
//        drawGameObjectSprite(graphics, layer.getHeight(), blockSize, model.getRoom().getPlayer());

        layer.setIcon(new ImageIcon(result));
    }

    private static void drawGameObjectSprite(Graphics graphics, int canvasHeight, int blockSize, GameObject object) {
        Image sprite = _resources.getSprite(object);
        Rectangle2D rect = object.getCollider();
        graphics.drawImage(
                sprite,
                (int)(rect.getX() * blockSize),
                (int)(canvasHeight - (rect.getY() + rect.getHeight()) * blockSize),
                (int)(rect.getWidth() * blockSize),
                (int)(rect.getHeight() * blockSize),
                null);
    }

    static void drawUI(IModel model, JLabel layer) {
    }

    static void drawMenu(IModel model, JLabel layer) {
        Image result = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = result.getGraphics();
        graphics.setColor(COLOR_MENU_SHADOW);
        graphics.fillRect(0, 0, layer.getWidth(), layer.getHeight());

        // header
        graphics.setColor(COLOR_TEXT_HEADER);
        drawCenteredString(
                graphics,
                model.getRoom().getMenuHeader(),
                new Rectangle2D.Float(0, 0, layer.getWidth(), (int)(layer.getHeight() * HEADER_HEIGHT)),
                _fontBig);

        // items
        graphics.setFont(_fontNormal);
        List<UIObject> uiObjects = model.getRoom().getUIObjects();
        int itemHeight = (int)((layer.getHeight() * (1 - HEADER_HEIGHT)) / (MAX_VISIBLE_MENU_ITEMS + 1));
        int offsetY = (int)(layer.getHeight() * HEADER_HEIGHT);
        for (UIObject object : uiObjects) {
            offsetY += itemHeight;
            String text;
            if (object.getType() == UIObjectType.TEXT) {
                text = object.getText();
            } else {
                text = object.getType().toString();
            }
            if (object.isSelectable()) {
                if (object.getState() == 1) {
                    graphics.setColor(COLOR_TEXT_SELECTION);
                } else {
                    graphics.setColor(COLOR_TEXT_PRIMARY);
                }
            } else {
                graphics.setColor(COLOR_TEXT_SUBHEADER);
            }

            drawCenteredString(
                    graphics,
                    text,
                    new Rectangle2D.Float(0, offsetY, layer.getWidth(), itemHeight),
                    _fontNormal);
        }

        layer.setIcon(new ImageIcon(result));
    }

    private static void drawCenteredString(Graphics graphics, String text, Rectangle2D rect, Font font) {
        FontMetrics metrics = graphics.getFontMetrics(font);
        int x = (int)(rect.getX() + (rect.getWidth() - metrics.stringWidth(text)) / 2);
        int y = (int)(rect.getY() + ((rect.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent());
        graphics.setFont(font);
        graphics.drawString(text, x, y);
    }
}
