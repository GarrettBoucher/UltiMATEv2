package com.example.garrett.ultimatev2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameHubActivity extends AppCompatActivity {

    Chronometer chronometer;
    long timeWhenStopped;
    ArrayList<String> playerString;
    ArrayList<String> teamString;
    DBHandler dbHandler = new DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        final Globals globalVariable = (Globals) getApplicationContext();

        if (isLargeDevice(getBaseContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(globalVariable.getStartTime()!=0){//resume the chronometer when activity restarted
            chronometer = (Chronometer) findViewById(R.id.chronometer);
            chronometer.setBase(globalVariable.getStartTime());
            chronometer.start();
        }
        if(globalVariable.getCurrentGame()==null){//if there is not a game going on, make one
            preGameLogic();
        }
        if(globalVariable.isParametersDefined()){
            startDialog();
            globalVariable.setParametersDefined(false);
        }
        toolbar.setTitle(globalVariable.getCurrentGame().get_gamename());
        TextView homeTeam = (TextView)findViewById(R.id.textViewHomeTeam);
        homeTeam.setText(globalVariable.getCurrentTeamName());
        TextView awayTeam = (TextView)findViewById(R.id.textViewOpponent);
        awayTeam.setText(globalVariable.getCurrentGame().get_opponent());
        printDatabaseLogEvents();
        printScoreDisplay();

        Button buttonDrop =      (Button) findViewById(R.id.buttonDrop);
        Button buttonScore =     (Button) findViewById(R.id.buttonScore);
        Button buttonAssist =    (Button) findViewById(R.id.buttonAssist);
        Button buttonThrowaway = (Button) findViewById(R.id.buttonThrowaway);
        Button buttonD =         (Button) findViewById(R.id.buttonD);
        Button buttonScoredOn =  (Button) findViewById(R.id.buttonOpponentScore);
        Button buttonUndo =      (Button) findViewById(R.id.buttonUndo);

//        Button buttonStartPause = (Button) findViewById(R.id.buttonStartPause);
//        buttonStartPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!globalVariable.isClockTicking()) {
//                    globalVariable.setClockTicking(true);
//                    chronometer.setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
//                    chronometer.start();
//                }else{
//                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
//                    chronometer.stop();
//                    globalVariable.setClockTicking(false);
//                }
//            }
//        });

//        Button buttonReset = (Button) findViewById(R.id.buttonReset);
//        buttonReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chronometer.setBase(SystemClock.elapsedRealtime());
//                timeWhenStopped = 0;
//            }
//        });



        dropButtonListener(buttonDrop);
        scoreButtonListener(buttonScore);
        assistButtonListener(buttonAssist);
        throwawayButtonListener(buttonThrowaway);
        dbuttonListener(buttonD);
        scoredOnButtonListener(buttonScoredOn);
        undoButtonListener(buttonUndo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.finishGame:
                postGameLogic();
                return true;
            case R.id.statSheet:
                goToStatSheet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void scoreButtonListener(Button buttonScore){
        final Globals globalVariable = (Globals) getApplicationContext();
        buttonScore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_player_list, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
                alertDialogBuilder.setView(promptView);

                TextView title = (TextView) promptView.findViewById(R.id.textView2);
                title.setText("Score: ");

                // create an alert dialog
                final AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                final ListView listView = (ListView) promptView.findViewById(R.id.listViewPlayers);
                playerString = dbHandler.getPlayersArrayList(globalVariable.getCurrentTeamName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplest_list_item_1, playerString);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedPlayer = listView.getItemAtPosition(position).toString();
                        globalVariable.setSelectedPlayer(selectedPlayer);
//                        Toast.makeText(getApplicationContext(),"selectedPlayer = " +selectedPlayer,Toast.LENGTH_LONG).show();
                        dbHandler.addScore(selectedPlayer, globalVariable.getCurrentTeamName(),globalVariable.getCurrentGame(),(String)chronometer.getText());
                        alert.dismiss();
                        printDatabaseLogEvents();
                        //increment the teamScore by 1
                        globalVariable.getCurrentGame().set_teamscore(globalVariable.getCurrentGame().get_teamscore()+1);
                        //print the updated score in the display
                        printScoreDisplay();
                    }
                });
            }
        });
    }

    public void dropButtonListener(Button buttonDrop){
        final Globals globalVariable = (Globals) getApplicationContext();
        buttonDrop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_player_list, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
                alertDialogBuilder.setView(promptView);

                TextView title = (TextView) promptView.findViewById(R.id.textView2);
                title.setText("Drop: ");

                // create an alert dialog
                final AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                final ListView listView = (ListView) promptView.findViewById(R.id.listViewPlayers);
                playerString = dbHandler.getPlayersArrayList(globalVariable.getCurrentTeamName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplest_list_item_1, playerString);
                listView.setAdapter(arrayAdapter);
//                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedPlayer = listView.getItemAtPosition(position).toString();
                        globalVariable.setSelectedPlayer(selectedPlayer);
//                        Toast.makeText(getApplicationContext(),"selectedPlayer = " +selectedPlayer,Toast.LENGTH_LONG).show();
                        dbHandler.addDrop(selectedPlayer,globalVariable.getCurrentTeamName(),globalVariable.getCurrentGame(),(String)chronometer.getText());
                        alert.dismiss();
                        printDatabaseLogEvents();
                    }
                });
            }
        });
    }

    public void assistButtonListener(Button buttonAssist){
        final Globals globalVariable = (Globals) getApplicationContext();
        buttonAssist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_player_list, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
                alertDialogBuilder.setView(promptView);

                TextView title = (TextView) promptView.findViewById(R.id.textView2);
                title.setText("Assist: ");

                // create an alert dialog
                final AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                final ListView listView = (ListView) promptView.findViewById(R.id.listViewPlayers);
                playerString = dbHandler.getPlayersArrayList(globalVariable.getCurrentTeamName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplest_list_item_1, playerString);
                listView.setAdapter(arrayAdapter);
//                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedPlayer = listView.getItemAtPosition(position).toString();
                        globalVariable.setSelectedPlayer(selectedPlayer);
//                        Toast.makeText(getApplicationContext(),"selectedPlayer = " +selectedPlayer,Toast.LENGTH_LONG).show();
                        dbHandler.addAssist(selectedPlayer,globalVariable.getCurrentTeamName(),globalVariable.getCurrentGame(),(String)chronometer.getText());
                        alert.dismiss();
                        printDatabaseLogEvents();
                    }
                });
            }
        });
    }

    public void throwawayButtonListener(Button buttonThrowaway){
        final Globals globalVariable = (Globals) getApplicationContext();
        buttonThrowaway.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_player_list, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
                alertDialogBuilder.setView(promptView);

                TextView title = (TextView) promptView.findViewById(R.id.textView2);
                title.setText("Throwaway: ");

                // create an alert dialog
                final AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                final ListView listView = (ListView) promptView.findViewById(R.id.listViewPlayers);
                playerString = dbHandler.getPlayersArrayList(globalVariable.getCurrentTeamName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplest_list_item_1, playerString);
                listView.setAdapter(arrayAdapter);
//                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedPlayer = listView.getItemAtPosition(position).toString();
                        globalVariable.setSelectedPlayer(selectedPlayer);
//                        Toast.makeText(getApplicationContext(),"selectedPlayer = " +selectedPlayer,Toast.LENGTH_LONG).show();
                        dbHandler.addThrowaway(selectedPlayer,globalVariable.getCurrentTeamName(),globalVariable.getCurrentGame(),(String)chronometer.getText());
                        alert.dismiss();
                        printDatabaseLogEvents();
                    }
                });
            }
        });
    }

    public void dbuttonListener(Button buttonD){
        final Globals globalVariable = (Globals) getApplicationContext();
        buttonD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_player_list, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
                alertDialogBuilder.setView(promptView);

                TextView title = (TextView) promptView.findViewById(R.id.textView2);
                title.setText("'D' : ");

                // create an alert dialog
                final AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                final ListView listView = (ListView) promptView.findViewById(R.id.listViewPlayers);
                playerString = dbHandler.getPlayersArrayList(globalVariable.getCurrentTeamName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplest_list_item_1, playerString);
                listView.setAdapter(arrayAdapter);
//                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedPlayer = listView.getItemAtPosition(position).toString();
                        globalVariable.setSelectedPlayer(selectedPlayer);
//                        Toast.makeText(getApplicationContext(),"selectedPlayer = " +selectedPlayer,Toast.LENGTH_LONG).show();
                        dbHandler.addD(selectedPlayer,globalVariable.getCurrentTeamName(),globalVariable.getCurrentGame(),(String)chronometer.getText());
                        alert.dismiss();
                        printDatabaseLogEvents();
                    }
                });
            }
        });
    }

    public void scoredOnButtonListener(Button buttonScoredOn){
        final Globals globalVariable = (Globals) getApplicationContext();
        buttonScoredOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_confirm_scored_on, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
                alertDialogBuilder.setView(promptView);
                TextView textConfirm = (TextView)promptView.findViewById(R.id.textViewConfirmScoredOn);
                textConfirm.setText(globalVariable.getCurrentGame().get_opponent()+" scored?");
                // setup a dialog window
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                globalVariable.getCurrentGame().set_opponentscore(globalVariable.getCurrentGame().get_opponentscore() + 1);
                                dbHandler.addOpponentScore(globalVariable.getCurrentGame(), (String) chronometer.getText());
                                printDatabaseLogEvents();
                                printScoreDisplay();
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create an alert dialog
                final AlertDialog alert = alertDialogBuilder.create();
                alert.show();

            }
        });
    }

    public void undoButtonListener(Button buttonUndo){
        final Globals globalVariable = (Globals) getApplicationContext();
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_confirm_scored_on, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
                alertDialogBuilder.setView(promptView);

                String lastEvent = dbHandler.getLastEventLog(globalVariable.getCurrentGame());

                TextView textConfirm = (TextView)promptView.findViewById(R.id.textViewConfirmScoredOn);
                textConfirm.setText("Undo previous action: \n\n\""+lastEvent+"\" ?");

                if(lastEvent!=""){//if there is a previous event, build the dialog
                    // setup a dialog window
//                    Toast.makeText(getApplicationContext(),"Undoable actions exist",Toast.LENGTH_LONG).show();
                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Connect to Database and reverse action
                                    dbHandler.undoLastEvent(globalVariable.getCurrentGame());
                                    printDatabaseLogEvents();
                                    printScoreDisplay();
                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    // create an alert dialog
                    final AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }else{//toast that there are no events to undo
                    Toast.makeText(getApplicationContext(),"There are no actions to undo",Toast.LENGTH_LONG).show();
                }


            }
        });
    }


    private boolean isLargeDevice(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return false;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return true;
            default:
                return false;
        }
    }

    public void preGameLogic(){
        final Globals globalVariable = (Globals) getApplicationContext();
        //create new game and set it
        Games game = new Games();
        game = dbHandler.addGame(game);
        globalVariable.setCurrentGame(game);

        chooseTeam();//ask for team to play with
//        chooseOpponent();//ask for opponent name
//        chooseGameName();//ask for game name



    }

    public void printDatabaseLogEvents(){
        final Globals globalVariable = (Globals) getApplicationContext();
        TextView outputText = (TextView)findViewById(R.id.textViewGameLog);
        String dbString = dbHandler.databaseEventLogToString(globalVariable.getCurrentGame());
        outputText.setText(dbString);

        outputText.setMovementMethod(new ScrollingMovementMethod());
        ((ScrollView) findViewById(R.id.scrollView)).post(new Runnable()
        {
            public void run()
            {
                ((ScrollView) findViewById(R.id.scrollView)).fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void printScoreDisplay(){
        final Globals globalVariable = (Globals) getApplicationContext();
        TextView teamScore = (TextView)findViewById(R.id.textViewHomeScore);
        String dbString = dbHandler.databaseHomeScoreToString(globalVariable.getCurrentGame());
        teamScore.setText(dbString);

        TextView opponentScore = (TextView)findViewById(R.id.textViewOpponentScore);
        String dbString2 = dbHandler.databaseOpponentScoreToString(globalVariable.getCurrentGame());
        opponentScore.setText(dbString2);
    }

    public void chooseTeam(){
        final Globals globalVariable = (Globals) getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_team_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);

        TextView title = (TextView) promptView.findViewById(R.id.textViewTitle);
        title.setText("Your Team: ");


        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        final ListView listView = (ListView) promptView.findViewById(R.id.listViewTeams);
        teamString = dbHandler.getTeamsArrayList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplest_list_item_1, teamString);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalVariable.setCurrentTeamName(listView.getItemAtPosition(position).toString());
                dbHandler.setTeamName(globalVariable.getCurrentGame(), globalVariable.getCurrentTeamName());
                alert.dismiss();
                chooseOpponent();
            }
        });
    }

    public void chooseOpponent(){
        final Globals globalVariable = (Globals) getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);


        TextView title = (TextView) promptView.findViewById(R.id.textPrompt);
        title.setText("Opponent Name: ");
        final EditText input = (EditText) promptView.findViewById(R.id.editTextInput);
        input.setHint("Opponent Name");

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String inputOpponentName = input.getText().toString();
                if(inputOpponentName.contains("'")||inputOpponentName.contains("\"")){
                    Toast.makeText(getApplicationContext(),"Please input a name without special characters",Toast.LENGTH_LONG).show();
                    chooseOpponent();
                }else {
                    dbHandler.setOpponentName(globalVariable.getCurrentGame(), inputOpponentName);
                    chooseGameName();
                }
            }
        });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void chooseGameName(){
        final Globals globalVariable = (Globals) getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);


        TextView title = (TextView) promptView.findViewById(R.id.textPrompt);
        title.setText("Enter a title for this game: ");
        final EditText input = (EditText) promptView.findViewById(R.id.editTextInput);
        input.setHint("Game Title");


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String inputGameTitle = input.getText().toString();
                        Log.i("inputGameTitle", inputGameTitle);
                        if(inputGameTitle.contains("'")||inputGameTitle.contains("\"")){
                            Toast.makeText(getApplicationContext(),"Please input a name without special characters",Toast.LENGTH_LONG).show();
                            chooseGameName();
                        }else {
                            if(inputGameTitle==""){
                                inputGameTitle=globalVariable.getCurrentGame().get_opponent();
                                Log.i("inputGameTitle", inputGameTitle);
                            }
                            Log.i("inputGameTitle", inputGameTitle);
                            dbHandler.setGameName(globalVariable.getCurrentGame(), inputGameTitle);

                            //create performances
                            playerString = dbHandler.getPlayersArrayList(globalVariable.getCurrentTeamName());
                            for (int i = 0; i < playerString.size(); i++) {//add a performance for each player on the team
                                dbHandler.addPerformance(playerString.get(i),globalVariable.getCurrentTeamName(),globalVariable.getCurrentGame());
                            }

                            globalVariable.setParametersDefined(true);
                            //refresh the view
                            Intent intent = getIntent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);

                        }

                    }
                });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void startDialog(){
        final Globals globalVariable = (Globals) getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_start, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);


        TextView title = (TextView) promptView.findViewById(R.id.textPrompt2);
        title.setText("Ready?\n The clock will begin when you press Start: ");


        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        Button buttonStart = (Button) promptView.findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                timeWhenStopped = 0;
                globalVariable.setStartTime(SystemClock.elapsedRealtime());
                chronometer = (Chronometer) findViewById(R.id.chronometer);
                chronometer.setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
                chronometer.start();
            }
        });


    }

    public void postGameLogic(){
        final Globals globalVariable = (Globals) getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(GameHubActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_finish_game, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameHubActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView title = (TextView)promptView.findViewById(R.id.textPromptFinish);
        title.setText("Are you sure you want to finish the game?\nStats cannot be edited once the game is finished.");

                // setup a dialog window
                alertDialogBuilder.setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
              final  AlertDialog alert = alertDialogBuilder.create();
                alert.show();

        Button buttonFinish = (Button) promptView.findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalVariable.setCurrentGame(null);
                globalVariable.setCurrentTeamName(null);
                globalVariable.setSelectedPlayer(null);
                globalVariable.setClockTicking(false);
                globalVariable.setParametersDefined(false);
                globalVariable.setStartTime(0);
                alert.dismiss();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    public void goToStatSheet(){
        Globals globalVariable = (Globals) getApplicationContext();
        globalVariable.setViewGame(globalVariable.getCurrentGame());
        globalVariable.setViewTeamName(globalVariable.getCurrentTeamName());
        globalVariable.setViewParametersDefined(true);

        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
    }

}
