package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IGameObject;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;

import java.awt.*;
import java.util.List;
import java.util.Map;

class ResourcesContainer {
    private Map<Integer, Image> _backgrounds;
    private Map<Integer, List<Image>> _levelObjectSprites;
    private Map<Integer, List<Image>> _enemySprites;
    private Map<Integer, List<Image>> _playerSprites;
    private Map<Integer, Image> _UISprites;
    private Font _font;

    ResourcesContainer(
            Map<Integer, Image> backgrounds,
            Map<Integer, List<Image>> levelObjectSprites,
            Map<Integer, List<Image>> enemySprites,
            Map<Integer, List<Image>> playerSprites,
            Map<Integer, Image> uiSprites,
            Font font) {
        _backgrounds = backgrounds;
        _levelObjectSprites = levelObjectSprites;
        _enemySprites = enemySprites;
        _playerSprites = playerSprites;
        _UISprites = uiSprites;
        _font = font;
    }

    Image getSprite(IGameObject gameObject) {
        int sign = gameObject.getFlippedY() ? -1 : 1;
        Map<Integer, List<Image>> sprites;
        if (gameObject instanceof LevelObject) {
            sprites = _levelObjectSprites;
        } else if (gameObject instanceof EnemyObject) {
            sprites = _enemySprites;
        } else if (gameObject instanceof PlayerObject) {
            sprites = _playerSprites;
        } else if (gameObject instanceof UIObject) {
            return _UISprites.get(gameObject.getTypeNumber());
        } else {
            return null;
        }
        return sprites
                .get(gameObject.getTypeNumber()* sign)
                .get(gameObject.getState());
    }

    Image getBackground(int number) {
        return _backgrounds.get(number);
    }

    Font getFont() {
        return _font;
    }
}
