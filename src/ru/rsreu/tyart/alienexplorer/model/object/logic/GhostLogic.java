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
    private static final int THREAD_SLEEP_MS = 15;

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

        if (isPlayerVisible()) {
            float playerX = (float)getRoom().getPlayer().getCollider().getX();
            float enemyX = (float)getEnemy().getCollider().getX();
            // Обработка движений по горизонтали
            if ((enemyX < playerX) && (freeSpace.getRight() > EPSILON)) {
                newSpeed.setX(HORIZONTAL_SPEED);
            } else if ((enemyX > playerX) && (freeSpace.getLeft() > EPSILON)) {
                newSpeed.setX(-HORIZONTAL_SPEED);
            }
        }

        return newSpeed;
    }

    private boolean isPlayerVisible() {
        Rectangle2D.Float playerCollider = getRoom().getPlayer().getCollider();

        if (getEnemy().getCollider().intersects(
                new Rectangle2D.Float(
                        -Float.MAX_VALUE / 2,
                        (float)playerCollider.getY(),
                        Float.MAX_VALUE,
                        (float)playerCollider.getHeight() ) )) {
            boolean barrierFound = false;
            for (LevelObject levelObject : getRoom().getLevelObjects()) {
                if (getEnemy().getCollider().getX() < playerCollider.getX()) {
                    barrierFound = levelObject.getCollider().intersects(
                            new Rectangle2D.Float(
                                    (float)getEnemy().getCollider().getX(),
                                    (float)getEnemy().getCollider().getY(),
                                    (float)(playerCollider.getX() - getEnemy().getCollider().getX()),
                                    (float)getEnemy().getCollider().getHeight() ) );
                } else {
                    barrierFound = levelObject.getCollider().intersects(
                            new Rectangle2D.Float(
                                    (float)playerCollider.getX(),
                                    (float)getEnemy().getCollider().getY(),
                                    (float)(getEnemy().getCollider().getX() - playerCollider.getX()),
                                    (float)getEnemy().getCollider().getHeight() ) );
                }
                if (barrierFound) {
                    break;
                }
            }
            return !barrierFound;
        } else {
            return false;
        }
    }

    public EnemyObject getEnemy() {
        return (EnemyObject)getObject();
    }

    public void setEnemy(EnemyObject enemy) {
        setObject(enemy);
    }
}
