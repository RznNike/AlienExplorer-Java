package ru.rsreu.tyart.alienexplorer.model.object.logic;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObject;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.PlayerStateMachine;
import ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines.PlayerStateType;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerLogic extends BaseObjectLogic<PlayerStateType> {
    public static final float HORIZONTAL_SPEED = MAX_SPEED / 3;
    private static final float JUMP_SPEED = MAX_SPEED / 1.5f;
    private static final int MAX_JUMPS = 2;
    private static final float HURT_COOLDOWN_TIME = 1f;
    private static final int THREAD_SLEEP_MS = 5;

    private List<ControllerCommand> _activeCommands;
    private int _jumpsCount;
    private boolean _isJumpActive;
    private float _hurtCooldown;

    public PlayerLogic(GameRoom room) {
        setRoom(room);
        setStateMachine(new PlayerStateMachine());
        setPlayer(room.getPlayer());
        _activeCommands = new ArrayList<ControllerCommand>();
    }

    @Override
    protected void processThread() {
        Space2D freeSpace = new Space2D(0, 0, 0, 0);
        Vector2D speed = new Vector2D(0, 0);
        Vector2D move = new Vector2D(0, 0);

        _jumpsCount = 0;
        _isJumpActive = false;
        _hurtCooldown = 0;

        setTimestamp(new Date().getTime());

        while (!isStopThread()) {
            try {
                getManualResetEvent().waitOne();

                freeSpace = findFreeSpace();
                long newTime = new Date().getTime();
                float deltaSeconds = (newTime - getTimestamp()) / 1000f;
                setTimestamp(newTime);
                speed = findSpeed(speed, freeSpace, deltaSeconds);
                move = findMoveVector(speed, freeSpace, deltaSeconds);
                moveObject(move);
                setObjectFlipped(move);
                getStateMachine().changeState(getPlayer(), freeSpace, move, deltaSeconds);
                getRoom().getParent().sendEvent(ModelEventType.LEVEL_CHANGED);

                Thread.sleep(THREAD_SLEEP_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resume() {
        super.resume();
        _activeCommands.clear();
    }

    public void receiveCommand(ControllerCommand command, boolean isThisACommandStart) {
        if (isThisACommandStart) {
            if (!_activeCommands.contains(command)) {
                _activeCommands.add(command);
            }
        } else {
            _activeCommands.remove(command);
        }
    }

    private Vector2D findSpeed(
            Vector2D oldSpeed,
            Space2D freeSpace,
            float deltaSeconds)
    {
        Vector2D newSpeed = new Vector2D(0, 0);

        // Обработка приседания
        if ((freeSpace.getBottom() < EPSILON) && _activeCommands.contains(ControllerCommand.DOWN)) {
            getPlayer().getCollider().height = getPlayer().getHeightSmall();
            return newSpeed;
        }
        getPlayer().getCollider().height = getPlayer().getHeightStandard();

        // Обработка движений по горизонтали
        int leftCommandPosition = _activeCommands.indexOf(ControllerCommand.LEFT);
        int rightCommandPosition = _activeCommands.indexOf(ControllerCommand.RIGHT);
        if ((leftCommandPosition >= 0) && (leftCommandPosition > rightCommandPosition) && (freeSpace.getLeft() > EPSILON)) {
            newSpeed.setX(-HORIZONTAL_SPEED);
        } else if ((rightCommandPosition >= 0) && (rightCommandPosition > leftCommandPosition) && (freeSpace.getRight() > EPSILON)) {
            newSpeed.setX(HORIZONTAL_SPEED);
        }

        // Обработка движений по вертикали
        if (((oldSpeed.getY() > EPSILON) && (freeSpace.getTop() > EPSILON))
                || (freeSpace.getBottom() > EPSILON)) {
            newSpeed.setY(oldSpeed.getY() - G * deltaSeconds);
            if (newSpeed.getY() < -MAX_SPEED) {
                newSpeed.setY(-MAX_SPEED);
            }
        } else if ((oldSpeed.getY() > EPSILON) && (freeSpace.getTop() < EPSILON)) {
            newSpeed.setY(0);
        }

        // Обработка получения урона
        if (_hurtCooldown > EPSILON) {
            _hurtCooldown -= deltaSeconds;
        } else if (isEnemyAttacks()) {
            newSpeed.setY(JUMP_SPEED / 1.5f);
            _hurtCooldown = HURT_COOLDOWN_TIME;
        }

        // Сброс счетчика прыжков, если игрок на земле
        if (freeSpace.getBottom() < EPSILON) {
            _jumpsCount = 0;
        }

        // Счетчик прыжков >= 1, если игрок в воздухе
        if ((freeSpace.getBottom() > EPSILON) & (_jumpsCount == 0)) {
            _jumpsCount = 1;
        }

        // Обработка попытки прыжка
        if ((!_isJumpActive) && _activeCommands.contains(ControllerCommand.UP) && (_jumpsCount < MAX_JUMPS)) {
            _jumpsCount++;
            _isJumpActive = true;
            if (freeSpace.getTop() > EPSILON) {
                newSpeed.setY(JUMP_SPEED);
            }
        } else if (!_activeCommands.contains(ControllerCommand.UP)) {
            _isJumpActive = false;
        }

        return newSpeed;
    }

    private boolean isEnemyAttacks() {
        for (EnemyObject enemy : getRoom().getEnemies()) {
            if (getPlayer().getCollider().intersects(enemy.getCollider())) {
                getPlayer().setHealth(getPlayer().getHealth() - enemy.getDamage());
                if (getPlayer().getHealth() < 0) {
                    getPlayer().setHealth(0);
                }
                getStateMachine().setMachineState(PlayerStateType.HURT);
                getRoom().getParent().sendEvent(ModelEventType.UI_CHANGED);
                return true;
            }
        }
        return false;
    }

    private PlayerObject getPlayer() {
        return (PlayerObject)getObject();
    }

    private void setPlayer(PlayerObject value) {
        setObject(value);
    }
}
