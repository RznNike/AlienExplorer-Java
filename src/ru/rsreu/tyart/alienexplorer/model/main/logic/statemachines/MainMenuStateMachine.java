package ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.globalutil.R;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.object.UIObject;
import ru.rsreu.tyart.alienexplorer.model.object.UIObjectType;
import ru.rsreu.tyart.alienexplorer.model.util.ModelEventType;
import ru.rsreu.tyart.alienexplorer.model.util.RoomLoader;

import java.util.ArrayList;
import java.util.List;

public class MainMenuStateMachine extends ModelStateMachine {
    public MainMenuStateMachine(GameRoom gameRoom) {
        super(gameRoom);

        initializeMainMenu();
    }

    @Override
    public void changeState(ControllerCommand command) {
        if ((getRoom().getUIObjects() != null) && (getRoom().getUIObjects().size() > 0)) {
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
                case ESC:
                    cancelAction();
                    break;
            }
            getRoom().getParent().sendEvent(ModelEventType.MENU_CHANGED);
        }
    }

    @Override
    protected void acceptAction() {
        UIObject selectedItem = getRoom().getUIObjects().get(getSelectedMenuItem());
        switch (getCurrentMenu()) {
            case OK:
                enterToMenu(selectedItem.getType());
                break;
            case NEW_GAME:
                if (selectedItem.getType() == UIObjectType.OK) {
                    setCurrentCommand(ModelStateMachineCommand.LOAD_LEVEL);
                    setSelectedMenuItem(RoomLoader.getAvailableLevels().get(0));
                } else {
                    enterToMenu(UIObjectType.OK);
                }
                break;
            case CHOOSE_LEVEL:
                setCurrentCommand(ModelStateMachineCommand.LOAD_LEVEL);
                setSelectedMenuItem(selectedItem.getNumber());
                break;
            case EXIT:
                if (selectedItem.getType() == UIObjectType.OK) {
                    setCurrentCommand(ModelStateMachineCommand.EXIT);
                } else {
                    enterToMenu(UIObjectType.OK);
                }
                break;
        }
    }

    @Override
    protected void cancelAction() {
        switch (getCurrentMenu()) {
            case OK:
                enterToMenu(UIObjectType.EXIT);
                break;
            default:
                enterToMenu(UIObjectType.OK);
                break;
        }
    }

    private void enterToMenu(UIObjectType type) {
        switch (type) {
            case OK:
                initializeMainMenu();
                break;
            case NEW_GAME:
            case EXIT:
                initializeConfirmationMenu(type);
                break;
            case CHOOSE_LEVEL:
                initializeChooseLevelMenu();
                break;
        }
    }

    private void initializeMainMenu() {
        List<UIObject> uiObjects = new ArrayList<UIObject>();

        for (int i = UIObjectType.NEW_GAME.ordinal(); i <= UIObjectType.EXIT.ordinal(); i++) {
            UIObject object = new UIObject();
            object.setType(UIObjectType.values()[i]);
            object.setState(0);
            uiObjects.add(object);
        }
        uiObjects.get(0).setState(1);
        getRoom().setUIObjects(uiObjects);

        setSelectedMenuItem(0);
        setMenuHeader(R.getString("app_name"));
        setCurrentMenu(UIObjectType.OK);
        setCurrentCommand(ModelStateMachineCommand.NONE);
    }

    private void initializeConfirmationMenu(UIObjectType type) {
        List<UIObject> uiObjects = new ArrayList<UIObject>();
        String caption = "";
        switch (type) {
            case NEW_GAME:
                caption = R.getString("start_a_new_game");
                setMenuHeader(R.getString("new_game"));
                break;
            case EXIT:
                caption = R.getString("close_game");
                setMenuHeader(R.getString("exit"));
                break;
        }
        UIObject captionObject = new UIObject();
        captionObject.setType(UIObjectType.TEXT);
        captionObject.setState(0);
        captionObject.setText(caption);
        captionObject.setSelectable(false);
        uiObjects.add(captionObject);

        for (int i = UIObjectType.OK.ordinal(); i <= UIObjectType.CANCEL.ordinal(); i++) {
            UIObject object = new UIObject();
            object.setType(UIObjectType.values()[i]);
            object.setState(0);
            uiObjects.add(object);
        }
        uiObjects.get(2).setState(1);
        getRoom().setUIObjects(uiObjects);

        setCurrentMenu(type);
        setSelectedMenuItem(2);
        setCurrentCommand(ModelStateMachineCommand.NONE);
    }

    private void initializeChooseLevelMenu() {
        List<Integer> levelIDs = RoomLoader.getAvailableLevels();
        List<UIObject> uiObjects = new ArrayList<UIObject>();
        for (Integer levelID : levelIDs) {
            UIObject object = new UIObject();
            object.setType(UIObjectType.TEXT);
            object.setState(0);
            object.setText(String.format("%s %d", R.getString("level"), levelID));
            object.setNumber(levelID);
            uiObjects.add(object);
        }
        uiObjects.get(0).setState(1);
        getRoom().setUIObjects(uiObjects);

        setSelectedMenuItem(0);
        setMenuHeader(R.getString("choose_level"));
        setCurrentMenu(UIObjectType.CHOOSE_LEVEL);
        setCurrentCommand(ModelStateMachineCommand.NONE);
    }
}
