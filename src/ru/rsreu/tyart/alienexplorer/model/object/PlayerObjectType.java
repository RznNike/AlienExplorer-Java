package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.model.IPathEnum;

public enum PlayerObjectType implements IPathEnum {
    GREEN {
        public String getPath() {
            return "resources/sprites/player/green";
        }},
    PINK {
        public String getPath() {
            return "resources/sprites/player/pink";
        }};

    @Override
    public abstract String getPath();
}
