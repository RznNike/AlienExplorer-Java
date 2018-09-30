package ru.rsreu.tyart.alienexplorer.model.object.logic;

import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.SlimeStateMachine;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.SlimeStateType;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;

import java.awt.geom.Point2D;
import java.util.Date;

public class SlimeLogic extends BaseObjectLogic<SlimeStateType> {
    private static final float HORIZONTAL_SPEED = MAX_SPEED / 20;
    private static final int THREAD_SLEEP_MS = 10;

    private Point2D _targetPoint;

    public SlimeLogic(GameRoom room, GameObject object) {
        setRoom(room);
        setObject(object);
        setStateMachine(new SlimeStateMachine());
    }

    @Override
    protected void processThread() {
        Space2D freeSpace;
        Vector2D speed = new Vector2D(0, 0);
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
                speed = findSpeed(speed, freeSpace, deltaSeconds);
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
            Vector2D oldSpeed,
            Space2D freeSpace,
            float deltaSeconds) {
        Vector2D newSpeed = new Vector2D(0, 0);

        // Обработка движений по горизонтали
        if (_targetPoint == getEnemy().getLeftWalkingBound()) {
            if ((getEnemy().getCollider().getX() < _targetPoint.getX())
                || (freeSpace.getLeft() < EPSILON)) {
                _targetPoint = getEnemy().getRightWalkingBound();
            }
        } else if (_targetPoint == getEnemy().getRightWalkingBound()) {
            if ((getEnemy().getCollider().getX() > _targetPoint.getX())
                    || (freeSpace.getRight() < EPSILON)) {
                _targetPoint = getEnemy().getLeftWalkingBound();
            }
        }

        if (_targetPoint == getEnemy().getRightWalkingBound()) {
            newSpeed.setX(HORIZONTAL_SPEED);
        } else {
            newSpeed.setX(-HORIZONTAL_SPEED);
        }

        // Обработка движений по вертикали
        if (((oldSpeed.getY() > EPSILON) && (freeSpace.getTop() > EPSILON))
                || (freeSpace.getBottom() > EPSILON)) {
            newSpeed.setY(oldSpeed.getY() - G * deltaSeconds);
            if (newSpeed.getY() < -MAX_SPEED) {
                newSpeed.setY(-MAX_SPEED);
            }
        }
        if ((oldSpeed.getY() > EPSILON) && (freeSpace.getTop() < EPSILON)) {
            newSpeed.setY(0);
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
