package ru.rsreu.tyart.alienexplorer.model;

import java.awt.*;

public interface IGameObject {
    Rectangle getCollider();
    int getTypeNumber();
    int getState();
    boolean getFlippedY();
}
