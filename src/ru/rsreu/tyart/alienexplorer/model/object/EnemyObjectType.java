package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.model.IPathEnum;

public enum EnemyObjectType implements IPathEnum {
    SPIKES {
        public String getPath() {
            return "resources/sprites/enemies/spikes";
        }},
    SLIME {
        public String getPath() {
            return "resources/sprites/enemies/slime";
        }},
    BAT {
        public String getPath() {
            return "resources/sprites/enemies/bat";
        }},
    GHOST {
        public String getPath() {
            return "resources/sprites/enemies/ghost";
        }};

    @Override
    public abstract String getPath();
}
