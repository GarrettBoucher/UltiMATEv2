package com.example.garrett.ultimatev2.MasterDetailContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class TeamContent  {

    /**
     * An array of sample (dummy) items.
     */
    public static List<TeamItem> ITEMS = new ArrayList<TeamItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, TeamItem> ITEM_MAP = new HashMap<String, TeamItem>();

    private static final int COUNT = 25;

    static {
//
//        addItem(new TeamItem("1", "Create Team", "Create Team Details go HERE"));
//        addItem(new TeamItem("2", "Another Team", "Create Team Details go HERE"));

//        SQLiteDatabase db = dbHandler.getWritableDatabase();
//        String query = "SELECT * FROM " + dbHandler.TABLE_TEAMS + " Where 1";
//        Cursor c = db.rawQuery(query, null);
//        if (c != null){
//            if (c.moveToFirst()){
//                do{
//                    addItem(new TeamItem(dbHandler.COLUMN_ID,dbHandler.COLUMN_TEAMS_TEAMNAME, "Team Details" ));
//                }while (c.moveToNext());
//            }
//        }


    //loop through rows in Database
        //addItem(new TeamItem(String.valueOf(


    }

    public static void addItem(TeamItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

//    private static TeamItem createDummyItem(int position) {
//        return new TeamItem(String.valueOf(position), "Item " + position, makeDetails(position));
//    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class TeamItem {
//        DBHandler dbHandler;

        public final String id;
        public final String teamName;
//        public final String details;
//        public String playerList;

        public TeamItem(String id, String teamName) {
//            dbHandler = new DBHandler(,null,null,1);
            this.id = id;
            this.teamName = teamName;
//            this.details = details;
//            this.playerList = dbHandler.getPlayersAsString(teamName);


        }

        @Override
        public String toString() {
            return teamName;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
