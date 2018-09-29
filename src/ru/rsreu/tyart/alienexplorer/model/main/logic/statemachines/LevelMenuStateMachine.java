package ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObjectType;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;
import ru.rsreu.tyart.alienexplorer.model.util.RoomLoader;

import java.util.ArrayList;
import java.util.List;

public class LevelMenuStateMachine extends ModelStateMachine {
    private boolean _menuDisplayed;

    public LevelMenuStateMachine(GameRoom gameRoom) {
        super(gameRoom);

        initializeLevelUI();
        setCurrentCommand(ModelStateMachineCommand.NONE);
        _menuDisplayed = false;
    }

    @Override
    public void changeState(ControllerCommand command) {
        setCurrentCommand(ModelStateMachineCommand.NONE);
        if ((getGameRoom().getUIObjects() != null) && (getGameRoom().getUIObjects().size() > 0)) {
            if (_menuDisplayed) {
                switch (command) {
                    case UP:
                        selectPrevMenuItem();
                        break;
                    case DOWN:
                        selectNextMenuItem();
                        break;
                    case OK:
                        acceptAction();
                        break;
                }
                getGameRoom().getParent().sendEvent(ModelEventType.MENU_CHANGED);
            }
            if (command == ControllerCommand.ESC) {
                cancelAction();
            }
        }
    }

    @Override
    protected void acceptAction() {
        UIObjectType selectedItem = getGameRoom().getUIObjects().get(getSelectedMenuItem()).getType();
        switch (selectedItem) {
            case RESUME:
                enterToMenu(UIObjectType.OK);
                break;
            case RESTART:
                // TODO set level number
                setCurrentCommand(ModelStateMachineCommand.LOAD_LEVEL);
                break;
            case NEXT_LEVEL:
                // TODO set level number
                setCurrentCommand(ModelStateMachineCommand.LOAD_LEVEL);
                break;
            case BACK_TO_MAIN_MENU:
                setCurrentCommand(ModelStateMachineCommand.LOAD_MENU);
                break;
        }
    }

    @Override
    protected void cancelAction() {
        switch (getCurrentMenu()) {
            case OK:
                enterToMenu(UIObjectType.RESUME);
                break;
            case RESUME:
                enterToMenu(UIObjectType.OK);
                break;
            default:
                setCurrentCommand(ModelStateMachineCommand.LOAD_MENU);
                break;
        }
    }

    public void enterToMenu(UIObjectType type) {
        switch (type) {
            case OK:
                initializeLevelUI();
                break;
            case RESUME:
                initializePauseMenu();
                break;
            case NEXT_LEVEL:
                initializeWinMenu();
                break;
            case RESTART:
                initializeLoseMenu();
                break;
        }
    }

    private void initializeLevelUI() {
        getGameRoom().setUIObjects(new ArrayList<UIObject>());
        UIObject health = new UIObject();
        health.setType(UIObjectType.HEALTH);
        getGameRoom().getUIObjects().add(health);

        setCurrentMenu(UIObjectType.OK);
        setCurrentCommand(ModelStateMachineCommand.RESUME);
        _menuDisplayed = false;

        getGameRoom().getParent().sendEvent(ModelEventType.CONTEXT_MENU_CLOSED);
    }

    private void initializePauseMenu() {
        getGameRoom().setUIObjects(new ArrayList<UIObject>());

        UIObject uiObject = new UIObject();
        uiObject.setType(UIObjectType.TEXT);
        uiObject.setState(0);
        uiObject.setText("GAME PAUSED");
        uiObject.setSelectable(false);
        getGameRoom().getUIObjects().add(uiObject);

        uiObject = new UIObject();
        uiObject.setType(UIObjectType.RESUME);
        uiObject.setState(1);
        getGameRoom().getUIObjects().add(uiObject);

        uiObject = new UIObject();
        uiObject.setType(UIObjectType.RESTART);
        uiObject.setState(0);
        getGameRoom().getUIObjects().add(uiObject);

        uiObject = new UIObject();
        uiObject.setType(UIObjectType.BACK_TO_MAIN_MENU);
        uiObject.setState(0);
        getGameRoom().getUIObjects().add(uiObject);

        setMenuHeader(String.format("Level %d", getGameRoom().getId()));
        setSelectedMenuItem(1);
        setCurrentMenu(UIObjectType.RESUME);
        setCurrentCommand(ModelStateMachineCommand.PAUSE);
        _menuDisplayed = true;

        getGameRoom().getParent().sendEvent(ModelEventType.CONTEXT_MENU_LOADED);
    }

    private void initializeWinMenu() {
        getGameRoom().setUIObjects(new ArrayList<UIObject>());

        UIObject uiObject = new UIObject();
        uiObject.setType(UIObjectType.TEXT);
        uiObject.setState(0);
        uiObject.setText("YOU WIN!");
        uiObject.setSelectable(false);
        getGameRoom().getUIObjects().add(uiObject);

        int currentLevel = getGameRoom().getId();
        List<Integer> levels = RoomLoader.checkAvailableLevels();
        int lastLevel = levels.get(levels.size() - 1);
        if (currentLevel < lastLevel) {
            uiObject = new UIObject();
            uiObject.setType(UIObjectType.NEXT_LEVEL);
            uiObject.setState(1);
            getGameRoom().getUIObjects().add(uiObject);

            uiObject = new UIObject();
            uiObject.setType(UIObjectType.RESTART);
            uiObject.setState(0);
            getGameRoom().getUIObjects().add(uiObject);

            uiObject = new UIObject();
            uiObject.setType(UIObjectType.BACK_TO_MAIN_MENU);
            uiObject.setState(0);
            getGameRoom().getUIObjects().add(uiObject);

            setSelectedMenuItem(1);
        } else {
            uiObject = new UIObject();
            uiObject.setType(UIObjectType.RESTART);
            uiObject.setState(0);
            getGameRoom().getUIObjects().add(uiObject);

            uiObject = new UIObject();
            uiObject.setType(UIObjectType.BACK_TO_MAIN_MENU);
            uiObject.setState(1);
            getGameRoom().getUIObjects().add(uiObject);

            setSelectedMenuItem(2);
        }

        setMenuHeader(String.format("Level %d", getGameRoom().getId()));
        setCurrentMenu(UIObjectType.NEXT_LEVEL);
        setCurrentCommand(ModelStateMachineCommand.PAUSE);
        _menuDisplayed = true;

        getGameRoom().getParent().sendEvent(ModelEventType.CONTEXT_MENU_LOADED);
    }

    private void initializeLoseMenu() {
        getGameRoom().setUIObjects(new ArrayList<UIObject>());

        UIObject uiObject = new UIObject();
        uiObject.setType(UIObjectType.TEXT);
        uiObject.setState(0);
        uiObject.setText("YOU LOSE...");
        uiObject.setSelectable(false);
        getGameRoom().getUIObjects().add(uiObject);

        uiObject = new UIObject();
        uiObject.setType(UIObjectType.RESTART);
        uiObject.setState(1);
        getGameRoom().getUIObjects().add(uiObject);

        uiObject = new UIObject();
        uiObject.setType(UIObjectType.BACK_TO_MAIN_MENU);
        uiObject.setState(0);
        getGameRoom().getUIObjects().add(uiObject);

        setMenuHeader(String.format("Level %d", getGameRoom().getId()));
        setSelectedMenuItem(1);
        setCurrentMenu(UIObjectType.NEXT_LEVEL);
        setCurrentCommand(ModelStateMachineCommand.PAUSE);
        _menuDisplayed = true;

        getGameRoom().getParent().sendEvent(ModelEventType.CONTEXT_MENU_LOADED);
    }
}
