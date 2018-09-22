package ru.rsreu.tyart.alienexplorer.model.object;

public class UIObject extends GameObject {
    private UIObjectType _type;
    private String _text;
    private int _number;
    private boolean _isSelectable;

    public UIObject() {
        _isSelectable = true;
    }

    public UIObject(boolean isSelectable) {
        _isSelectable = isSelectable;
    }

    @Override
    public int getTypeNumber() {
        return 0;
    }

    public UIObjectType getType() {
        return _type;
    }

    public void setType(UIObjectType value) {
        _type = value;
    }

    public String getText() {
        return _text;
    }

    public void setText(String value) {
        _text = value;
    }

    public int getNumber() {
        return _number;
    }

    public void setNumber(int value) {
        _number = value;
    }

    public boolean isSelectable() {
        return _isSelectable;
    }

    public void setSelectable(boolean value) {
        _isSelectable = value;
    }
}
