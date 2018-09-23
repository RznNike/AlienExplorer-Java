package ru.rsreu.tyart.alienexplorer.model.main;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.IGameRoom;
import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.model.main.logic.RoomWorkResult;
import ru.rsreu.tyart.alienexplorer.model.main.logic.RoomWorkResultType;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEvent;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;
import ru.rsreu.tyart.alienexplorer.model.util.RoomLoader;
import ru.rsreu.tyart.alienexplorer.view.ModelEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameModel implements IModel {
    private static GameModel _instance;
    private List<ModelEventListener> _listeners;

    private GameRoom _room;

    private GameModel() {
        _listeners = new ArrayList<ModelEventListener>();
    }

    public static GameModel getInstance() {
        if (_instance == null) {
            _instance = new GameModel();
        }
        return _instance;
    }

    public void start() {
        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GameModel.this.processMainThread();
            }
        });
        mainThread.start();
    }

    private void processMainThread() {
        RoomWorkResult roomWorkResult = new RoomWorkResult(RoomWorkResultType.LOAD_MENU);

        while (roomWorkResult.getResultType() != RoomWorkResultType.EXIT) {
            roomWorkResult = executeNextRoom(roomWorkResult);
        }

        System.exit(0);
    }

    private RoomWorkResult executeNextRoom(RoomWorkResult previousRoomWorkResult) {
        switch (previousRoomWorkResult.getResultType()) {
            case LOAD_MENU:
                _room = RoomLoader.loadMainMenu(this);
                return _room.executeWithResult();
            case LOAD_LEVEL:
                _room = RoomLoader.loadLevel(this, previousRoomWorkResult.getResultValue());
                return _room.executeWithResult();
            default:
                return previousRoomWorkResult;
        }
    }

    @Override
    public void addEventListener(ModelEventListener listener) {
        _listeners.add(listener);
    }

    @Override
    public void removeEventListener(ModelEventListener listener) {
        _listeners.remove(listener);
    }

    @Override
    public void removeAllEventListeners() {
        _listeners.clear();
    }

    @Override
    public IGameRoom getRoom() {
        return _room;
    }

    @Override
    public void receiveCommand(ControllerCommand command, boolean isThisACommandStart) {
        _room.getRoomLogic().receiveCommand(command, isThisACommandStart);
    }

    public void sendEvent(ModelEventType eventType) {
        ModelEvent event = new ModelEvent(this, eventType);
        for (ModelEventListener listener : _listeners) {
            listener.onModelEvent(event);
        }
    }
}
