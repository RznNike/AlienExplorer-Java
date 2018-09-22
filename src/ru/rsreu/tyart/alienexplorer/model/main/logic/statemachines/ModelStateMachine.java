package ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObjectType;

public abstract class ModelStateMachine {
    private GameRoom _gameRoom;
    private UIObjectType _currentMenu;
    private ModelStateMachineCommand _currentCommand;
    private int _selectedMenuItem;
    private String _menuHeader;

    public ModelStateMachine(GameRoom gameRoom) {
        _gameRoom = gameRoom;
    }

    public abstract void changeState(ControllerCommand command);

    protected abstract void acceptAction();

    protected abstract void cancelAction();

    protected void selectMenuItem(int itemNumber) {
        for (UIObject item : _gameRoom.getUIObjects()) {
            item.setState(0);
        }
        _gameRoom.getUIObjects().get(itemNumber).setState(1);
    }

    protected void selectPrevMenuItem() {
        if ((_gameRoom.getUIObjects() != null) && (_gameRoom.getUIObjects().size() > 0)) {
            int newSelection = _selectedMenuItem - 1;
            if (!_gameRoom.getUIObjects().get(newSelection).isSelectable()) {
                newSelection--;
            }
            if (newSelection < 0) {
                newSelection = _gameRoom.getUIObjects().size() - 1;
            }
            _selectedMenuItem = newSelection;
            selectMenuItem(_selectedMenuItem);
        }
    }

    protected void selectNextMenuItem() {
        if ((_gameRoom.getUIObjects() != null) && (_gameRoom.getUIObjects().size() > 0)) {
            int newSelection = _selectedMenuItem + 1;

            if (newSelection >= _gameRoom.getUIObjects().size()) {
                newSelection = 0;
            }
            if (!_gameRoom.getUIObjects().get(newSelection).isSelectable()) {
                newSelection++;
            }
            _selectedMenuItem = newSelection;
            selectMenuItem(_selectedMenuItem);
        }
    }

    protected GameRoom getGameRoom() {
        return _gameRoom;
    }

    public void setGameRoom(GameRoom value) {
        _gameRoom = value;
    }

    protected UIObjectType getCurrentMenu() {
        return _currentMenu;
    }

    protected void setCurrentMenu(UIObjectType value) {
        _currentMenu = value;
    }

    public ModelStateMachineCommand getCurrentCommand() {
        return _currentCommand;
    }

    protected void setCurrentCommand(ModelStateMachineCommand value) {
        _currentCommand = value;
    }

    public int getSelectedMenuItem() {
        return _selectedMenuItem;
    }

    protected void setSelectedMenuItem(int value) {
        _selectedMenuItem = value;
    }

    public String getMenuHeader() {
        return _menuHeader;
    }

    protected void setMenuHeader(String value) {
        _menuHeader = value;
    }
}
