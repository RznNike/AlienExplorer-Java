package ru.rsreu.tyart.alienexplorer.model.main;

import ru.rsreu.tyart.alienexplorer.model.IGameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.BaseRoomLogic;
import ru.rsreu.tyart.alienexplorer.model.main.logic.MenuLogic;
import ru.rsreu.tyart.alienexplorer.model.main.logic.RoomWorkResult;
import ru.rsreu.tyart.alienexplorer.model.main.logic.RoomWorkResultType;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GameRoom implements IGameRoom {
    private GameRoomType _type;
    private int _id;
    private BaseRoomLogic _roomLogic;
    private List<LevelObject> _levelObjects;
    private List<LevelObject> _doors;
    private List<EnemyObject> _enemies;
    private PlayerObject _player;
    private List<UIObject> _UIObjects;
    private Dimension _dimension;

    private GameModel _parent;
    private Semaphore _logicBusySemaphore;

    public GameRoom() {
        _logicBusySemaphore = new Semaphore(1);
    }

    public RoomWorkResult executeWithResult() {
        // TODO room executeWithResult
        _roomLogic = new MenuLogic(this);
        _parent.sendEvent(ModelEventType.MENU_LOADED);

        try {
            _logicBusySemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new RoomWorkResult(RoomWorkResultType.EXIT);
    }

    @Override
    public GameRoomType getType() {
        return _type;
    }

    public void setType(GameRoomType value) {
        _type = value;
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

    @Override
    public List<LevelObject> getLevelObjects() {
        return _levelObjects;
    }

    public void setLevelObjects(List<LevelObject> value) {
        _levelObjects = value;
    }

    @Override
    public List<LevelObject> getDoors() {
        return _doors;
    }

    public void setDoors(List<LevelObject> value) {
        _doors = value;
    }

    @Override
    public List<EnemyObject> getEnemies() {
        return _enemies;
    }

    public void setEnemies(List<EnemyObject> value) {
        _enemies = value;
    }

    @Override
    public PlayerObject getPlayer() {
        return _player;
    }

    public void setPlayer(PlayerObject value) {
        _player = value;
    }

    @Override
    public List<UIObject> getUIObjects() {
        return _UIObjects;
    }

    public void setUIObjects(List<UIObject> value) {
        _UIObjects = value;
    }

    public GameModel getParent() {
        return _parent;
    }

    public void setParent(GameModel value) {
        _parent = value;
    }

    public Dimension getDimension() {
        return _dimension;
    }

    public void setDimension(Dimension value) {
        _dimension = value;
    }

    public Semaphore getLogicBusySemaphore() {
        return _logicBusySemaphore;
    }
}
