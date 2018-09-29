package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.model.object.logic.BaseObjectLogic;

import java.awt.geom.Point2D;

public class EnemyObject extends GameObject {
    private EnemyObjectType _type;
    private boolean _isMoving;
    private Point2D _leftWalkingBound;
    private Point2D _rightWalkingBound;
    private int _damage;
    private BaseObjectLogic _logic;

    @Override
    public int getTypeNumber() {
        return _type.ordinal() + 1;
    }

    public EnemyObjectType getType() {
        return _type;
    }

    public void setType(EnemyObjectType value) {
        _type = value;
    }

    public boolean isMoving() {
        return _isMoving;
    }

    public void setMoving(boolean value) {
        _isMoving = value;
    }

    public Point2D getLeftWalkingBound() {
        return _leftWalkingBound;
    }

    public void setLeftWalkingBound(Point2D value) {
        _leftWalkingBound = value;
    }

    public Point2D getRightWalkingBound() {
        return _rightWalkingBound;
    }

    public void setRightWalkingBound(Point2D value) {
        _rightWalkingBound = value;
    }

    public int getDamage() {
        return _damage;
    }

    public void setDamage(int value) {
        _damage = value;
    }

    public BaseObjectLogic getLogic() {
        return _logic;
    }

    public void setLogic(BaseObjectLogic value) {
        _logic = value;
    }
}
