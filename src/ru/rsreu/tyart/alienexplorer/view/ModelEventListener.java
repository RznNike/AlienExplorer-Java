package ru.rsreu.tyart.alienexplorer.view;

import ru.rsreu.tyart.alienexplorer.model.util.ModelEvent;

public interface ModelEventListener {
    void onModelEvent(ModelEvent event);
}
