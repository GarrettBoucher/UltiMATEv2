package com.example.garrett.ultimatev2;

/**
 * Created by Garrett on 5/5/16.
 */
public class LogEvents {
    private int _id;
    private int _gameID;
    private String _eventString;
    private boolean _isValid;

    public LogEvents(int _gameID) {
        this._gameID = _gameID;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_gameID() {
        return _gameID;
    }

    public void set_gameID(int _gameID) {
        this._gameID = _gameID;
    }

    public String get_eventString() {
        return _eventString;
    }

    public void set_eventString(String _eventString) {
        this._eventString = _eventString;
    }

    public boolean is_isValid() {
        return _isValid;
    }

    public void set_isValid(boolean _isValid) {
        this._isValid = _isValid;
    }
}
