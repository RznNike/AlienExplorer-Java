package ru.rsreu.tyart.alienexplorer.model.main.logic;

public class RoomWorkResult {
    private RoomWorkResultType _resultType;
    private int _resultValue;

    public RoomWorkResult() {
        _resultType = RoomWorkResultType.EXIT;
        _resultValue = 0;
    }

    public RoomWorkResult(RoomWorkResultType resultType) {
        _resultType = resultType;
        _resultValue = 0;
    }

    public RoomWorkResult(RoomWorkResultType resultType, int resultValue) {
        _resultType = resultType;
        _resultValue = resultValue;
    }

    public RoomWorkResultType getResultType() {
        return _resultType;
    }

    public void setResultType(RoomWorkResultType resultType) {
        this._resultType = resultType;
    }

    public int getResultValue() {
        return _resultValue;
    }

    public void setResultValue(int resultValue) {
        this._resultValue = resultValue;
    }
}
