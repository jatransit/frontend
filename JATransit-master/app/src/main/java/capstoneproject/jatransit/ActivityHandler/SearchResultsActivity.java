package capstoneproject.jatransit.ActivityHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 03/09/2015.
 */
public class SearchResultsActivity  extends Activity implements SearchView.OnQueryTextListener {

    private TextView txtQuery;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // get the action bar
        ActionBar actionBar = getActionBar();

        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtQuery = (TextView) findViewById(R.id.txtQuery);

        searchView = (SearchView) findViewById(R.id.action_search);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView.setQueryHint("Enter query here");
        searchView.setOnQueryTextListener(this);

        handleIntent(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);


            /**
             * Use this query to display search results like
             * 1. Getting the data from SQLite and showing in listview
             * 2. Making webrequest and displaying the data
             * For now we just display the query only
             */
           // Toast.makeText(this, "search database", Toast.LENGTH_SHORT).show();
            finish();

            txtQuery.setText("Search Query: " + query);

        }

    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("query",query);
        startActivity(intent);
        finish();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
