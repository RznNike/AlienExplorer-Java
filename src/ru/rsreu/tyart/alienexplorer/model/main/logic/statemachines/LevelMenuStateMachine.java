package ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObjectType;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;
import ru.rsreu.tyart.alienexplorer.model.util.RoomLoader;

import java.util.ArrayList;
import java.util.Arrays;
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
        if ((getRoom().getUIObjects() != null) && (getRoom().getUIObjects().size() > 0)) {
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
                getRoom().getParent().sendEvent(ModelEventType.MENU_CHANGED);
            }
            if (command == ControllerCommand.ESC) {
                cancelAction();
            }
        }
    }

    @Override
    protected void acceptAction() {
        UIObjectType selectedItem = getRoom().getUIObjects().get(getSelectedMenuItem()).getType();
        switch (selectedItem) {
            case RESUME:
                enterToMenu(UIObjectType.OK);
                break;
            case RESTART:
                setSelectedMenuItem(getRoom().getId());
                setCurrentCommand(ModelStateMachineCommand.LOAD_LEVEL);
                break;
            case NEXT_LEVEL:
                List<Integer> levelNumbers = RoomLoader.getAvailableLevels();
                int currentLevelPosition = levelNumbers.indexOf(getRoom().getId());
                int nextLevel = levelNumbers.get(currentLevelPosition + 1);
                setSelectedMenuItem(nextLevel);
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
        getRoom().setUIObjects(new ArrayList<UIObject>());
        UIObject health = new UIObject();
        health.setType(UIObjectType.HEALTH);
        getRoom().getUIObjects().add(health);

        setCurrentMenu(UIObjectType.OK);
        setCurrentCommand(ModelStateMachineCommand.RESUME);
        _menuDisplayed = false;

        getRoom().getParent().sendEvent(ModelEventType.CONTEXT_MENU_CLOSED);
    }

    private void initializePauseMenu() {
        getRoom().setUIObjects(new ArrayList<UIObject>(Arrays.asList(
                new UIObject(UIObjectType.TEXT, 0, "GAME PAUSED", false),
                new UIObject(UIObjectType.RESUME, 1),
                new UIObject(UIObjectType.RESTART, 0),
                new UIObject(UIObjectType.BACK_TO_MAIN_MENU, 0)
        )));

        setMenuHeader(String.format("Level %d", getRoom().getId()));
        setSelectedMenuItem(1);
        setCurrentMenu(UIObjectType.RESUME);
        setCurrentCommand(ModelStateMachineCommand.PAUSE);
        _menuDisplayed = true;

        getRoom().getParent().sendEvent(ModelEventType.CONTEXT_MENU_LOADED);
    }

    private void initializeWinMenu() {
        getRoom().setUIObjects(new ArrayList<UIObject>());
        getRoom().getUIObjects().add(new UIObject(UIObjectType.TEXT, 0, "YOU WIN!", false));

        int currentLevel = getRoom().getId();
        List<Integer> levels = RoomLoader.getAvailableLevels();
        int lastLevel = levels.get(levels.size() - 1);
        if (currentLevel < lastLevel) {
            getRoom().getUIObjects().addAll(Arrays.asList(
                    new UIObject(UIObjectType.NEXT_LEVEL, 1),
                    new UIObject(UIObjectType.RESTART, 0),
                    new UIObject(UIObjectType.BACK_TO_MAIN_MENU, 0)
            ));
            setSelectedMenuItem(1);
        } else {
            getRoom().getUIObjects().addAll(Arrays.asList(
                    new UIObject(UIObjectType.RESTART, 0),
                    new UIObject(UIObjectType.BACK_TO_MAIN_MENU, 1)
            ));
            setSelectedMenuItem(2);
        }

        setMenuHeader(String.format("Level %d", getRoom().getId()));
        setCurrentMenu(UIObjectType.NEXT_LEVEL);
        setCurrentCommand(ModelStateMachineCommand.PAUSE);
        _menuDisplayed = true;

        getRoom().getParent().sendEvent(ModelEventType.CONTEXT_MENU_LOADED);
    }

    private void initializeLoseMenu() {
        getRoom().setUIObjects(new ArrayList<UIObject>(Arrays.asList(
                new UIObject(UIObjectType.TEXT, 0, "YOU LOSE...", false),
                new UIObject(UIObjectType.RESTART, 1),
                new UIObject(UIObjectType.BACK_TO_MAIN_MENU, 0)
        )));

        setMenuHeader(String.format("Level %d", getRoom().getId()));
        setSelectedMenuItem(1);
        setCurrentMenu(UIObjectType.NEXT_LEVEL);
        setCurrentCommand(ModelStateMachineCommand.PAUSE);
        _menuDisplayed = true;

        getRoom().getParent().sendEvent(ModelEventType.CONTEXT_MENU_LOADED);
    }
}
