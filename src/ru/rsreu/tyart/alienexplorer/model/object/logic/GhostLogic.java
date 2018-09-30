package ru.rsreu.tyart.alienexplorer.model.object.logic;

import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.GhostStateMachine;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.GhostStateType;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;

import java.awt.geom.Rectangle2D;
import java.util.Date;

public class GhostLogic extends BaseObjectLogic<GhostStateType> {
    private static final float HORIZONTAL_SPEED = PlayerLogic.HORIZONTAL_SPEED;
    private static final float ATTACK_COOLDOWN_TIME = 1f;
    private static final int THREAD_SLEEP_MS = 10;

    private Rectangle2D.Float _triggerZone;
    private float _attackCooldown;

    public GhostLogic(GameRoom room, GameObject object) {
        setRoom(room);
        setObject(object);
        setStateMachine(new GhostStateMachine());
    }

    @Override
    protected void processThread() {
        Space2D freeSpace;
        Vector2D speed;
        Vector2D move;
        setTimestamp(new Date().getTime());

        initTriggerZone();

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
                speed = findSpeed(freeSpace, deltaSeconds);
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

    private void initTriggerZone() {
        _triggerZone = new Rectangle2D.Float(
                -Float.MAX_VALUE / 2,
                (float)getEnemy().getCollider().getY(),
                Float.MAX_VALUE,
                (float)getEnemy().getCollider().getHeight());

        float leftBound = (float)_triggerZone.getX();
        float rightBound = (float)(_triggerZone.getX() + _triggerZone.getWidth());

        for (LevelObject levelObject : getRoom().getLevelObjects()) {
            if (_triggerZone.intersects(levelObject.getCollider())) {
                if (levelObject.getCollider().getX() < getEnemy().getCollider().getX()) {
                    leftBound = (float)(levelObject.getCollider().getX() + levelObject.getCollider().getWidth());
                } else {
                    rightBound = (float)levelObject.getCollider().getX();
                }
                _triggerZone.x = leftBound;
                _triggerZone.width = rightBound - leftBound;
            }
        }
    }

    private Vector2D findSpeed(
            Space2D freeSpace,
            float deltaSeconds) {
        Vector2D newSpeed = new Vector2D(0, 0);

        if (_attackCooldown > EPSILON) {
            _attackCooldown -= deltaSeconds;
            return newSpeed;
        }

        if (getEnemy().getCollider().intersects(getRoom().getPlayer().getCollider())) {
            _attackCooldown = ATTACK_COOLDOWN_TIME;
            return newSpeed;
        }

        if (_triggerZone.intersects(getRoom().getPlayer().getCollider())) {
            float playerX = (float)getRoom().getPlayer().getCollider().getX();
            float enemyX = (float)getEnemy().getCollider().getX();

            if ((enemyX < playerX) && (freeSpace.getRight() > EPSILON)) {
                newSpeed.setX(HORIZONTAL_SPEED);
            } else if ((enemyX > playerX) && (freeSpace.getLeft() > EPSILON)) {
                newSpeed.setX(-HORIZONTAL_SPEED);
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
