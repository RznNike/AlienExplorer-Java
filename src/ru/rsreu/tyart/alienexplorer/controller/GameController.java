package ru.rsreu.tyart.alienexplorer.controller;

import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.view.ModelEventListener;

public class GameController {
    private static GameController _instance;
    private IModel _model;

    private GameController() {
    }

    public static GameController getInstance() {
        if (_instance == null) {
            _instance = new GameController();
        }
        return _instance;
    }

    public void setModel(IModel model) {
        _model = model;
    }

    public void subscribeViewToModel(ModelEventListener view) {
        _model.addEventListener(view);
    }

    public void unsubscribeViewFromModel(ModelEventListener view) {
        _model.removeEventListener(view);
    }

    public void unsubscribeAllViewsFromModel() {
        _model.removeAllEventListeners();
    }

    public void sendCommand(ControllerCommand command) {
    }
}
