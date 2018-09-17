package ru.rsreu.tyart.alienexplorer;

import ru.rsreu.tyart.alienexplorer.controller.GameController;
import ru.rsreu.tyart.alienexplorer.model.main.GameModel;
import ru.rsreu.tyart.alienexplorer.view.ModelEventListener;
import ru.rsreu.tyart.alienexplorer.view.swing.MainForm;

public class Runner {
    public static void main(String[] args) {
        GameModel model = GameModel.getInstance();

        GameController controller = GameController.getInstance();
        controller.setModel(model);

        ModelEventListener view = new MainForm(controller);

        model.start();
    }
}
