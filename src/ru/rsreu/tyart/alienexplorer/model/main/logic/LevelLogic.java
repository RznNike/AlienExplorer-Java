package ru.rsreu.tyart.alienexplorer.model.main.logic;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines.LevelMenuStateMachine;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObject;
import ru.rsreu.tyart.alienexplorer.model.util.ManualResetEvent;

import java.awt.geom.Rectangle2D;

public class LevelLogic extends BaseRoomLogic {
    private static final int THREAD_SLEEP_MS = 5;
    private static final float PLAYER_TO_DOOR_WIN_OFFSET = 0.4f;

    private boolean _stopThread;
    private ManualResetEvent _manualResetEvent;

    public LevelLogic(GameRoom room) {
        room.getLogicBusySemaphore().drainPermits();
        setRoom(room);
        setStateMachine(new LevelMenuStateMachine(room));
        _manualResetEvent = new ManualResetEvent();
    }

    @Override
    public void receiveCommand(ControllerCommand command, boolean isThisACommandStart) {
        if (isThisACommandStart) {
            handleCommand(command);
        }
        // TODO send command to player
//        if (command.ordinal() <= ControllerCommand.RIGHT.ordinal()) {
//            getRoom().getPlayer().
//        }
    }

    @Override
    protected void handleCommand(ControllerCommand command) {
        getStateMachine().changeState(command);
        switch (getStateMachine().getCurrentCommand()) {
            case PAUSE:
                pause();
                break;
            case RESUME:
                resume();
                break;
            case LOAD_MENU:
            case LOAD_LEVEL:
            case EXIT:
                stop();
                getRoom().getLogicBusySemaphore().release();
                break;
        }
    }

    public void start() {
        _manualResetEvent.set();

//        getRoom().getPlayer().getLogic().start(_manualResetEvent);
//        foreach (ILogic elLogic in _model.EnemyLogics)
//        {
//            if (elLogic != null)
//            {
//                elLogic.Start(_manualResetEventSlim);
//            }
//        }

        _stopThread = false;
        Thread logicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                LevelLogic.this.processMainThread();
            }
        });
        logicThread.start();
    }

    public void stop() {
        _stopThread = true;
//        _model.PlayerLogics.Stop();
//        foreach (ILogic elLogic in _model.EnemyLogics)
//        {
//            if (elLogic != null)
//            {
//                elLogic.Stop();
//            }
//        }
        _manualResetEvent.set();
    }

    public void pause() {
        _manualResetEvent.reset();
    }

    public void resume() {
//        _model.PlayerLogics.Resume();
//        foreach (ILogic elLogic in _model.EnemyLogics)
//        {
//            if (elLogic != null)
//            {
//                elLogic.Resume();
//            }
//        }
        _manualResetEvent.set();
    }

    private void processMainThread() {
        while (!_stopThread)
        {
            try {
                _manualResetEvent.waitOne();
                _stopThread = checkPlayerHP() || checkPlayerPosition();
                if (!_stopThread)
                {
                    Thread.sleep(THREAD_SLEEP_MS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPlayerHP() {
        boolean stopThread = false;
        if (getRoom().getPlayer().getHealth() == 0) {
            this.stop();
            // TODO!!!
//            ((LevelMenuStateMachine)getStateMachine()).enterToMenu(UIObjectType.RESTART);
            stopThread = true;
        }

        return stopThread;
    }

    private boolean checkPlayerPosition() {
        boolean stopThread = false;
        if (isPlayerInGoalPoint()) {
            this.stop();
            // TODO!!!
//            ((LevelMenuStateMachine)getStateMachine()).enterToMenu(UIObjectType.NEXT_LEVEL);
            stopThread = true;
        }

        return stopThread;
    }

    private boolean isPlayerInGoalPoint() {
        boolean result = false;
        for (LevelObject door : getRoom().getDoors()) {
            if (door.getState() == 1) {
                Rectangle2D.Float playerCollider = getRoom().getPlayer().getCollider();
                Rectangle2D.Float doorCollider = door.getCollider();
                Rectangle2D.Float pointCollider = new Rectangle2D.Float(
                        (float)door.getCollider().getX() + PLAYER_TO_DOOR_WIN_OFFSET,
                        (float)door.getCollider().getY() + PLAYER_TO_DOOR_WIN_OFFSET,
                        (float)door.getCollider().getWidth() - PLAYER_TO_DOOR_WIN_OFFSET * 2,
                        (float)door.getCollider().getHeight() - PLAYER_TO_DOOR_WIN_OFFSET * 2);
                result |= playerCollider.intersects(pointCollider);
            }
        }

        return result;
    }
}
