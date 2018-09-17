package ru.rsreu.tyart.alienexplorer.model;

import ru.rsreu.tyart.alienexplorer.model.main.GameRoomType;
import ru.rsreu.tyart.alienexplorer.view.ModelEventListener;

public interface IModel {
    void addEventListener(ModelEventListener listener);
    void removeEventListener(ModelEventListener listener);
    void removeAllEventListeners();
    GameRoomType getRoomType();
}
