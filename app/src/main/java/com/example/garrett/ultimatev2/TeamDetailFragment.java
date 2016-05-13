package com.example.garrett.ultimatev2;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garrett.ultimatev2.dummy.TeamContent;

import java.util.ArrayList;

/**
 * A fragment representing a single Team detail screen.
 * This fragment is either contained in a {@link TeamListActivity}
 * in two-pane mode (on tablets) or a {@link TeamDetailActivity}
 * on handsets.
 */
public class TeamDetailFragment extends Fragment{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private TeamContent.TeamItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamDetailFragment() {
    }

    DBHandler dbHandler;
    TextView myText;
    ListView myList;
    ArrayList<String> playerString;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Globals globalVariable = (Globals) getActivity().getApplicationContext();
        dbHandler = new DBHandler(getContext(), null, null, 1);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = TeamContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            globalVariable.setCurrentTeamName(mItem.teamName);
//            Toast.makeText(getContext(),"Team Name Global = "+globalVariable.getCurrentTeamName(),Toast.LENGTH_LONG).show();
            playerString = dbHandler.getPlayersArrayList(globalVariable.getCurrentTeamName());
//            globalVariable.setFragmentPopulated(true);


            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.teamName);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.team_detail, container, false);
//        Toast.makeText(getContext(),"Fragment onCreateView",Toast.LENGTH_LONG).show();
//        myText = (TextView) rootView.findViewById(R.id.team_detail);
        //Tried:
        myList = (ListView) rootView.findViewById(R.id.playerListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.simplest_list_item_1, playerString);
        myList.setAdapter(arrayAdapter);


        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.team_detail)).setText(mItem.details);
//        }

        return rootView;
    }

    public void printDatabasePlayers(){
        String dbString = dbHandler.databasePlayersToString(mItem.teamName);
        myText.setText(dbString);
    }

    public TeamContent.TeamItem getmItem(){
        return mItem;
    }
}
