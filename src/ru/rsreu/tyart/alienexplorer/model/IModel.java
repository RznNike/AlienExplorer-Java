package ru.rsreu.tyart.alienexplorer.model;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.view.ModelEventListener;

public interface IModel {
    void addEventListener(ModelEventListener listener);
    void removeEventListener(ModelEventListener listener);
    void removeAllEventListeners();
    void receiveCommand(ControllerCommand command, boolean isThisACommandStart);
    IGameRoom getRoom();
}
