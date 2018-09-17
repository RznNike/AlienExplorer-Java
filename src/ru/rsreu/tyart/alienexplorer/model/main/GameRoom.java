package ru.rsreu.tyart.alienexplorer.model.main;

import ru.rsreu.tyart.alienexplorer.model.main.logic.BaseRoomLogic;
import ru.rsreu.tyart.alienexplorer.model.main.logic.RoomWorkResult;
import ru.rsreu.tyart.alienexplorer.model.main.logic.RoomWorkResultType;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;

import java.util.List;

public class GameRoom {
    private GameRoomType _type;
    private int _id;
    private BaseRoomLogic _roomLogic;
    private List<LevelObject> _levelObjects;
    private List<LevelObject> _doors;
    private List<EnemyObject> _enemies;
    private PlayerObject _player;
    private List<UIObject> _UIObjects;
    private int sizeX;
    private int sizeY;
    private GameModel _parent;


    public GameRoomType getType() {
        return _type;
    }

    public void setType(GameRoomType value) {
        _type = value;
    }

    public RoomWorkResult executeWithResult() {
        // TODO room executeWithResult
        _parent.sendEvent(ModelEventType.MENU_LOADED);
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        return new RoomWorkResult(RoomWorkResultType.LOAD_MAIN_MENU);
    }

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }

    public BaseRoomLogic getRoomLogic() {
        return _roomLogic;
    }

    public void setRoomLogic(BaseRoomLogic value) {
        _roomLogic = value;
    }

    public List<LevelObject> getLevelObjects() {
        return _levelObjects;
    }

    public void setLevelObjects(List<LevelObject> value) {
        _levelObjects = value;
    }

    public List<LevelObject> getDoors() {
        return _doors;
    }

    public void setDoors(List<LevelObject> value) {
        _doors = value;
    }

    public List<EnemyObject> getEnemies() {
        return _enemies;
    }

    public void setEnemies(List<EnemyObject> value) {
        _enemies = value;
    }

    public PlayerObject getPlayer() {
        return _player;
    }

    public void setPlayer(PlayerObject value) {
        _player = value;
    }

    public List<UIObject> getUIObjects() {
        return _UIObjects;
    }

    public void setUIObjects(List<UIObject> value) {
        _UIObjects = value;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int value) {
        sizeX = value;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int value) {
        sizeY = value;
    }

    public GameModel getParent() {
        return _parent;
    }

    public void setParent(GameModel value) {
        _parent = value;
    }
}
