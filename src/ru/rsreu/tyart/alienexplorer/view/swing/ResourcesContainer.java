package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IGameObject;

import java.awt.*;
import java.util.Dictionary;
import java.util.List;

class ResourcesContainer {
    private Dictionary<Integer, Image> _backgrounds;
    private Dictionary<Integer, List<Image>> _levelObjectSprites;
    private Dictionary<Integer, List<Image>> _enemySprites;
    private Dictionary<Integer, List<Image>> _playerSprites;
    private Dictionary<Integer, Image> _UISprites;

    ResourcesContainer(
            Dictionary<Integer, Image> backgrounds,
            Dictionary<Integer, List<Image>> levelObjectSprites,
            Dictionary<Integer, List<Image>> enemySprites,
            Dictionary<Integer, List<Image>> playerSprites,
            Dictionary<Integer, Image> uiSprites)
    {
        _backgrounds = backgrounds;
        _levelObjectSprites = levelObjectSprites;
        _enemySprites = enemySprites;
        _playerSprites = playerSprites;
        _UISprites = uiSprites;
    }

    public Image getSprite(IGameObject gameObject)
    {
//        int sign = gameObject.getFlippedY() ? -1 : 1;
        return  _levelObjectSprites.get(0).get(0);
//        if (typeof(LevelObject).IsInstanceOfType(gameObject))
//        {
//            return _levelObjectSprites[(int)((LevelObject)parObject).Type * sign][parObject.State];
//        }
//        else if (typeof(EnemyObject).IsInstanceOfType(parObject))
//        {
//            return _enemySprites[(int)((EnemyObject)parObject).Type * sign][parObject.State];
//        }
//        else
//        {
//            return _playerSprites[(int)((PlayerObject)parObject).Type * sign][parObject.State];
//        }
    }
}
