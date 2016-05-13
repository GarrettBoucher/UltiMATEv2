package com.example.garrett.ultimatev2;

/**
 * Created by Garrett on 5/5/16.
 */
public class Games {
    private int _id;
    private String _teamname;
    private String _gamename;
    private String _opponent;
    private int _teamscore;
    private int _opponentscore;

    public Games(){
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_teamname() {
        return _teamname;
    }

    public void set_teamname(String _teamname) {
        this._teamname = _teamname;
    }

    public String get_gamename() {
        return _gamename;
    }

    public void set_gamename(String _gamename) {
        this._gamename = _gamename;
    }

    public String get_opponent() {
        return _opponent;
    }

    public void set_opponent(String _opponent) {
        this._opponent = _opponent;
    }

    public int get_teamscore() {
        return _teamscore;
    }

    public void set_teamscore(int _teamscore) {
        this._teamscore = _teamscore;
    }

    public int get_opponentscore() {
        return _opponentscore;
    }

    public void set_opponentscore(int _opponentscore) {
        this._opponentscore = _opponentscore;
    }
}
