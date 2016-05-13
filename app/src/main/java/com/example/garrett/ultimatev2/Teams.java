package com.example.garrett.ultimatev2;

/**
 * Created by Garrett on 4/20/16.
 */
public class Teams {

    private int _id;
    private String _teamname;

    public Teams(String teamname) {
        this._teamname = teamname;
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
}
