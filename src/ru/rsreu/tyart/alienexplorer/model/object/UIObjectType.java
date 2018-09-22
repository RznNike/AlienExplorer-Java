package ru.rsreu.tyart.alienexplorer.model.object;

import ru.rsreu.tyart.alienexplorer.model.IPathEnum;

public enum UIObjectType implements IPathEnum {
    NEW_GAME {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "New game";
        }},
    CHOOSE_LEVEL {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "Choose level";
        }},
    EXIT {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "Exit";
        }},

    RESUME {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "Resume";
        }},
    BACK_TO_MAIN_MENU {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "Back to main menu";
        }},
    RESTART {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "Restart";
        }},
    NEXT_LEVEL {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "Next level";
        }},

    OK {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "OK";
        }},
    CANCEL {
        public String getPath() {
            return "";
        }

        @Override
        public String toString() {
            return "Cancel";
        }},

    HEALTH {
        public String getPath() {
            return "resources/sprites/ui/health";
        }},
    TIMER {
        public String getPath() {
            return "resources/sprites/ui/timer";
        }},

    TEXT {
        public String getPath() {
            return "";
        }};

    @Override
    public abstract String getPath();
}
