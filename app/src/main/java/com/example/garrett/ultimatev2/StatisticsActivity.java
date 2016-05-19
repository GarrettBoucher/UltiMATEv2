package com.example.garrett.ultimatev2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class StatisticsActivity extends AppCompatActivity {

    DBHandler dbHandler = new DBHandler(this, null, null, 1);
    ArrayList<String> teamString;
    ArrayList<String> gameString;
    ArrayList<Integer> gamePosition;
    ArrayList<String[]> performancesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Globals globalVariable = (Globals) getApplicationContext();
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(globalVariable.getTitle());
        setSupportActionBar(toolbar);

        TextView homeTeam = (TextView)findViewById(R.id.textViewHomeTeam2);
        TextView awayTeam = (TextView)findViewById(R.id.textViewOpponent2);
        TextView homeScore = (TextView)findViewById(R.id.textViewHomeScore2);
        TextView awayScore = (TextView)findViewById(R.id.textViewOpponentScore2);


//        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView2);
//        ScrollView scrollView = new ScrollView(this);
//        HorizontalScrollView horoScrollView = new HorizontalScrollView(this);

        if(!globalVariable.isViewParametersDefined()){
            chooseTeam();
        }else{
            TableLayout tableLayout = (TableLayout)findViewById(R.id.table);
            buildTable(globalVariable.getViewGame(), tableLayout);

            homeTeam.setText(globalVariable.getViewGame().get_teamname());
            awayTeam.setText(globalVariable.getViewGame().get_opponent());
            String opponentScore = String.valueOf(globalVariable.getViewGame().get_opponentscore());
            awayScore.setText(opponentScore);
            String teamScore = String.valueOf(globalVariable.getViewGame().get_teamscore());
            homeScore.setText(teamScore);


//            TableLayout tableLayout = buildTable(globalVariable.getViewGame());
//            horoScrollView.addView(tableLayout);
//            scrollView.addView(horoScrollView);
//
//            setContentView(scrollView);
        }





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Globals globalVariable = (Globals) getApplicationContext();
        if(globalVariable.getCurrentGame()!=null){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_statistics, menu);
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.returnToGame:
                returnToGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void chooseTeam(){
        final Globals globalVariable = (Globals) getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(StatisticsActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_team_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StatisticsActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);

        TextView title = (TextView) promptView.findViewById(R.id.textViewTitle);
        title.setText("Choose Team: ");


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
                globalVariable.setViewTeamName(listView.getItemAtPosition(position).toString());
                alert.dismiss();
                chooseGame();
            }
        });
    }

    public void chooseGame(){
        final Globals globalVariable = (Globals) getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(StatisticsActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_team_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StatisticsActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);

        final TextView title = (TextView) promptView.findViewById(R.id.textViewTitle);
        title.setText("Choose Game: ");


        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        final ListView listView = (ListView) promptView.findViewById(R.id.listViewTeams);
        gameString = dbHandler.getGamesArrayList(globalVariable.getViewTeamName());
        gamePosition = dbHandler.getGamesIDArrayList(globalVariable.getViewTeamName());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simplest_list_item_1, gameString);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int gameID;
                gameID = gamePosition.get(position);
                Games game = new Games();
                game = dbHandler.getGame(game, gameID);
                globalVariable.setViewGame(game);

//                globalVariable.setViewGameString(listView.getItemAtPosition(position).toString());
                alert.dismiss();
                globalVariable.setTitle(globalVariable.getViewGame().get_gamename());

                globalVariable.setViewParametersDefined(true);
                Log.i("GamesTest", globalVariable.getViewGame().get_gamename());
//                //refresh the view
                Log.i("Refresh", "Activity was refreshed");
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });
    }

    public void buildTable(Games game, TableLayout table){
        Log.i("Method:", "buildTable started");
        performancesList = dbHandler.getPerformancesFromGame(game);

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        table.setBackgroundColor(Color.WHITE);

        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(3, 1, 3, 1);
        tableRowParams.weight = 1;

        String[] columnHeaders = {"Player Name", "Scores","Assists","D's","Throwaways","Drops" };

        for (int i = 0; i < performancesList.size()+1; i++) {
            //create tableRow
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.WHITE);

            for (int j = 0; j < 6; j++) {
                //create textView
                TextView textView = new TextView(this);

//                if(i%2==0){//if even
//                    textView.setBackgroundColor(Color.LTGRAY);
//                    textView.setTextColor(Color.BLACK);
//                }else{
//                    textView.setTextColor(Color.WHITE);
//                    textView.setBackgroundColor(Color.DKGRAY);
//                }
                textView.setGravity(Gravity.CENTER);


                int id = j + (i*6);
                Log.d("TAG", "-___>" + id);
                if (i == 0) {
                    Log.d("TAG", "set Column Headers");
                    textView.setText(columnHeaders[j]);
                    Log.i("Column Header", columnHeaders[j]);
                    textView.setTextSize(22);
                } else {
                    Log.d("TAG", "Set Row");
                    textView.setText(performancesList.get(i-1)[j]);
                    Log.i("Row Data", performancesList.get(i-1)[j]);
                    textView.setTextSize(22);


                }
                textView.setMinWidth(300);
                textView.setBackgroundColor(Color.BLACK);
                textView.setPadding(10,1,10,1);
                //add textView to tableRow
                tableRow.addView(textView, tableRowParams);

            }
            //add tableRow to TableLayout
            table.addView(tableRow, tableLayoutParams);
        }


    }

    public void returnToGame(){
        Globals globalVariable = (Globals) getApplicationContext();
        globalVariable.setViewTeamName(null);
        globalVariable.setViewGame(null);
        globalVariable.setViewParametersDefined(false);
        Intent intent = new Intent(this, GameHubActivity.class);
        startActivity(intent);
    }
}
