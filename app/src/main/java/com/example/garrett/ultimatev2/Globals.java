package com.example.garrett.ultimatev2;

import android.app.Application;
import android.widget.Chronometer;

/**
 * Created by Garrett on 4/23/16.
 */
public class Globals extends Application {
    private String currentTeamName;
    private String opponentName;
    private String gameTitle;
    private boolean clockTicking = false;
    private String selectedPlayer;
    private Games currentGame = null;
    private boolean parametersDefined = false;
    private long startTime = 0;
    //Viewing Globals
    private String viewTeamName;
    private Games viewGame = null;
    private boolean viewParametersDefined = false;
    private String title;

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isViewParametersDefined() {
        return viewParametersDefined;
    }

    public void setViewParametersDefined(boolean viewParametersDefined) {
        this.viewParametersDefined = viewParametersDefined;
    }

    public String getViewTeamName() {
        return viewTeamName;
    }

    public void setViewTeamName(String viewTeamName) {
        this.viewTeamName = viewTeamName;
    }

    public Games getViewGame() {
        return viewGame;
    }

    public void setViewGame(Games viewGame) {
        this.viewGame = viewGame;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getCurrentTeamName(){
        return currentTeamName;
    }

    public void setCurrentTeamName(String teamName){
        currentTeamName = teamName;
    }

    public boolean isClockTicking(){
        return clockTicking;
    }

    public void setClockTicking(Boolean bool){
        clockTicking = bool;
    }

    public String getSelectedPlayer(){
        return selectedPlayer;
    }

    public void setSelectedPlayer(String playerName){
        selectedPlayer = playerName;
    }

    public Games getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Games currentGame) {
        this.currentGame = currentGame;
    }

    public boolean isParametersDefined() {
        return parametersDefined;
    }

    public void setParametersDefined(boolean parametersSet) {
        this.parametersDefined = parametersSet;
    }
    //    public boolean getFragmentPopulated(){
//        return fragmentPopulated;
//    }
//     public void setFragmentPopulated(Boolean bool){
//         fragmentPopulated = bool;
//     }

}
