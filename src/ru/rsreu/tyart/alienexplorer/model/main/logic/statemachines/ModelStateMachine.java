package ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObjectType;

public abstract class ModelStateMachine {
    private GameRoom _room;
    private UIObjectType _currentMenu;
    private ModelStateMachineCommand _currentCommand;
    private int _selectedMenuItem;
    private String _menuHeader;

    ModelStateMachine(GameRoom room) {
        _room = room;
    }

    public abstract void changeState(ControllerCommand command);

    protected abstract void acceptAction();

    protected abstract void cancelAction();

    private void selectMenuItem(int itemNumber) {
        for (UIObject item : _room.getUIObjects()) {
            item.setState(0);
        }
        _room.getUIObjects().get(itemNumber).setState(1);
    }

    void selectPrevMenuItem() {
        if ((_room.getUIObjects() != null) && (_room.getUIObjects().size() > 0)) {
            int newSelection = _selectedMenuItem - 1;
            if (newSelection < 0) {
                newSelection = _room.getUIObjects().size() - 1;
            }
            if (!_room.getUIObjects().get(newSelection).isSelectable()) {
                newSelection--;
                if (newSelection < 0) {
                    newSelection = _room.getUIObjects().size() - 1;
                }
            }
            _selectedMenuItem = newSelection;
            selectMenuItem(_selectedMenuItem);
        }
    }

    void selectNextMenuItem() {
        if ((_room.getUIObjects() != null) && (_room.getUIObjects().size() > 0)) {
            int newSelection = _selectedMenuItem + 1;

            if (newSelection >= _room.getUIObjects().size()) {
                newSelection = 0;
            }
            if (!_room.getUIObjects().get(newSelection).isSelectable()) {
                newSelection++;
            }
            _selectedMenuItem = newSelection;
            selectMenuItem(_selectedMenuItem);
        }
    }

    protected GameRoom getRoom() {
        return _room;
    }

    UIObjectType getCurrentMenu() {
        return _currentMenu;
    }

    void setCurrentMenu(UIObjectType value) {
        _currentMenu = value;
    }

    public ModelStateMachineCommand getCurrentCommand() {
        return _currentCommand;
    }

    void setCurrentCommand(ModelStateMachineCommand value) {
        _currentCommand = value;
    }

    public int getSelectedMenuItem() {
        return _selectedMenuItem;
    }

    void setSelectedMenuItem(int value) {
        _selectedMenuItem = value;
    }

    public String getMenuHeader() {
        return _menuHeader;
    }

    void setMenuHeader(String value) {
        _menuHeader = value;
    }
}
