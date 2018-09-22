package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IGameObject;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;

import java.awt.*;
import java.util.Dictionary;
import java.util.List;

class ResourcesContainer {
    private Dictionary<Integer, Image> _backgrounds;
    private Dictionary<Integer, List<Image>> _levelObjectSprites;
    private Dictionary<Integer, List<Image>> _enemySprites;
    private Dictionary<Integer, List<Image>> _playerSprites;
    private Dictionary<Integer, Image> _UISprites;
    private Font _font;

    ResourcesContainer(
            Dictionary<Integer, Image> backgrounds,
            Dictionary<Integer, List<Image>> levelObjectSprites,
            Dictionary<Integer, List<Image>> enemySprites,
            Dictionary<Integer, List<Image>> playerSprites,
            Dictionary<Integer, Image> uiSprites,
            Font font) {
        _backgrounds = backgrounds;
        _levelObjectSprites = levelObjectSprites;
        _enemySprites = enemySprites;
        _playerSprites = playerSprites;
        _UISprites = uiSprites;
        _font = font;
    }

    public Image getSprite(IGameObject gameObject) {
        int sign = gameObject.getFlippedY() ? -1 : 1;
        Dictionary<Integer, List<Image>> dictionary;
        if (gameObject instanceof LevelObject) {
            dictionary = _levelObjectSprites;
        } else if (gameObject instanceof EnemyObject) {
            dictionary = _enemySprites;
        } else if (gameObject instanceof PlayerObject) {
            dictionary = _playerSprites;
        } else if (gameObject instanceof UIObject) {
            return _UISprites.get(gameObject.getTypeNumber());
        } else {
            return null;
        }
        return dictionary
                .get(gameObject.getTypeNumber()* sign)
                .get(gameObject.getState());
    }

    public Image getBackground(int number) {
        return _backgrounds.get(number);
    }

    public Font getFont() {
        return _font;
    }
}
