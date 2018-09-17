package ru.rsreu.tyart.alienexplorer.model.main;

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
        RoomWorkResult roomWorkResult = new RoomWorkResult(RoomWorkResultType.LOAD_MAIN_MENU);

        while (roomWorkResult.getResultType() != RoomWorkResultType.EXIT) {
            roomWorkResult = executeNextRoomCommand(roomWorkResult);
        }

        // TODO stop app
    }

    private RoomWorkResult executeNextRoomCommand(RoomWorkResult roomWorkResult) {
        switch (roomWorkResult.getResultType()) {
            case LOAD_MAIN_MENU:
                _room = RoomLoader.loadMainMenu(this);
                return _room.executeWithResult();
            case LOAD_LEVEL:
                _room = RoomLoader.loadLevel(this, roomWorkResult.getResultValue());
                return _room.executeWithResult();
            default:
                return roomWorkResult;
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
    public GameRoomType getRoomType() {
        return _room.getType();
    }

    public void sendEvent(ModelEventType eventType) {
        ModelEvent event = new ModelEvent(this, eventType);
        for (ModelEventListener listener : _listeners) {
            listener.onModelEvent(event);
        }
    }
}
