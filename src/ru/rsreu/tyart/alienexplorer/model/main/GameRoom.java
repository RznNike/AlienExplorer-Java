package ru.rsreu.tyart.alienexplorer.model.main;

import ru.rsreu.tyart.alienexplorer.model.IGameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.*;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.BaseObjectLogic;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class GameRoom implements IGameRoom {
    private GameRoomType _type;
    private int _id;
    private BaseRoomLogic _roomLogic;
    private List<LevelObject> _levelObjects;
    private Map<Integer, Map<Integer, List<LevelObject>>> _levelObjects2DMap;
    private List<LevelObject> _doors;
    private List<EnemyObject> _enemies;
    private PlayerObject _player;
    private List<UIObject> _UIObjects;
    private Dimension _dimension;
    private Point2D.Float _startCameraPosition;

    private GameModel _parent;
    private Semaphore _logicBusySemaphore;

    public GameRoom() {
        _logicBusySemaphore = new Semaphore(1);
    }

    RoomWorkResult executeWithResult() {
        if (_type == GameRoomType.MENU) {
            _parent.sendEvent(ModelEventType.MENU_LOADED);
        } else {
            _parent.sendEvent(ModelEventType.LEVEL_LOADED);
            mapLevelObjects();
            ((LevelLogic)_roomLogic).start();
        }

        try {
            _logicBusySemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (getRoomLogic().getStateMachine().getCurrentCommand())
        {
            case LOAD_MENU:
                return new RoomWorkResult(RoomWorkResultType.LOAD_MENU);
            case LOAD_LEVEL:
                return new RoomWorkResult(
                        RoomWorkResultType.LOAD_LEVEL,
                        getRoomLogic().getStateMachine().getSelectedMenuItem());
            default:
                return new RoomWorkResult(RoomWorkResultType.EXIT);
        }
    }

    private void mapLevelObjects() {
        _levelObjects2DMap = new HashMap<Integer, Map<Integer, List<LevelObject>>>();
        float epsilon = BaseObjectLogic.EPSILON;
        for (LevelObject levelObject : _levelObjects) {
            Rectangle2D.Float collider = levelObject.getCollider();
            for (int x = (int)(collider.getX() + epsilon);
                 x <= (int)(collider.getX() + collider.getWidth() - epsilon);
                 x++) {
                if (!_levelObjects2DMap.containsKey(x)) {
                    _levelObjects2DMap.put(x, new HashMap<Integer, List<LevelObject>>());
                }
                for (int y = (int)(collider.getY() + epsilon);
                     y <= (int)(collider.getY() + collider.getHeight() - epsilon);
                     y++) {
                    if (!_levelObjects2DMap.get(x).containsKey(y)) {
                        _levelObjects2DMap.get(x).put(y, new ArrayList<LevelObject>());
                    }

                    _levelObjects2DMap.get(x).get(y).add(levelObject);
                }
            }
        }
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

    BaseRoomLogic getRoomLogic() {
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

    public Map<Integer, Map<Integer, List<LevelObject>>> getLevelObjects2DMap() {
        return _levelObjects2DMap;
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

    @Override
    public Dimension getDimension() {
        return _dimension;
    }

    public void setDimension(Dimension value) {
        _dimension = value;
    }

    public Semaphore getLogicBusySemaphore() {
        return _logicBusySemaphore;
    }

    @Override
    public String getMenuHeader() {
        return _roomLogic.getMenuHeader();
    }

    @Override
    public Point2D.Float getStartCameraPosition() {
        return _startCameraPosition;
    }

    public void setStartCameraPosition(Point2D.Float value) {
        _startCameraPosition = value;
    }
}
