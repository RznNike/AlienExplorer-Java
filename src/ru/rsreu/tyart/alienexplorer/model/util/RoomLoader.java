package ru.rsreu.tyart.alienexplorer.model.util;

import ru.rsreu.tyart.alienexplorer.model.main.GameModel;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoomType;
import ru.rsreu.tyart.alienexplorer.model.main.logic.MenuLogic;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;

import java.util.ArrayList;

public class RoomLoader {
    public static GameRoom loadMainMenu(GameModel parent) {
        GameRoom room = new GameRoom();
        room.setType(GameRoomType.MENU);
        room.setLevelObjects(new ArrayList<LevelObject>());
        room.setDoors(new ArrayList<LevelObject>());
        room.setEnemies(new ArrayList<EnemyObject>());
        room.setUIObjects(new ArrayList<UIObject>());
        room.setRoomLogic(new MenuLogic(room));
        room.setParent(parent);

        return room;
    }

    public static GameRoom loadLevel(GameModel parent, int levelID) {
        return null;
    }
}
