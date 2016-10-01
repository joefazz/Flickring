package com.joefazzino.flickring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<Photo> mPhotosList = new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdaptor flickrRecyclerViewAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {

//        @Override
//        public void onClick(View view) {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//            }
//        });

        activateToolbar();


        // Assign Recycler View so it's ready for updates
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flickrRecyclerViewAdaptor = new FlickrRecyclerViewAdaptor(new ArrayList<Photo>()
                , MainActivity.this);
        mRecyclerView.setAdapter(flickrRecyclerViewAdaptor);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, ViewPhotoDetailsActivity.class);
                intent.putExtra(PHOTO_TRANSFER, flickrRecyclerViewAdaptor.getPhoto(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        }));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // When Search item is selected make a new intent pointing towards the SearchActivity Class
        // So that the input can be handled correctly
        if (id == R.id.menu_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // When the focus comes back to MainActivity
    @Override
    protected void onResume() {
        super.onResume();

        // Find out what the query was from the preference data
        String query = getSavedPreferenceData(FLICKR_QUERY);

        // If the query is empty ignore it otherwise execute download of data again
        if (query.length() > 0) {
            ProcessPhotos processPhotos = new ProcessPhotos(query, true);
            processPhotos.execute();
        }


    }

    // Retrieves the query that is saved in the Shared Preferences
    private String getSavedPreferenceData(String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        return sharedPref.getString(key, "");
    }

    // Creates a way for the data to be processed from within MainActivity and makes sure the views
    // are updated
    public class ProcessPhotos extends GetFlickrJsonData {

        public ProcessPhotos(String searchCriteria, boolean matchAll) {
            super(searchCriteria, matchAll);
        }

        public void execute() {
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            @Override
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                flickrRecyclerViewAdaptor.loadNewData(getPhotos());
            }
        }

    }

}
