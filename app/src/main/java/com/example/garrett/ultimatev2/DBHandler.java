package com.example.garrett.ultimatev2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.garrett.ultimatev2.dummy.TeamContent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Garrett on 4/19/16.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "ultimate.db";
    //TEAMS table
    public static final String TABLE_TEAMS = "teams";
    public static final String COLUMN_TEAMS_ID = "_id";
    public static final String COLUMN_TEAMS_TEAMNAME = "teamname";
    //PLAYERS table
    public static final String TABLE_PLAYERS = "players";
    public static final String COLUMN_PLAYERS_ID = "_id";
    public static final String COLUMN_PLAYERS_PLAYERNAME = "playername";
    public static final String COLUMN_PLAYERS_TEAMID = "teamid";
    public static final String COLUMN_PLAYERS_TEAMNAME = "teamname";
    //GAMES table
    public static final String TABLE_GAMES = "games";
    public static final String COLUMN_GAMES_ID = "_id";
    public static final String COLUMN_GAMES_GAMENAME = "gamename";
    public static final String COLUMN_GAMES_OPPONENT = "opponent";
    public static final String COLUMN_GAMES_TEAMNAME = "teamname";
    public static final String COLUMN_GAMES_TEAMSCORE = "tamscore";
    public static final String COLUMN_GAMES_OPPONENTSCORE = "opponentscore";
    //PERFORMANCES table
    public static final String TABLE_PERFORMANCES = "performances";
    public static final String COLUMN_PERFORMANCES_PLAYERID = "player_id";
    public static final String COLUMN_PERFORMANCES_GAMEID = "game_id";
    public static final String COLUMN_PERFORMANCES_TEAMNAME = "team_name";
    public static final String COLUMN_PERFORMANCES_PLAYERNAME = "player_name";
    public static final String COLUMN_PERFORMANCES_DROPS = "drops";
    public static final String COLUMN_PERFORMANCES_SCORES = "scores";
    public static final String COLUMN_PERFORMANCES_Ds = "ds";
    public static final String COLUMN_PERFORMANCES_ASSISTS = "assists";
    public static final String COLUMN_PERFORMANCES_THROWAWAYS = "throwaways";
    //LOGEVENTS table
    public static final String TABLE_LOGEVENTS = "logevents";
    public static final String COLUMN_LOGEVENTS_LOGID = "log_id";
    public static final String COLUMN_LOGEVENTS_GAMEID = "game_id";
    public static final String COLUMN_LOGEVENTS_EVENT_STRING = "eventstring";
    public static final String COLUMN_LOGEVENTS_PLAYERID = "player_id";
    public static final String COLUMN_LOGEVENTS_EVENT_VALIDITY = "eventvalidity";
//                    0 means the event is valid, 1 means invalid
    public static final String COLUMN_LOGEVENTS_EVENT_TYPE = "eventtype";
    //Encoding for event type in LOGEVENTS table
    public static final int intScore = 0;
    public static final int intAssist = 1;
    public static final int intD = 2;
    public static final int intScoredOn = 3;
    public static final int intThrowaway = 4;
    public static final int intDrop = 5;


    private static final String SQL_CREATE_TABLE_TEAMS =
            "CREATE TABLE " + TABLE_TEAMS + " (" +
                    COLUMN_TEAMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TEAMS_TEAMNAME + " TEXT " +
                    ");";
    private static final String SQL_CREATE_TABLE_PlAYERS =
            "CREATE TABLE " + TABLE_PLAYERS + " (" +
                    COLUMN_PLAYERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PLAYERS_PLAYERNAME + " TEXT, " +
                    COLUMN_PLAYERS_TEAMID + " INTEGER, " +
                    COLUMN_PLAYERS_TEAMNAME+ " TEXT, " +
//                    "FOREIGN KEY ("+COLUMN_PLAYERS_TEAMID+") REFERENCES "+TABLE_TEAMS+"("+COLUMN_TEAMS_ID+")"+
                    "FOREIGN KEY ("+COLUMN_PLAYERS_TEAMNAME+") REFERENCES "+TABLE_TEAMS+"("+COLUMN_TEAMS_TEAMNAME+")"+
                    ");";
    private static final String SQL_CREATE_TABLE_GAMES =
            "CREATE TABLE " + TABLE_GAMES + " (" +
                    COLUMN_GAMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GAMES_GAMENAME + " TEXT, " +
                    COLUMN_GAMES_OPPONENT + " TEXT, " +
                    COLUMN_GAMES_TEAMNAME + " TEXT, " +
                    COLUMN_GAMES_TEAMSCORE + " INTEGER DEFAULT 0, " +
                    COLUMN_GAMES_OPPONENTSCORE + " INTEGER DEFAULT 0, " +
                    "FOREIGN KEY ("+COLUMN_GAMES_TEAMNAME+") REFERENCES "+TABLE_TEAMS+"("+COLUMN_TEAMS_TEAMNAME+")"+
                    ");";
    private static final String SQL_CREATE_TABLE_PERFORMANCES =
            "CREATE TABLE " + TABLE_PERFORMANCES + " (" +
                    COLUMN_PERFORMANCES_PLAYERID + " INTEGER, " +
                    COLUMN_PERFORMANCES_GAMEID + " INTEGER, " +
                    COLUMN_PERFORMANCES_TEAMNAME + " Text, " +
                    COLUMN_PERFORMANCES_PLAYERNAME + " TEXT, " +
                    COLUMN_PERFORMANCES_DROPS + " INTEGER DEFAULT 0, " +
                    COLUMN_PERFORMANCES_SCORES + " INTEGER DEFAULT 0, " +
                    COLUMN_PERFORMANCES_Ds + " INTEGER DEFAULT 0, " +
                    COLUMN_PERFORMANCES_ASSISTS + " INTEGER DEFAULT 0, " +
                    COLUMN_PERFORMANCES_THROWAWAYS + " INTEGER DEFAULT 0, " +
                    "FOREIGN KEY ("+COLUMN_PERFORMANCES_TEAMNAME+") REFERENCES "+TABLE_PLAYERS+"("+COLUMN_PLAYERS_TEAMNAME+"),"+
                    "FOREIGN KEY ("+COLUMN_PERFORMANCES_PLAYERID+") REFERENCES "+TABLE_PLAYERS+"("+COLUMN_PLAYERS_ID+"),"+
                    "FOREIGN KEY ("+COLUMN_PERFORMANCES_GAMEID  +") REFERENCES "+TABLE_GAMES+  "("+COLUMN_GAMES_ID+  "),"+
                    "PRIMARY KEY ("+COLUMN_PERFORMANCES_PLAYERID+", "+COLUMN_PERFORMANCES_GAMEID+")"+
                    ");";
    private static final String SQL_CREATE_TABLE_LOGEVENTS =
            "CREATE TABLE " + TABLE_LOGEVENTS + " (" +
                    COLUMN_LOGEVENTS_LOGID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOGEVENTS_GAMEID + " INTEGER, " +
                    COLUMN_LOGEVENTS_EVENT_STRING + " TEXT, " +
                    COLUMN_LOGEVENTS_PLAYERID + " INTEGER," +
                    COLUMN_LOGEVENTS_EVENT_VALIDITY + " INTEGER DEFAULT 0, " +
//                    0 means the event is valid, 1 means invalid
                    COLUMN_LOGEVENTS_EVENT_TYPE + " INTEGER, " +
                    "FOREIGN KEY ("+COLUMN_LOGEVENTS_GAMEID+") REFERENCES "+TABLE_GAMES+"("+COLUMN_GAMES_ID+")"+
                    ");";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFORMANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGEVENTS);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TEAMS);
        db.execSQL(SQL_CREATE_TABLE_PlAYERS);
        db.execSQL(SQL_CREATE_TABLE_GAMES);
        db.execSQL(SQL_CREATE_TABLE_PERFORMANCES);
        db.execSQL(SQL_CREATE_TABLE_LOGEVENTS);

    }

    //Add a new row to the database
    public void addTeam(Teams team){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEAMS_TEAMNAME, team.get_teamname());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TEAMS, null, values);
        db.close();
    }

    public boolean teamExists(String teamName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT 1 FROM "+TABLE_TEAMS+" WHERE "+ COLUMN_TEAMS_TEAMNAME +"=?", new String[] {teamName});
        boolean exists = c.moveToFirst();
        c.close();
        db.close();
        return exists;
    }

    //Delete a team from the database
    public void deleteTeam(String teamName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_TEAMS+" WHERE "+ COLUMN_TEAMS_TEAMNAME + "='"+teamName+"'");
        db.close();
    }

    //Print out the TEAMS table as a string
    public String databaseTeamsToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TEAMS + " Where 1";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("teamname"))!= null){
                dbString += c.getString(c.getColumnIndex("teamname"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

    //Put teams in the array list for master/detail flow
    public void putTeamsInList(){

        TeamContent.ITEMS = new ArrayList<TeamContent.TeamItem>();
        TeamContent.ITEM_MAP = new HashMap<String, TeamContent.TeamItem>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TEAMS + " ORDER BY "+COLUMN_TEAMS_TEAMNAME+" ASC";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()){
            String teamName = "";
            String teamID = "";
            if(c.getString(c.getColumnIndex(COLUMN_TEAMS_TEAMNAME))!= null){
                teamName = c.getString(c.getColumnIndex(COLUMN_TEAMS_TEAMNAME));
                teamID = c.getString(c.getColumnIndex(COLUMN_TEAMS_ID));
                TeamContent.addItem(new TeamContent.TeamItem(teamID, teamName));

            }
            c.moveToNext();
        }
        c.close();
        db.close();
    }

    //Add a new row to the database
    public void addPlayer(Players player){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYERS_PLAYERNAME, player.get_playername());
        values.put(COLUMN_PLAYERS_TEAMNAME, player.get_teamname());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PLAYERS, null, values);
        db.close();
    }

    public boolean playerExists(String playerName, String teamName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT 1 FROM "+TABLE_PLAYERS+" WHERE "+ COLUMN_PLAYERS_PLAYERNAME +" =? AND "+COLUMN_PLAYERS_TEAMNAME+" = '"+teamName+"'",new String[] {playerName});
        boolean exists = c.moveToFirst();
        c.close();
        db.close();
        return exists;
    }

    //Delete a player from the database
    public void deletePlayer(String playerName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_PLAYERS+" WHERE "+ COLUMN_PLAYERS_PLAYERNAME + "='"+playerName+"'");
        db.close();
    }

    //Print out the PLAYERS table as a string
    public String databasePlayersToString(String teamName){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PLAYERS + " Where " +COLUMN_PLAYERS_TEAMNAME+ " = '" +teamName+"'";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_PLAYERS_PLAYERNAME))!= null){
                dbString += c.getString(c.getColumnIndex(COLUMN_PLAYERS_PLAYERNAME));
                dbString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

//    public String getPlayersAsString(String teamName){
//        String playersString = "";
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_PLAYERS + " Where "+ COLUMN_PLAYERS_TEAMNAME+" = '"+teamName+"'";
//
//        //Cursor point to a location in your results
//        Cursor c = db.rawQuery(query, null);
//        //Move to the first row in your results
//        c.moveToFirst();
//
//        while(!c.isAfterLast()){
//            if(c.getString(c.getColumnIndex(COLUMN_PLAYERS_PLAYERNAME))!= null){
//                playersString += c.getString(c.getColumnIndex(COLUMN_PLAYERS_PLAYERNAME));
//                playersString += "\n";
//            }
//            c.moveToNext();
//        }
//        c.close();
//        db.close();
//        return playersString;
//    }

    public ArrayList getTeamsArrayList(){
        ArrayList<String> teamString = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TEAMS + " ORDER BY "+COLUMN_TEAMS_TEAMNAME+" ASC";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        String printString = "";
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_TEAMS_ID))!= null){
                teamString.add(c.getString(c.getColumnIndex(COLUMN_TEAMS_TEAMNAME)));
                printString += c.getString(c.getColumnIndex(COLUMN_TEAMS_TEAMNAME));
                printString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        Log.i("TEAMS", printString);
        return teamString;
    }

    public ArrayList getPlayersArrayList(String teamName){
        ArrayList<String> playerString = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PLAYERS + " Where "+ COLUMN_PLAYERS_TEAMNAME+" = '"+teamName+"' ORDER BY "+COLUMN_PLAYERS_PLAYERNAME+" ASC";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        String printString = "";
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_PLAYERS_PLAYERNAME))!= null){
                playerString.add(c.getString(c.getColumnIndex(COLUMN_PLAYERS_PLAYERNAME)));
                printString += c.getString(c.getColumnIndex(COLUMN_PLAYERS_PLAYERNAME));
                printString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        Log.i("PLAYERS", printString);
        return playerString;
    }

    public ArrayList getGamesArrayList(String teamName){
        ArrayList<String> gameString = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GAMES + " Where "+COLUMN_GAMES_TEAMNAME+" = '"+teamName+"'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        String printString = "";
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_GAMES_ID))!= null){
                gameString.add(c.getString(c.getColumnIndex(COLUMN_GAMES_GAMENAME)));
                printString += c.getString(c.getColumnIndex(COLUMN_GAMES_GAMENAME));
                printString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        Log.i("GAMES", printString);
        return gameString;
    }

    public ArrayList getGamesIDArrayList(String teamName){
        ArrayList<Integer> gameString = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GAMES + " Where "+COLUMN_GAMES_TEAMNAME+" = '"+teamName+"'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        String printString = "";
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_GAMES_ID))!= null){
                gameString.add(c.getInt(c.getColumnIndex(COLUMN_GAMES_ID)));
                printString += c.getString(c.getColumnIndex(COLUMN_GAMES_ID));
                printString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        Log.i("GAMESPositions", printString);
        return gameString;
    }

    public Games addGame(Games game){
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAMES_GAMENAME, game.get_gamename());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_GAMES, null, values);
        db.close();
        game.set_id(getIdOfLatestGame());
        return game;
    }

    public int getIdOfLatestGame(){
        int gameID;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_GAMES, null);
        c.moveToLast();
        gameID = c.getInt(c.getColumnIndex(COLUMN_GAMES_ID));
        return gameID;
    }

    public void addPerformance(String playerName, String teamName, Games game){
        int playerID = getPlayerID(playerName, teamName);
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERFORMANCES_PLAYERID, playerID);
        values.put(COLUMN_PERFORMANCES_PLAYERNAME, playerName);
        values.put(COLUMN_PERFORMANCES_GAMEID, game.get_id());
        values.put(COLUMN_PERFORMANCES_TEAMNAME, teamName);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PERFORMANCES, null, values);
        db.close();
    }

    public int getPlayerID(String playerName, String teamName){
        int playerID;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_PLAYERS+" WHERE "+ COLUMN_PLAYERS_PLAYERNAME +" = '"+playerName+"' AND "+COLUMN_PLAYERS_TEAMNAME+" = '"+teamName+"'",null);
        c.moveToFirst();
        playerID = c.getInt(c.getColumnIndex(COLUMN_PLAYERS_ID));
        c.close();
        db.close();
        return playerID;
    }

    public void addScore(String playerName, String teamName, Games game, String timeString){
        int playerID = getPlayerID(playerName,teamName);
        String updateScore =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_SCORES+" = " +
                        COLUMN_PERFORMANCES_SCORES+" + 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateScore);
        updateScore =
                "UPDATE "+TABLE_GAMES+" SET "+COLUMN_GAMES_TEAMSCORE+" = " +
                        COLUMN_GAMES_TEAMSCORE+" + 1 WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        db.execSQL(updateScore);
        db.close();
        String eventString = timeString + " :: Score by "+playerName;
        addLogEvent(game,eventString,intScore,playerName,teamName);
    }

    public void addAssist(String playerName, String teamName, Games game, String timeString){
        int playerID = getPlayerID(playerName,teamName);
        String updateAssist =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_ASSISTS+" = " +
                        COLUMN_PERFORMANCES_ASSISTS+" + 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateAssist);
        db.close();
        String eventString = timeString + " :: Assist by "+playerName;
        addLogEvent(game,eventString,intAssist,playerName,teamName);
    }

    public void addD(String playerName, String teamName, Games game, String timeString){
        int playerID = getPlayerID(playerName,teamName);
        String updateDs =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_Ds+" = " +
                        COLUMN_PERFORMANCES_Ds+" + 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateDs);
        db.close();
        String eventString = timeString + " :: 'D' by "+playerName;
        addLogEvent(game,eventString,intD,playerName,teamName);
    }

    public void addThrowaway(String playerName, String teamName, Games game, String timeString){
        int playerID = getPlayerID(playerName,teamName);
        String updateThrowaway =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_THROWAWAYS+" = " +
                        COLUMN_PERFORMANCES_THROWAWAYS+" + 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateThrowaway);
        db.close();
        String eventString = timeString+" :: Throwaway by "+playerName;
        addLogEvent(game,eventString,intThrowaway,playerName,teamName);
    }

    public void addDrop(String playerName, String teamName, Games game, String timeString){
        int playerID = getPlayerID(playerName,teamName);
        String updateDrop =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_DROPS+" = " +
                        COLUMN_PERFORMANCES_DROPS+" + 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateDrop);
        db.close();
        String eventString = timeString+" :: Drop by "+playerName;
        addLogEvent(game, eventString,intDrop,playerName,teamName);
    }

    public void addOpponentScore(Games game, String timeString){
        String updateOpponentScore =
                "UPDATE "+TABLE_GAMES+" SET "+COLUMN_GAMES_OPPONENTSCORE+" = " +
                        COLUMN_GAMES_OPPONENTSCORE+" + 1 WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateOpponentScore);
        db.close();
        String eventString = timeString+" :: Scored On by "+game.get_opponent();
        addLogEvent(game,eventString,intScoredOn,"","");
    }

    public String databasePerformancesToString(Games game){
//        int playerID = getPlayerID(playerName,teamName);
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PERFORMANCES + " Where " + COLUMN_PERFORMANCES_GAMEID+" = '"+game.get_id()+"'";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_GAMEID))!= null){
                dbString += c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_PLAYERNAME));
                dbString += ": Score = " + c.getInt(c.getColumnIndex(COLUMN_PERFORMANCES_SCORES));
//                dbString += " || Assist = "+ c.getInt(c.getColumnIndex(COLUMN_PERFORMANCES_ASSISTS));
                dbString += "\n";
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

    public void addLogEvent(Games game, String eventString, int eventType, String playerName, String teamName){

        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGEVENTS_GAMEID, game.get_id());
        values.put(COLUMN_LOGEVENTS_EVENT_STRING, eventString);
        values.put(COLUMN_LOGEVENTS_EVENT_TYPE, eventType);
        if (playerName != "") {
            int playerID = getPlayerID(playerName, teamName);
            values.put(COLUMN_LOGEVENTS_PLAYERID, playerID);
        }
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_LOGEVENTS, null, values);
        db.close();
    }

    public String databaseEventLogToString(Games game){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_LOGEVENTS+ " WHERE "+COLUMN_LOGEVENTS_GAMEID+" = "+game.get_id()+" AND "+COLUMN_LOGEVENTS_EVENT_VALIDITY+" = 0";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_LOGEVENTS_LOGID))!= null){
                dbString += c.getString(c.getColumnIndex(COLUMN_LOGEVENTS_EVENT_STRING));
                if(!c.isLast()) {
                    dbString += "\n";
                }
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        return dbString;
    }

    public String getLastEventLog(Games game){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_LOGEVENTS+ " WHERE "+COLUMN_LOGEVENTS_GAMEID+" = "+game.get_id()+" AND "+COLUMN_LOGEVENTS_EVENT_VALIDITY+" = 0" +
                " ORDER BY "+COLUMN_LOGEVENTS_LOGID+" DESC";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if(c.getCount()>0){
            dbString = c.getString(c.getColumnIndex(COLUMN_LOGEVENTS_EVENT_STRING));
        }
        c.close();
        db.close();
        return dbString;
    }

    public String databaseHomeScoreToString(Games game){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_GAMES+ " WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        dbString = c.getString(c.getColumnIndex(COLUMN_GAMES_TEAMSCORE));
        c.close();
        db.close();
        return dbString;
    }

    public String databaseOpponentScoreToString(Games game){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_GAMES+ " WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        dbString = c.getString(c.getColumnIndex(COLUMN_GAMES_OPPONENTSCORE));
        c.close();
        db.close();
        return dbString;
    }

    public void undoLastEvent(Games game){
        int eventType;
        int eventlogID;
        int playerID;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_LOGEVENTS+ " WHERE "+COLUMN_LOGEVENTS_GAMEID+" = "+game.get_id()+" AND "+COLUMN_LOGEVENTS_EVENT_VALIDITY+" = 0" +
                " ORDER BY "+COLUMN_LOGEVENTS_LOGID+" DESC";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        eventlogID= c.getInt(c.getColumnIndex(COLUMN_LOGEVENTS_LOGID));
        eventType = c.getInt(c.getColumnIndex(COLUMN_LOGEVENTS_EVENT_TYPE));
        playerID = c.getInt(c.getColumnIndex(COLUMN_LOGEVENTS_PLAYERID));
        c.close();
        db.close();

        switch (eventType){
            case intScore: subtractScore(playerID, game);
                break;
            case intAssist: subtractAssist(playerID, game);
                break;
            case intD: subtractD(playerID, game);
                break;
            case intScoredOn: subtractScoredOn(game);
                break;
            case intThrowaway: subtractThrowaway(playerID, game);
                break;
            case intDrop: subtractDrop(playerID, game);
                break;
            default: break;
        }

        query = //Change the validity to 1 (the action has been undone)
                "UPDATE "+TABLE_LOGEVENTS+" SET "+COLUMN_LOGEVENTS_EVENT_VALIDITY+" = 1" +
                        " WHERE "+COLUMN_LOGEVENTS_LOGID+" = "+eventlogID;
        db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void subtractScore(int playerID, Games game){
        String updateScore =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_SCORES+" = " +
                        COLUMN_PERFORMANCES_SCORES+" - 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateScore);
        updateScore =
                "UPDATE "+TABLE_GAMES+" SET "+COLUMN_GAMES_TEAMSCORE+" = " +
                        COLUMN_GAMES_TEAMSCORE+" - 1 WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        db.execSQL(updateScore);
        db.close();
    }

    public void subtractAssist(int playerID, Games game){
        String updateAssist =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_ASSISTS+" = " +
                        COLUMN_PERFORMANCES_ASSISTS+" - 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateAssist);
        db.close();
    }

    public void subtractD(int playerID, Games game){
        String updateDs =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_Ds+" = " +
                        COLUMN_PERFORMANCES_Ds+" - 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateDs);
        db.close();
    }

    public void subtractScoredOn(Games game){
        String updateOpponentScore =
                "UPDATE "+TABLE_GAMES+" SET "+COLUMN_GAMES_OPPONENTSCORE+" = " +
                        COLUMN_GAMES_OPPONENTSCORE+" - 1 WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateOpponentScore);
        db.close();
    }

    public void subtractThrowaway(int playerID, Games game){
        String updateThrowaway =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_THROWAWAYS+" = " +
                        COLUMN_PERFORMANCES_THROWAWAYS+" - 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateThrowaway);
        db.close();
    }

    public void subtractDrop(int playerID, Games game){
        String updateDrop =
                "UPDATE "+TABLE_PERFORMANCES+" SET "+COLUMN_PERFORMANCES_DROPS+" = " +
                        COLUMN_PERFORMANCES_DROPS+" + 1 WHERE "+COLUMN_PERFORMANCES_PLAYERID+" = "+playerID+
                        " AND "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        //select the player id that matches the player name and team name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateDrop);
        db.close();
    }

    public void setTeamName(Games game, String teamName){
        String updateTeamName =
                "UPDATE "+TABLE_GAMES+" SET "+COLUMN_GAMES_TEAMNAME+" = '" +
                        teamName+"' WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        game.set_teamname(teamName);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateTeamName);
        db.close();
    }

    public void setOpponentName(Games game, String opponentName){
        String updateOpponentName =
                "UPDATE "+TABLE_GAMES+" SET "+COLUMN_GAMES_OPPONENT+" = '" +
                       opponentName +"' WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        game.set_opponent(opponentName);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateOpponentName);
        db.close();
    }

    public void setGameName(Games game, String gameName){
        String updateGameName=
                "UPDATE "+TABLE_GAMES+" SET "+COLUMN_GAMES_GAMENAME+" = '" +
                        gameName +"' WHERE "+COLUMN_GAMES_ID+" = "+game.get_id();
        game.set_gamename(gameName);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(updateGameName);
        db.close();
    }

    public Games getGame(Games game, int gameID){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_GAMES+ " WHERE "+COLUMN_GAMES_ID+" = "+gameID;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        game.set_id(gameID);
        game.set_teamname(c.getString(c.getColumnIndex(COLUMN_GAMES_TEAMNAME)));
        game.set_gamename(c.getString(c.getColumnIndex(COLUMN_GAMES_GAMENAME)));
        game.set_opponent(c.getString(c.getColumnIndex(COLUMN_GAMES_OPPONENT)));
        game.set_teamscore(c.getInt(c.getColumnIndex(COLUMN_GAMES_TEAMSCORE)));
        game.set_opponentscore(c.getInt(c.getColumnIndex(COLUMN_GAMES_OPPONENTSCORE)));
        c.close();
        db.close();
        return game;
    }

    public ArrayList getPerformancesFromGame(Games game){
        ArrayList<String[]> performancesList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_PERFORMANCES+ " WHERE "+COLUMN_PERFORMANCES_GAMEID+" = "+game.get_id();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        String printString = "";
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_PLAYERID))!= null){
                String[] row = new String[6];
                row[0] = c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_PLAYERNAME));
                row[1] = c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_SCORES));
                row[2] = c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_ASSISTS));
                row[3] = c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_Ds));
                row[4] = c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_THROWAWAYS));
                row[5] = c.getString(c.getColumnIndex(COLUMN_PERFORMANCES_DROPS));
                performancesList.add(row);
                printString += row[0];
                printString += "\n";
            }
            c.moveToNext();
        }

        c.close();
        db.close();
        Log.i("TableRows", printString);
        return performancesList;
    }

}

