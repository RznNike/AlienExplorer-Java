package ru.rsreu.tyart.alienexplorer.model.object.logic;

public class Vector2D {
    private float _x;
    private float _y;

    public Vector2D(float x, float y) {
        _x = x;
        _y = y;
    }

    public float getX() {
        return _x;
    }

    public void setX(float value) {
        _x = value;
    }

    public float getY() {
        return _y;
    }

    public void setY(float value) {
        _y = value;
    }
}
