package ru.rsreu.tyart.alienexplorer.model;

public interface IGameObject {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    int getTypeNumber();
    int getState();
    boolean getFlippedY();
}
