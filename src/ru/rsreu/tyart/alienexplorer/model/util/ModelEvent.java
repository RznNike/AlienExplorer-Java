package ru.rsreu.tyart.alienexplorer.model.util;

import ru.rsreu.tyart.alienexplorer.model.IModel;

public class ModelEvent {
    private IModel _sender;
    private ModelEventType _type;

    public ModelEvent(IModel sender, ModelEventType type) {
        _sender = sender;
        _type = type;
    }

    public IModel getSender() {
        return _sender;
    }

    public ModelEventType getEventType() {
        return _type;
    }
}
