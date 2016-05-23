package com.example.garrett.ultimatev2;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.garrett.ultimatev2.MasterDetailContent.TeamContent;

import java.util.ArrayList;

/**
 * A fragment representing a single Game detail screen.
 * This fragment is either contained in a {@link GameListActivity}
 * in two-pane mode (on tablets) or a {@link GameDetailActivity}
 * on handsets.
 */
public class GameDetailFragment extends Fragment {
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
    public GameDetailFragment() {
    }

    DBHandler dbHandler;
    TextView myText;
    ListView myList;
    ArrayList<String> gameString;
    ArrayList<Integer> gamePosition;

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
            globalVariable.setViewTeamName(mItem.teamName);

            gameString = dbHandler.getGamesArrayList(globalVariable.getViewTeamName());
            gamePosition = dbHandler.getGamesIDArrayList(globalVariable.getViewTeamName());


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
        View rootView = inflater.inflate(R.layout.game_detail, container, false);

        final Globals globalVariable = (Globals) getActivity().getApplicationContext();
        myList = (ListView) rootView.findViewById(R.id.gameListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.simplest_list_item_1, gameString);
        myList.setAdapter(arrayAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int gameID;
                gameID = gamePosition.get(position);
                Games game = new Games();
                game = dbHandler.getGame(game, gameID);
                globalVariable.setViewGame(game);
//                Toast.makeText(getContext(),"viewGame: "+ globalVariable.getViewGame().get_gamename(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), StatisticsActivity.class);
                startActivity(intent);
            }
        });


        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.team_detail)).setText(mItem.details);
//        }

        return rootView;
    }

    public TeamContent.TeamItem getmItem(){
        return mItem;
    }
}
