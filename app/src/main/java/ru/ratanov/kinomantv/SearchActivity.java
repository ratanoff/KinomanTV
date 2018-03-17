package ru.ratanov.kinomantv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SearchActivity extends Activity implements SearchFragment.OnFragmentInteractionListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent: ");
    }

    public void search(String query) {
        Log.v(TAG, "new Query: " + query);
    }

    @Override
    public void onFragmentInteraction(String query) {
        ResultsFragment resultsFragment = (ResultsFragment) getFragmentManager().findFragmentById(R.id.results_fragment);
        resultsFragment.updateTitle(query);
        resultsFragment.updateSearchResults(query);
    }
}
