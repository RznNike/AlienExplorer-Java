package ru.rsreu.tyart.alienexplorer.model.main;

import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEvent;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;
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
        // TODO launch sequence (main menu)
        _room = new GameRoom();
        _room.setType(GameRoomType.LEVEL);
        sendEvent(ModelEventType.LEVEL_LOADED);

//        while (true) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
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

    private void sendEvent(ModelEventType eventType) {
        ModelEvent event = new ModelEvent(this, eventType);
        for (ModelEventListener listener : _listeners) {
            listener.onModelEvent(event);
        }
    }
}
