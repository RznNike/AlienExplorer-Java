package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.model.IPathEnum;

public enum LevelObjectType implements IPathEnum {
    STONE {
        public String getPath() {
            return "resources/sprites/levels/stone";
        }},
    DOOR {
        public String getPath() {
            return "resources/sprites/levels/door";
        }},;

    @Override
    public abstract String getPath();
}
