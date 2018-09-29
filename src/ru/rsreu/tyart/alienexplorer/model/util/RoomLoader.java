package ru.rsreu.tyart.alienexplorer.model.util;

import ru.rsreu.tyart.alienexplorer.model.main.GameModel;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoomType;
import ru.rsreu.tyart.alienexplorer.model.main.logic.LevelLogic;
import ru.rsreu.tyart.alienexplorer.model.main.logic.MenuLogic;
import ru.rsreu.tyart.alienexplorer.model.object.*;
import ru.rsreu.tyart.alienexplorer.model.object.logic.BatLogic;
import ru.rsreu.tyart.alienexplorer.model.object.logic.GhostLogic;
import ru.rsreu.tyart.alienexplorer.model.object.logic.PlayerLogic;
import ru.rsreu.tyart.alienexplorer.model.object.logic.SlimeLogic;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RoomLoader {
    private static final String LEVELS_FOLDER = "resources/levels";

    public static GameRoom loadMainMenu(GameModel parent) {
        GameRoom room = new GameRoom();
        room.setType(GameRoomType.MENU);
        room.setLevelObjects(new ArrayList<LevelObject>());
        room.setDoors(new ArrayList<LevelObject>());
        room.setEnemies(new ArrayList<EnemyObject>());
        room.setUIObjects(new ArrayList<UIObject>());
        room.setRoomLogic(new MenuLogic(room));
        room.setParent(parent);

        return room;
    }

    public static GameRoom loadLevel(GameModel parent, int levelID) {
        GameRoom room = new GameRoom();
        room.setLevelObjects(new ArrayList<LevelObject>());
        room.setDoors(new ArrayList<LevelObject>());
        room.setEnemies(new ArrayList<EnemyObject>());
        room.setUIObjects(new ArrayList<UIObject>());
        int levelWidth = 1;
        int levelHeight = 1;

        String path = String.format("%s/level%d.txt", LEVELS_FOLDER, levelID);
        List<String> strings = readFile(path);
        for (String line : strings) {
            String[] parts = line.split(" +");
            try {
                FileTag tag = FileTag.getTagFromString(parts[0]);
                switch (tag) {
                    case LEVEL_ID:
                        room.setId(Integer.valueOf(parts[1]));
                        break;
                    case WIDTH:
                        levelWidth = Integer.valueOf(parts[1]);
                        break;
                    case HEIGHT:
                        levelHeight = Integer.valueOf(parts[1]);
                        break;
                    case CAMERA_X:
                        // TODO CAMERA_X
                        break;
                    case CAMERA_Y:
                        // TODO CAMERA_Y
                        break;
                    case LEVEL_OBJECT:
                        room.getLevelObjects().add(parseLevelObject(parts));
                        break;
                    case ENEMY:
                        room.getEnemies().add(parseEnemy(parts));
                        break;
                    case DOOR:
                        room.getDoors().add(parseLevelObject(parts));
                        break;
                    case PLAYER:
                        room.setPlayer(parsePlayer(parts));
                        break;
                }
            } catch (Exception e) {
                String message = String.format("Can't parse entry in level file:\n%s\n%s",
                        path, line);
                System.err.println(message);
            }
        }

        room.setType(GameRoomType.LEVEL);
        room.setDimension(new Dimension(levelWidth, levelHeight));

        // TODO init logics here
        room.getPlayer().setLogic(new PlayerLogic(room));
        for (EnemyObject enemy : room.getEnemies()) {
            switch (enemy.getType()) {
                case SLIME:
                    enemy.setLogic(new SlimeLogic(room, enemy));
                    break;
//                case BAT:
//                    enemy.setLogic(new BatLogic(room, enemy));
//                    break;
//                case GHOST:
//                    enemy.setLogic(new GhostLogic(room, enemy));
//                    break;
            }
        }

        room.setParent(parent);
        room.setRoomLogic(new LevelLogic(room));

        return room;
    }

    private static LevelObject parseLevelObject(String[] parts) {
        final int TYPE_N = 1;
        final int X_N = 2;
        final int Y_N = 3;
        final int WIDTH_N = 4;
        final int HEIGHT_N = 5;
        final int STATE_N = 6;
        final int IS_FLIPPED_N = 7;

        LevelObject result = new LevelObject();
        result.setType(LevelObjectType.valueOf(parts[TYPE_N]));
        float x = Float.valueOf(parts[X_N]);
        float y = Float.valueOf(parts[Y_N]);
        float width = Float.valueOf(parts[WIDTH_N]);
        float height = Float.valueOf(parts[HEIGHT_N]);
        result.setCollider(new Rectangle2D.Float(x, y, width, height));
        result.setState(Integer.valueOf(parts[STATE_N]));
        result.setFlippedY(Boolean.valueOf(parts[IS_FLIPPED_N]));

        return result;
    }

    private static EnemyObject parseEnemy(String[] parts) {
        final int TYPE_N = 1;
        final int DAMAGE_N = 2;
        final int X_N = 3;
        final int Y_N = 4;
        final int WIDTH_N = 5;
        final int HEIGHT_N = 6;
        final int STATE_N = 7;
        final int IS_FLIPPED_N = 8;
        final int LEFT_WALKING_BOUND_X = 9;
        final int LEFT_WALKING_BOUND_Y = 10;
        final int RIGHT_WALKING_BOUND_X = 11;
        final int RIGHT_WALKING_BOUND_Y = 12;

        EnemyObject result = new EnemyObject();
        result.setType(EnemyObjectType.valueOf(parts[TYPE_N]));
        result.setDamage(Integer.valueOf(parts[DAMAGE_N]));
        float x = Float.valueOf(parts[X_N]);
        float y = Float.valueOf(parts[Y_N]);
        float width = Float.valueOf(parts[WIDTH_N]);
        float height = Float.valueOf(parts[HEIGHT_N]);
        result.setCollider(new Rectangle2D.Float(x, y, width, height));
        result.setState(Integer.valueOf(parts[STATE_N]));
        result.setFlippedY(Boolean.valueOf(parts[IS_FLIPPED_N]));
        float leftWalkingBoundX = Float.valueOf(parts[LEFT_WALKING_BOUND_X]);
        float leftWalkingBoundY = Float.valueOf(parts[LEFT_WALKING_BOUND_Y]);
        float rightWalkingBoundX = Float.valueOf(parts[RIGHT_WALKING_BOUND_X]);
        float rightWalkingBoundY = Float.valueOf(parts[RIGHT_WALKING_BOUND_Y]);
        result.setLeftWalkingBound(new Point2D.Float(leftWalkingBoundX, leftWalkingBoundY));
        result.setRightWalkingBound(new Point2D.Float(rightWalkingBoundX, rightWalkingBoundY));

        return result;
    }

    private static PlayerObject parsePlayer(String[] parts) {
        final int TYPE_N = 1;
        final int X_N = 2;
        final int Y_N = 3;
        final int WIDTH_N = 4;
        final int HEIGHT_N = 5;
        final int HEIGHT_STANDARD_N = 6;
        final int HEIGHT_SMALL_N = 7;
        final int STATE_N = 8;
        final int IS_FLIPPED_N = 9;
        final int HEALTH_N = 10;

        PlayerObject result = new PlayerObject();
        result.setType(PlayerObjectType.valueOf(parts[TYPE_N]));
        float x = Float.valueOf(parts[X_N]);
        float y = Float.valueOf(parts[Y_N]);
        float width = Float.valueOf(parts[WIDTH_N]);
        float height = Float.valueOf(parts[HEIGHT_N]);
        result.setCollider(new Rectangle2D.Float(x, y, width, height));
        result.setHeightStandard(Float.valueOf(parts[HEIGHT_STANDARD_N]));
        result.setHeightSmall(Float.valueOf(parts[HEIGHT_SMALL_N]));
        result.setState(Integer.valueOf(parts[STATE_N]));
        result.setFlippedY(Boolean.valueOf(parts[IS_FLIPPED_N]));
        result.setHealth(Integer.valueOf(parts[HEALTH_N]));
        result.setDamaged(false);

        return result;
    }

    private static List<String> readFile(String path) {
        List<String> result = new ArrayList<String>();
        try {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Integer> getAvailableLevels() {
        List<Integer> result = new ArrayList<Integer>();
        File folder = new File(LEVELS_FOLDER);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().matches("level\\d+.txt")) {
                    result.add(Integer.valueOf(file.getName().replaceAll("\\D+", "")));
                }
            }
        }

        return result;
    }
}
