package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.view.IViewable;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame implements IViewable {
    private JPanel _canvas;
    private ResourcesContainer _resources;

    public MainForm() {
        setContentPane(_canvas);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        _resources = ResourcesLoader.loadResources();
    }

    @Override
    public void viewModel(IModel model) {
        repaint();
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        while (_resources == null){

        }
        if (_resources != null) {
            // TODO !!!!!!
            Image img = _resources.getSprite(null);
            graphics.drawImage(img, 50, 100, 60, 60, null);
        }
    }
}