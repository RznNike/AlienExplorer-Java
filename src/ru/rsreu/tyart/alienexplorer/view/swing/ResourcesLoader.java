package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IPathEnum;
import ru.rsreu.tyart.alienexplorer.model.object.EnemyObjectType;
import ru.rsreu.tyart.alienexplorer.model.object.LevelObjectType;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObjectType;
import ru.rsreu.tyart.alienexplorer.model.object.UIObjectType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

class ResourcesLoader {
    private final static String BACKGROUNDS_PATH = "resources/sprites/levels/backgrounds";
    private final static String FONT_PATH = "resources/fonts/04b_30.otf";

    static ResourcesContainer loadResources() {
        Dictionary<Integer, Image> backgrounds = loadBackgrounds();
        Dictionary<Integer, List<Image>> levelObjectSprites = loadSpritesForEnum(LevelObjectType.class);
        Dictionary<Integer, List<Image>> enemySprites = loadSpritesForEnum(EnemyObjectType.class);
        Dictionary<Integer, List<Image>> playerSprites = loadSpritesForEnum(PlayerObjectType.class);
        Dictionary<Integer, Image> UISprites = loadUISprites();
        Font font = loadFont();

        return new ResourcesContainer(backgrounds, levelObjectSprites, enemySprites, playerSprites, UISprites, font);
    }

    private static Dictionary<Integer, List<Image>> loadSpritesForEnum(Class enumClass) {
        Dictionary<Integer, List<Image>> sprites = new Hashtable<Integer, List<Image>>();

        Enum[] constants = (Enum[])enumClass.getEnumConstants();
        for (Enum constant : constants)
        {
            String path = ((IPathEnum)constant).getPath();
            Integer number = constant.ordinal() + 1;
            List<Image> list = loadSpritesFromFolder(path);
            sprites.put(number, list);
            sprites.put(-number, flipSprites(list));
        }

        return sprites;
    }

    private static List<Image> loadSpritesFromFolder(String path) {
        List<Image> result = new ArrayList<Image>();
        List<String> filePaths = new ArrayList<String>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filePaths.add(file.getPath());
                }
            }
        }
        for (String file : filePaths)
        {
            try {
                result.add(ImageIO.read(new File(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static List<Image> flipSprites(List<Image> originalSprites) {
        List<Image> sprites = new ArrayList<Image>();
        for (Image sprite : originalSprites)
        {
            BufferedImage flippedSprite = new BufferedImage(
                    sprite.getWidth(null),
                    sprite.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics = flippedSprite.createGraphics();
            graphics.drawImage(sprite, 0, 0, null);
            graphics.dispose();

            AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
            transform.translate(
                    -flippedSprite.getWidth(null),
                    0);
            AffineTransformOp transformOp = new AffineTransformOp(
                    transform,
                    AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            flippedSprite = transformOp.filter(flippedSprite, null);
            sprites.add(flippedSprite);
        }

        return sprites;
    }

    private static Dictionary<Integer, Image> loadBackgrounds() {
        List<Image> backgrounds = loadSpritesFromFolder(BACKGROUNDS_PATH);
        Dictionary<Integer, Image> result = new Hashtable<Integer, Image>();
        for (int i = 0; i < backgrounds.size(); i++) {
            result.put(i, backgrounds.get(i));
        }
        return result;
    }

    private static Dictionary<Integer, Image> loadUISprites() {
        Dictionary<Integer, Image> sprites = new Hashtable<Integer, Image>();

        String path = UIObjectType.HEALTH.getPath();
        Image sprite = loadSpritesFromFolder(path).get(0);
        sprites.put(UIObjectType.HEALTH.ordinal(), sprite);

        path = UIObjectType.TIMER.getPath();
        sprite = loadSpritesFromFolder(path).get(0);
        sprites.put(UIObjectType.TIMER.ordinal(), sprite);

        return sprites;
    }

    private static Font loadFont() {
        try {
            return Font.createFont(Font.PLAIN, new File(FONT_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
