package ru.ratanov.kinomantv;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.ratanov.kinomantv.model.SearchItem;
import ru.ratanov.kinomantv.search.SearchAPI;
import ru.ratanov.kinomantv.search.SearchListAdapter;

public class ResultsFragment extends Fragment {

    private TextView title;
    private ListView mListView;
    private List<SearchItem> mItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        title = view.findViewById(R.id.results_title);
        mListView = view.findViewById(R.id.results_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = mItems.get(position).getLink();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.LINK, link);
                startActivity(intent);
            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void updateTitle(String newTitle) {
        title.setText(newTitle);
    }

    public void updateSearchResults(String query) {
        mItems = SearchAPI.search(query);
        SearchListAdapter adapter = new SearchListAdapter(getActivity(), mItems);
        mListView.setAdapter(adapter);
    }
}
