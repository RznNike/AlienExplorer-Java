package ru.rsreu.tyart.alienexplorer.model.util;

public enum FileTag {
    LEVEL_ID,
    WIDTH,
    HEIGHT,
    CAMERA_X,
    CAMERA_Y,
    LEVEL_OBJECT,
    ENEMY,
    DOOR,
    PLAYER,
    UNKNOWN;

    public static FileTag getTagFromString(String str) {
        if ("levelID".equals(str)) {
            return LEVEL_ID;
        } else if ("width".equals(str)) {
            return WIDTH;
        } else if ("height".equals(str)) {
            return HEIGHT;
        } else if ("cameraX".equals(str)) {
            return CAMERA_X;
        } else if ("cameraY".equals(str)) {
            return CAMERA_Y;
        } else if ("levelObject".equals(str)) {
            return LEVEL_OBJECT;
        } else if ("enemy".equals(str)) {
            return ENEMY;
        } else if ("door".equals(str)) {
            return DOOR;
        } else if ("player".equals(str)) {
            return PLAYER;
        } else {
            return UNKNOWN;
        }
    }
}
