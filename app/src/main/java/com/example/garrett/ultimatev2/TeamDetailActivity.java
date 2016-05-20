package com.example.garrett.ultimatev2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * An activity representing a single Team detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TeamListActivity}.
 */
public class TeamDetailActivity extends AppCompatActivity {

    private String inputPlayerName;
    private DBHandler dbHandler;
    ListView myList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final Globals globalVariable = (Globals) getApplicationContext();
        dbHandler = new DBHandler(TeamDetailActivity.this, null, null, 1);

//        myList = (ListView) findViewById(R.id.playerListView);
//        ArrayList<String> playerString = dbHandler.getPlayersArrayList(globalVariable.getCurrentTeamName());
//        toastArrayList(playerString);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerString);
//        myList.setAdapter(arrayAdapter);
//        Toast.makeText(TeamDetailActivity.this,"Players:\n"+dbHandler.getPlayersAsString(), Toast.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(TeamDetailActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_add_new_player, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TeamDetailActivity.this);
                alertDialogBuilder.setView(promptView);

                final EditText editText = (EditText) promptView.findViewById(R.id.editText2);
                // setup a dialog window
                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                inputPlayerName = editText.getText().toString();

                                if(!dbHandler.playerExists(inputPlayerName, globalVariable.getCurrentTeamName())){
                                    //add player to Database
                                    Players player = new Players(inputPlayerName, globalVariable.getCurrentTeamName());
                                    dbHandler.addPlayer(player);
                                    dbHandler.putTeamsInList();
                                    Toast.makeText(TeamDetailActivity.this,inputPlayerName+" successfully added", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(TeamDetailActivity.this,inputPlayerName+" already exists on this team", Toast.LENGTH_LONG).show();
                                }





//                                Toast.makeText(TeamDetailActivity.this,teamName, Toast.LENGTH_LONG).show();


//                                    Toast.makeText(TeamDetailActivity.this,inputPlayerName+" successfully added!", Toast.LENGTH_LONG).show();

                                    //add to master/detail flow list
                                   // dbHandler.putTeamsInList();



//                                //refresh the view
                                Intent intent = getIntent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(TeamDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(TeamDetailFragment.ARG_ITEM_ID));
            TeamDetailFragment fragment = new TeamDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.team_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, TeamListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void toastArrayList(ArrayList arrayList){
//        String arrayString = "";
//        for (int i = 0; i < 10; i++) {
//            arrayString += arrayList.get(i).toString();
//            arrayString += "\n";
//        }
//        Toast.makeText(this, arrayString, Toast.LENGTH_SHORT).show();
//    }

//    public void addNewTeam(View view){
//        Toast.makeText(TeamDetailActivity.this, "Feature Coming Soon!", Toast.LENGTH_LONG).show();
//
//    }
}
