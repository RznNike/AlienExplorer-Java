package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.model.object.logic.PlayerLogic;

public class PlayerObject extends GameObject {
    private PlayerObjectType _type;
    private float _heightStandard;
    private float _heightSmall;
    private int _health;
    private boolean _damaged;
    private PlayerLogic _logic;

    @Override
    public int getTypeNumber() {
        return _type.ordinal() + 1;
    }

    public PlayerObjectType type() {
        return _type;
    }

    public void setType(PlayerObjectType value) {
        _type = value;
    }

    public float getHeightStandard() {
        return _heightStandard;
    }

    public void setHeightStandard(float value) {
        _heightStandard = value;
    }

    public float getHeightSmall() {
        return _heightSmall;
    }

    public void setHeightSmall(float value) {
        _heightSmall = value;
    }

    public int getHealth() {
        return _health;
    }

    public void setHealth(int value) {
        _health = value;
    }

    public boolean isDamaged() {
        return _damaged;
    }

    public void setDamaged(boolean value) {
        _damaged = value;
    }

    public PlayerLogic getLogic() {
        return _logic;
    }

    public void setLogic(PlayerLogic value) {
        _logic = value;
    }
}
