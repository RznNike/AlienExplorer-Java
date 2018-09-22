package ru.rsreu.tyart.alienexplorer.model;

import ru.rsreu.tyart.alienexplorer.model.main.GameRoomType;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;

import java.util.List;

public interface IGameRoom {
    GameRoomType getType();
    List<LevelObject> getLevelObjects();
    List<LevelObject> getDoors();
    List<EnemyObject> getEnemies();
    PlayerObject getPlayer();
    List<UIObject> getUIObjects();
}
