package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.globalutil.R;
import ru.rsreu.tyart.alienexplorer.model.IPathEnum;

public enum UIObjectType implements IPathEnum {
    NEW_GAME {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("new_game");
        }},
    CHOOSE_LEVEL {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("choose_level");
        }},
    EXIT {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("exit");
        }},

    RESUME {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("resume");
        }},
    BACK_TO_MAIN_MENU {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("back_to_main_menu");
        }},
    RESTART {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("restart");
        }},
    NEXT_LEVEL {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("next_level");
        }},

    OK {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("ok");
        }},
    CANCEL {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return R.getString("cancel");
        }},

    HEALTH {
        public String getPath() {
            return "resources/sprites/ui/health";
        }},

    TEXT {
        public String getPath() {
            return "";
        }};

    @Override
    public abstract String getPath();
}
