package ru.rsreu.tyart.alienexplorer.model.object;

public class LevelObject extends GameObject {
    private LevelObjectType _type;

    public void setType(LevelObjectType value) {
        _type = value;
    }

    @Override
    public int getTypeNumber() {
        return _type.ordinal() + 1;
    }
}
