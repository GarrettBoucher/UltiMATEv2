package com.example.garrett.ultimatev2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.garrett.ultimatev2.MasterDetailContent.TeamContent;

import java.util.List;

/**
 * An activity representing a list of Games. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GameDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GameListActivity extends AppCompatActivity {

    private boolean mTwoPane;    //Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
    private DBHandler dbHandler;
    GameDetailFragment fragment;
    SimpleItemRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final Globals globalVariable = (Globals) getApplicationContext();
        dbHandler = new DBHandler(this, null, null, 1);
        dbHandler.putTeamsInList();
        FloatingActionButton fabTeam;

        if (findViewById(R.id.game_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

        }
//        Toast.makeText(TeamListActivity.this, "TwoPane: " + mTwoPane, Toast.LENGTH_LONG).show();


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.game_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, HomeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        adapter = new SimpleItemRecyclerViewAdapter(TeamContent.ITEMS);
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private int selectedPos = -1;

        private final List<TeamContent.TeamItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<TeamContent.TeamItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            holder.itemView.setSelected(selectedPos==position);
            if(selectedPos == position){
                holder.itemView.setBackgroundColor(Color.LTGRAY);
                holder.mContentView.setTextColor(Color.BLACK);
            }else{
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                holder.mContentView.setTextColor(Color.WHITE);
            }

            holder.mItem = mValues.get(position);
//            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).teamName);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(TeamListActivity.this, "Position: " + position, Toast.LENGTH_LONG).show();
                    if (mTwoPane) {
                        notifyItemChanged(selectedPos);
                        selectedPos = position;
                        notifyItemChanged(selectedPos);


                        Bundle arguments = new Bundle();
                        arguments.putString(GameDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        fragment = new GameDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.game_detail_container, fragment)
                                .commit();

                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GameDetailActivity.class);
                        intent.putExtra(GameDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            //            public final TextView mIdView;
            public final TextView mContentView;
            public TeamContent.TeamItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
//                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }


}
