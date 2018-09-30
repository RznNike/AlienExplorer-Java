package ru.rsreu.tyart.alienexplorer.model.object.logic;

import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.ObjectStateMachine;
import ru.rsreu.tyart.alienexplorer.model.util.ManualResetEvent;

import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BaseObjectLogic<MachineStateEnum> {
    public static final float EPSILON = 0.001f;
    public static final float MAX_SPEED = 9.0f;
    public static final float G = 15.0f;
    public static final float LOOKUP_DIST = 1.5f;

    private ObjectStateMachine<MachineStateEnum> _stateMachine;
    private boolean _stopThread;
    private long _timestamp;
    private GameRoom _room;
    private GameObject _object;
    private ManualResetEvent _manualResetEvent;

    protected abstract void processThread();

    public void start(ManualResetEvent manualResetEvent) {
        _manualResetEvent = manualResetEvent;
        _stopThread = false;
        Thread logicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                BaseObjectLogic.this.processThread();
            }
        });
        logicThread.start();
    }

    public void stop() {
        _stopThread = true;
    }

    public void resume() {
        _timestamp = new Date().getTime();
    }

    protected Space2D findFreeSpace() {
        Space2D freeSpace = new Space2D(LOOKUP_DIST, LOOKUP_DIST, LOOKUP_DIST, LOOKUP_DIST);

        Rectangle2D.Float collider = getObject().getCollider();
        Map<Integer, Map<Integer, List<LevelObject>>> levelObjects2DMap = getRoom().getLevelObjects2DMap();
        for (int x = (int)(collider.getX() - LOOKUP_DIST);
             x <= (int)(collider.getX() + collider.getWidth() + LOOKUP_DIST);
             x++) {
            if (!levelObjects2DMap.containsKey(x)) {
                continue;
            }
            for (int y = (int)(collider.getY() - LOOKUP_DIST);
                 y <= (int)(collider.getY() + collider.getHeight() + LOOKUP_DIST);
                 y++) {
                if (!levelObjects2DMap.get(x).containsKey(y)) {
                    continue;
                }
                for (LevelObject levelObject : levelObjects2DMap.get(x).get(y)) {
                    Space2D distances = findDistances(levelObject);
                    freeSpace.setLeft(freeSpace.getLeft() > distances.getLeft()
                            ? distances.getLeft()
                            : freeSpace.getLeft());
                    freeSpace.setTop(freeSpace.getTop() > distances.getTop()
                            ? distances.getTop()
                            : freeSpace.getTop());
                    freeSpace.setRight(freeSpace.getRight() > distances.getRight()
                            ? distances.getRight()
                            : freeSpace.getRight());
                    freeSpace.setBottom(freeSpace.getBottom() > distances.getBottom()
                            ? distances.getBottom()
                            : freeSpace.getBottom());
                }
            }
        }

        return freeSpace;
    }

    private Space2D findDistances(LevelObject secondObject) {
        Space2D distances = new Space2D(LOOKUP_DIST, LOOKUP_DIST, LOOKUP_DIST, LOOKUP_DIST);
        if (_object.getCollider().intersects(
                secondObject.getCollider().getX(),
                -Float.MAX_VALUE / 2,
                secondObject.getCollider().getWidth(),
                Float.MAX_VALUE)) {
            // Наблюдатель над/под объектом
            if ((_object.getCollider().getY() - secondObject.getCollider().getY()) > EPSILON) {
                // Наблюдатель над объектом
                distances.setBottom((float)(_object.getCollider().getY()
                        - (secondObject.getCollider().getY() + secondObject.getCollider().getHeight())));
            } else {
                // Наблюдатель под объектом
                distances.setTop((float)(secondObject.getCollider().getY()
                        - (_object.getCollider().getY() + _object.getCollider().getHeight())));
            }
        } else if (_object.getCollider().intersects(
                -Float.MAX_VALUE / 2,
                secondObject.getCollider().getY(),
                Float.MAX_VALUE,
                secondObject.getCollider().getHeight())) {
            // Наблюдатель левее/правее объекта
            if ((_object.getCollider().getX() - secondObject.getCollider().getX()) > EPSILON) {
                // Наблюдатель правее объекта
                distances.setLeft((float)(_object.getCollider().getX()
                        - (secondObject.getCollider().getX() + secondObject.getCollider().getWidth())));
            } else {
                // Наблюдатель левее объекта
                distances.setRight((float)(secondObject.getCollider().getX()
                        - (_object.getCollider().getX() + _object.getCollider().getWidth())));
            }
        }

        return distances;
    }

    protected Vector2D findMoveVector(
            Vector2D speed,
            Space2D freeSpace,
            float deltaSeconds)
    {
        Vector2D move = new Vector2D(
                speed.getX() * deltaSeconds,
                speed.getY() * deltaSeconds);

        if (move.getX() > EPSILON) {
            if ((freeSpace.getRight() - move.getX()) < EPSILON) {
                move.setX(freeSpace.getRight() - EPSILON / 2);
            }
        } else {
            if ((freeSpace.getLeft() + move.getX()) < EPSILON) {
                move.setX(-freeSpace.getLeft() + EPSILON / 2);
            }
        }

        if (move.getY() > EPSILON) {
            if ((freeSpace.getTop() - move.getY()) < EPSILON) {
                move.setY(freeSpace.getTop() - EPSILON / 2);
            }
        } else {
            if ((freeSpace.getBottom() + move.getY()) < EPSILON) {
                move.setY(-freeSpace.getBottom() + EPSILON / 2);
            }
        }

        return move;
    }

    protected boolean moveObject(Vector2D move) {
        if ((Math.abs(move.getX()) > EPSILON) || (Math.abs(move.getY()) > EPSILON)) {
            _object.getCollider().x += move.getX();
            _object.getCollider().y += move.getY();
            return true;
        }
        return false;
    }

    protected boolean setObjectFlipped(Vector2D move) {
        boolean oldValue = _object.getFlippedY();
        if (Math.abs(move.getX()) > EPSILON) {
            _object.setFlippedY(move.getX() < 0);
        }
        return oldValue != _object.getFlippedY();
    }

    public ObjectStateMachine getStateMachine() {
        return _stateMachine;
    }

    public void setStateMachine(ObjectStateMachine value) {
        _stateMachine = value;
    }

    public boolean isStopThread() {
        return _stopThread;
    }

    public void setStopThread(boolean value) {
        _stopThread = value;
    }

    public long getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(long value) {
        _timestamp = value;
    }

    public GameRoom getRoom() {
        return _room;
    }

    public void setRoom(GameRoom value) {
        _room = value;
    }

    protected GameObject getObject() {
        return _object;
    }

    protected void setObject(GameObject value) {
        _object = value;
    }

    public ManualResetEvent getManualResetEvent() {
        return _manualResetEvent;
    }

    public void setManualResetEvent(ManualResetEvent value) {
        _manualResetEvent = value;
    }
}
