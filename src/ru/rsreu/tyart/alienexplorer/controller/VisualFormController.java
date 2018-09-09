package ru.rsreu.tyart.alienexplorer.controller;

import ru.rsreu.tyart.alienexplorer.view.swing.MainForm;

public class VisualFormController extends BaseController {
    public VisualFormController() {
        super();

        _view = new MainForm();
    }
}
