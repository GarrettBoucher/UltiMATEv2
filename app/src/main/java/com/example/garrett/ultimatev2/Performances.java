package com.example.garrett.ultimatev2;

/**
 * Created by Garrett on 5/5/16.
 */
public class Performances {
    private int _playerID;
    private int _gameID;
    private String _playerName;
    private int _dropNumber;
    private int _scoreNumber;
    private int _dNumber;
    private int _assistNumber;
    private int _throwawayNumber;

    public Performances(String _playerName, int _gameID) {
        this._playerName = _playerName;
        this._gameID = _gameID;
    }

    public int get_playerID() {
        return _playerID;
    }

    public void set_playerID(int _playerID) {
        this._playerID = _playerID;
    }

    public int get_gameID() {
        return _gameID;
    }

    public void set_gameID(int _gameID) {
        this._gameID = _gameID;
    }

    public int get_dropNumber() {
        return _dropNumber;
    }

    public void set_dropNumber(int _dropNumber) {
        this._dropNumber = _dropNumber;
    }

    public int get_scoreNumber() {
        return _scoreNumber;
    }

    public void set_scoreNumber(int _scoreNumber) {
        this._scoreNumber = _scoreNumber;
    }

    public int get_dNumber() {
        return _dNumber;
    }

    public void set_dNumber(int _dNumber) {
        this._dNumber = _dNumber;
    }

    public int get_assistNumber() {
        return _assistNumber;
    }

    public void set_assistNumber(int _assistNumber) {
        this._assistNumber = _assistNumber;
    }

    public int get_throwawayNumber() {
        return _throwawayNumber;
    }

    public void set_throwawayNumber(int _throwawayNumber) {
        this._throwawayNumber = _throwawayNumber;
    }
}
