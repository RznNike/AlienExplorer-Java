package ru.rsreu.tyart.alienexplorer.model;

import ru.rsreu.tyart.alienexplorer.view.ModelEventListener;

public interface IModel {
    // TODO remove start from interface
    void start();
    void addEventListener(ModelEventListener listener);
    void removeEventListener(ModelEventListener listener);
    void removeAllEventListeners();
}
