package ru.rsreu.tyart.alienexplorer.model;

import java.awt.geom.Rectangle2D;

public interface IGameObject {
    Rectangle2D getCollider();
    int getTypeNumber();
    int getState();
    boolean getFlippedY();
}
