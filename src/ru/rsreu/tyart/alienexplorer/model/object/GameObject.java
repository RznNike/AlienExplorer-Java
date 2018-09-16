package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.model.IGameObject;

public abstract class GameObject implements IGameObject {
    private int _x;
    private int _y;
    private int _width;
    private int _height;
    private int _state;
    private boolean _flippedY;

    @Override
    public int getX() {
        return _x;
    }

    public void setX(int value) {
        _x = value;
    }

    @Override
    public int getY() {
        return _y;
    }

    public void setY(int value) {
        _y = value;
    }

    @Override
    public int getWidth() {
        return _width;
    }

    public void setWidth(int value) {
        _width = value;
    }

    @Override
    public int getHeight() {
        return _height;
    }

    public void setHeight(int value) {
        _height = value;
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
