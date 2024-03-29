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
    private static final int BUFFERS_COUNT = 4;

    private static final Color COLOR_TRANSPARENT = new Color(0, 0, 0, 0);
    private static final Color COLOR_MENU_SHADOW = new Color(31, 68, 82, 180);
    private static final Color COLOR_TEXT_PRIMARY = Color.WHITE;
    private static final Color COLOR_TEXT_HEADER = new Color(173, 216, 230);
    private static final Color COLOR_TEXT_SUBHEADER = new Color(255, 255, 0);
    private static final Color COLOR_TEXT_SELECTION = new Color(124, 252, 0);
    private static final Color COLOR_TEXT_HEALTH = new Color(217, 29, 60);

    private static final float FONT_NORMAL_DIVIDER = 16;
    private static final float FONT_BIG_DIVIDER = 8;
    private static final float FONT_SMALL_DIVIDER = 20;
    private static final double HEADER_HEIGHT = 0.2;

    private static final double MAX_VISIBLE_MENU_ITEMS = 7;
    private static final int DEFAULT_CAMERA_WIDTH = 14;

    private static ResourcesContainer _resources;
    private static Font _fontNormal;
    private static Font _fontBig;
    private static Font _fontSmall;

    private static Rectangle2D.Float _camera;
    private static Image[][] _buffers;

    private ModelDrawer() {}

    static void prepareDrawer(Dimension screenSize) {
        _resources = ResourcesLoader.loadResources();
        _fontNormal = _resources.getFont().deriveFont(Font.PLAIN, screenSize.height / FONT_NORMAL_DIVIDER);
        _fontBig = _resources.getFont().deriveFont(Font.PLAIN, screenSize.height / FONT_BIG_DIVIDER);
        _fontSmall = _resources.getFont().deriveFont(Font.PLAIN, screenSize.height / FONT_SMALL_DIVIDER);
        _buffers = new Image[LayerType.values().length][BUFFERS_COUNT];
        for (int i = 0; i < _buffers.length; i++) {
            for (int j = 0; j < _buffers[0].length; j++) {
                _buffers[i][j] = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
            }
        }
    }

    static void resetCamera(IModel model, Dimension screenSize) {
        float width = (float)model.getRoom().getDimension().getWidth();
        if (width > DEFAULT_CAMERA_WIDTH) {
            width = DEFAULT_CAMERA_WIDTH;
        }
        float height = (float)(width * (screenSize.getHeight() / screenSize.getWidth()));
        _camera = new Rectangle2D.Float(
                (float)model.getRoom().getStartCameraPosition().getX(),
                (float)model.getRoom().getStartCameraPosition().getY(),
                (float)Math.ceil(width),
                (float)Math.ceil(height)
        );
    }

    static void drawBackground(IModel model, JLabel layer) {
        Image result = _buffers[LayerType.BACKGROUND.ordinal()][0];
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setBackground(COLOR_TRANSPARENT);
        graphics.clearRect(0, 0, result.getWidth(null), result.getHeight(null));
        Image backgroundTile = _resources.getBackground(model.getRoom().getType().ordinal());
        int imageHeight = backgroundTile.getHeight(layer);
        int imageWidth = backgroundTile.getWidth(layer);
        for (int x = 0; x < layer.getWidth(); x += imageWidth) {
            for (int y = 0; y < layer.getHeight(); y += imageHeight) {
                graphics.drawImage(backgroundTile, x, y, null);
            }
        }

        swapBuffers(LayerType.BACKGROUND.ordinal());
        layer.setIcon(new ImageIcon(result));
    }

    static void drawLevel(IModel model, JLabel layer) {
        moveCamera(model);

        Image result = _buffers[LayerType.LEVEL.ordinal()][0];
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setBackground(COLOR_TRANSPARENT);
        graphics.clearRect(0, 0, result.getWidth(null), result.getHeight(null));

        int blockSize = layer.getWidth() / (int)_camera.getWidth();

        List<GameObject> objectsToDraw = new ArrayList<GameObject>();
        objectsToDraw.addAll(model.getRoom().getLevelObjects());
        objectsToDraw.addAll(model.getRoom().getDoors());
        objectsToDraw.addAll(model.getRoom().getEnemies());
        objectsToDraw.add(model.getRoom().getPlayer());
        for (GameObject object : objectsToDraw) {
            if (object.getCollider().intersects(
                    _camera.x - 1,
                    _camera.y - 1,
                    _camera.width + 2,
                    _camera.height + 2)) {
                Rectangle2D.Float position = new Rectangle2D.Float(
                        (float)(object.getCollider().getX() - _camera.getX()),
                        (float)(object.getCollider().getY() - _camera.getY()),
                        (float)object.getCollider().getWidth(),
                        (float)object.getCollider().getHeight()
                );
                drawGameObjectSprite(graphics, layer.getHeight(), blockSize, object, position);
            }
        }

        swapBuffers(LayerType.LEVEL.ordinal());
        layer.setIcon(new ImageIcon(result));
    }

    static void drawUI(IModel model, JLabel layer) {
        Image result = _buffers[LayerType.UI.ordinal()][0];
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setBackground(COLOR_TRANSPARENT);
        graphics.clearRect(0, 0, result.getWidth(null), result.getHeight(null));

        int blockSize = _fontSmall.getSize();

        List<UIObject> uiObjects = model.getRoom().getUIObjects();
        for (UIObject object : uiObjects) {
            switch (object.getType()) {
                case HEALTH:
                    Rectangle2D.Float rect = new Rectangle2D.Float(0.25f, 0.25f, 1, 1);
                    drawGameObjectSprite(graphics, layer.getHeight(), blockSize, object, rect);

                    String text = String.valueOf(model.getRoom().getPlayer().getHealth());
                    graphics.setColor(COLOR_TEXT_HEALTH);
                    graphics.setFont(_fontSmall);
                    graphics.drawString(text, (int)(blockSize * 1.5), (int)(layer.getHeight() - blockSize * 0.3));
                    break;
            }
        }

        swapBuffers(LayerType.UI.ordinal());
        layer.setIcon(new ImageIcon(result));
    }

    static void drawMenu(IModel model, JLabel layer) {
        Image result = _buffers[LayerType.MENU.ordinal()][0];
        Graphics2D graphics = (Graphics2D)result.getGraphics();
        graphics.setBackground(COLOR_TRANSPARENT);
        graphics.clearRect(0, 0, result.getWidth(null), result.getHeight(null));
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

        swapBuffers(LayerType.MENU.ordinal());
        layer.setIcon(new ImageIcon(result));
    }

    private static void swapBuffers(int layerIndex) {
        Image temp = _buffers[layerIndex][0];
        System.arraycopy(_buffers[layerIndex], 1, _buffers[layerIndex], 0, BUFFERS_COUNT - 1);
        _buffers[layerIndex][BUFFERS_COUNT - 1] = temp;
    }

    private static void drawGameObjectSprite(
            Graphics graphics,
            int canvasHeight,
            int blockSize,
            GameObject object,
            Rectangle2D.Float rect) {
        Image sprite = _resources.getSprite(object);
        graphics.drawImage(
                sprite,
                (int)(rect.getX() * blockSize),
                (int)(canvasHeight - (rect.getY() + rect.getHeight()) * blockSize),
                (int)(rect.getWidth() * blockSize + 1),
                (int)(rect.getHeight() * blockSize + 1),
                null);
    }

    private static void drawCenteredString(Graphics graphics, String text, Rectangle2D.Float rect, Font font) {
        if ((text == null) || ("".equals(text))) {
            return;
        }

        FontMetrics metrics = graphics.getFontMetrics(font);
        int x = (int)(rect.getX() + (rect.getWidth() - metrics.stringWidth(text)) / 2);
        int y = (int)(rect.getY() + ((rect.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent());
        graphics.setFont(font);
        graphics.drawString(text, x, y);
    }

    private static void moveCamera(IModel model) {
        float newX;
        float newY;
        float epsilon = 0.001f;

        Rectangle2D.Float playerCollider = model.getRoom().getPlayer().getCollider();
        newX = (float)(playerCollider.getX() + (playerCollider.getWidth() - _camera.getWidth()) / 2);
        newY = (float)(playerCollider.getY() + (playerCollider.getHeight() - _camera.getHeight()) / 2);

        float roomWidth = (float)model.getRoom().getDimension().getWidth();
        if (newX < 0) {
            newX = 0;
        } else if ((_camera.getWidth() < (roomWidth + epsilon))
                && (newX + _camera.getWidth() > roomWidth)) {
            newX = roomWidth - (float)_camera.getWidth();
        }

        float roomHeight = (float)model.getRoom().getDimension().getHeight();
        if (newY < 0) {
            newY = 0;
        } else if ((_camera.getHeight() < (roomHeight + epsilon))
                && (newY + _camera.getHeight() > roomHeight)) {
            newY = roomHeight - (float)_camera.getHeight();
        }

        _camera.x = newX;
        _camera.y = newY;
    }
}
