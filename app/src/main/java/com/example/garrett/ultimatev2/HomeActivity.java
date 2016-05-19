package com.example.garrett.ultimatev2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    EditText myInput;
    TextView myText;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Globals globalVariable = (Globals) getApplicationContext();
        globalVariable.setViewTeamName(null);
        globalVariable.setViewGame(null);
        globalVariable.setViewParametersDefined(false);

        if (isLargeDevice(getBaseContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
//        myInput = (EditText) findViewById(R.id.editText);
//        myText = (TextView) findViewById(R.id.textView);
        dbHandler = new DBHandler(this, null, null, 1);
//        printDatabaseTeams();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
    }

    @Override
    public void onResume(){
        super.onResume();
        Globals globalVariable = (Globals) getApplicationContext();
        globalVariable.setViewTeamName(null);
        globalVariable.setViewGame(null);
        globalVariable.setViewParametersDefined(false);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_game_hub, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.finishGame) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //Called when the user clicks the 'Enter/Edit A Team' button
    public void goToTeamList(View view){
        Intent intent = new Intent(this, TeamListActivity.class);
        startActivity(intent);
    }

    //Called when the user clicks the game button
    public void goToGame(View view){
        Intent intent = new Intent(this, GameHubActivity.class);
        startActivity(intent);
    }

    //called when the user clicks the view stats button
    public void goToStats(View view){
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    public void printDatabaseTeams(){
        String dbString = dbHandler.databaseTeamsToString();
        myText.setText(dbString);
        myInput.setText("");
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

    public void addButtonClicked(View view){
        Teams team = new Teams(myInput.getText().toString());
        dbHandler.addTeam(team);
        printDatabaseTeams();
    }

    public void deleteButtonClicked(View view){
        String inputText = myInput.getText().toString();
        dbHandler.deleteTeam(inputText);
        printDatabaseTeams();
    }


}
