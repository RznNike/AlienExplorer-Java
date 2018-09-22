package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.model.IGameObject;

import java.awt.*;

public abstract class GameObject implements IGameObject {
    private Rectangle _collider;
    private int _state;
    private boolean _flippedY;

    @Override
    public Rectangle getCollider() {
        return _collider;
    }

    public void setCollider(Rectangle value) {
        _collider = value;
    }

    @Override
    public abstract int getTypeNumber();

    @Override
    public int getState() {
        return _state;
    }

    public void setState(int value) {
        _state = value;
    }

    @Override
    public boolean getFlippedY() {
        return _flippedY;
    }

    public void setFlippedY(boolean value) {
        _flippedY = value;
    }
}
