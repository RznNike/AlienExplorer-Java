package ru.rsreu.tyart.alienexplorer.model.object.logic;

import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.BatStateMachine;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.BatStateType;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;

import java.awt.geom.Point2D;
import java.util.Date;

public class BatLogic extends BaseObjectLogic<BatStateType> {
    private static final float MOVE_SPEED = MAX_SPEED / 10;
    private static final int THREAD_SLEEP_MS = 20;

    private Point2D.Float _targetPoint;

    public BatLogic(GameRoom room, GameObject object) {
        setRoom(room);
        setObject(object);
        setStateMachine(new BatStateMachine());
    }

    @Override
    protected void processThread() {
        Space2D freeSpace;
        Vector2D speed;
        Vector2D move;
        _targetPoint = getEnemy().getLeftWalkingBound();
        setTimestamp(new Date().getTime());

        while (!isStopThread()) {
            try {
                getManualResetEvent().waitOne();
                if (isStopThread()) {
                    continue;
                }
                boolean levelChanged;

                freeSpace = findFreeSpace();
                long newTime = new Date().getTime();
                float deltaSeconds = (newTime - getTimestamp()) / 1000f;
                setTimestamp(newTime);
                speed = findSpeed(deltaSeconds);
                move = findMoveVector(speed, freeSpace, deltaSeconds);
                levelChanged = moveObject(move);
                levelChanged |= setObjectFlipped(move);
                levelChanged |= getStateMachine().changeState(getEnemy(), freeSpace, move, deltaSeconds);

                if (levelChanged) {
                    getRoom().getParent().sendEvent(ModelEventType.LEVEL_CHANGED);
                }

                Thread.sleep(THREAD_SLEEP_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Vector2D findSpeed(
            float deltaSeconds) {
        Vector2D newSpeed = new Vector2D(0, 0);

        float dX = (float)Math.abs(getEnemy().getCollider().getX() - _targetPoint.getX());
        float dY = (float)Math.abs(getEnemy().getCollider().getY() - _targetPoint.getY());
        float path = (float)Math.sqrt(dX * dX + dY * dY);
        float speedMultiplier;
        if (path > MOVE_SPEED * deltaSeconds) {
            speedMultiplier = MOVE_SPEED / path;
        } else {
            speedMultiplier = 0;
            if (_targetPoint == getEnemy().getLeftWalkingBound()) {
                _targetPoint = getEnemy().getRightWalkingBound();
            } else {
                _targetPoint = getEnemy().getLeftWalkingBound();
            }
        }

        // Нахождение скорости
        if (dX > EPSILON) {
            if (getEnemy().getCollider().getX() < _targetPoint.getX()) {
                newSpeed.setX(dX * speedMultiplier);
            } else {
                newSpeed.setX(-dX * speedMultiplier);
            }
        }

        if (dY > EPSILON) {
            if (getEnemy().getCollider().getY() < _targetPoint.getY()) {
                newSpeed.setY(dY * speedMultiplier);
            } else {
                newSpeed.setY(-dY * speedMultiplier);
            }
        }

        return newSpeed;
    }

    public EnemyObject getEnemy() {
        return (EnemyObject)getObject();
    }

    public void setEnemy(EnemyObject enemy) {
        setObject(enemy);
    }
}
