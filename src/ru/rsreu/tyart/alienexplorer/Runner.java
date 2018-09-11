package ru.rsreu.tyart.alienexplorer;

import ru.rsreu.tyart.alienexplorer.controller.GameController;
import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.model.main.GameModel;
import ru.rsreu.tyart.alienexplorer.view.IViewable;
import ru.rsreu.tyart.alienexplorer.view.swing.MainForm;

public class Runner {
    public static void main(String[] args) {
        IModel model = new GameModel();
        GameController controller = new GameController(model);
        IViewable view = new MainForm(controller);
    }
}
