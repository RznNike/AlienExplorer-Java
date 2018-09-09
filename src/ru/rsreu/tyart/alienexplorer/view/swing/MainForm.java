package ru.rsreu.tyart.alienexplorer.view.swing;

import ru.rsreu.tyart.alienexplorer.model.IModel;
import ru.rsreu.tyart.alienexplorer.view.IViewable;

import javax.swing.*;

public class MainForm extends JFrame implements IViewable {
    private JPanel rootPanel;

    public MainForm() {
        setContentPane(rootPanel);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void viewModel(IModel model) {

    }
}