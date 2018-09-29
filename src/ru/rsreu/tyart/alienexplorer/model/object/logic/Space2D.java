package ru.rsreu.tyart.alienexplorer.model.object.logic;

public class Space2D {
    private float _left;
    private float _right;
    private float _top;
    private float _bottom;

    public Space2D(float left, float right, float top, float bottom) {
        _left = left;
        _right = right;
        _top = top;
        _bottom = bottom;
    }

    public float getLeft() {
        return _left;
    }

    public void setLeft(float value) {
        _left = value;
    }

    public float getRight() {
        return _right;
    }

    public void setRight(float value) {
        _right = value;
    }

    public float getTop() {
        return _top;
    }

    public void setTop(float value) {
        _top = value;
    }

    public float getBottom() {
        return _bottom;
    }

    public void setBottom(float value) {
        _bottom = value;
    }
}
