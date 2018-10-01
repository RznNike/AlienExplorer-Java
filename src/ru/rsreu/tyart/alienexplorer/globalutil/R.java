package ru.rsreu.tyart.alienexplorer.globalutil;

import java.util.ResourceBundle;

public class R {
    private static final String MAIN_BUNDLE = "ru.rsreu.tyart.alienexplorer.res.values.strings";

    private static ResourceBundle _bundle;

    public static String getString(String id) {
        if (_bundle == null) {
            _bundle = ResourceBundle.getBundle(MAIN_BUNDLE, new UTF8Control());
        }
        return _bundle.getString(id);
    }
}
