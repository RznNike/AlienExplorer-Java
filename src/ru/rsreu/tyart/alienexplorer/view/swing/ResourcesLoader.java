package ru.rsreu.tyart.alienexplorer.view.swing;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class ResourcesLoader {
    static ResourcesContainer loadResources() {
        ResourcesContainer container = new ResourcesContainer();
        try {
            container.testImage = ImageIO.read(new File("resources/sprites/player/green/00.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return container;
    }
}
