package com.example.garrett.ultimatev2;

/**
 * Created by Garrett on 4/21/16.
 */
public class Players {

    private int _id;
    private String _playername;
    private int _teamid;
    private String _teamname;

    public Players(String playername, String teamname) {
        this._playername = playername;
        this._teamname = teamname;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_playername() {
        return _playername;
    }

    public void set_teamname(String playername) {
        this._playername = playername;
    }

    public int get_teamid() {
        return _teamid;
    }

    public void set_teamid(int teamid) {
        this._teamid = teamid;
    }

    public String get_teamname() {
        return _teamname;
    }
}
