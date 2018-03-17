package ru.ratanov.kinomantv.search;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.ratanov.kinomantv.R;
import ru.ratanov.kinomantv.model.SearchItem;

public class SearchListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SearchItem> mItems;

    public SearchListAdapter(Context context, List<SearchItem> items) {
        mContext = context;
        mItems = items;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.search_item, parent, false);
        }

        SearchItem item = (SearchItem) getItem(position);
        ((TextView) view.findViewById(R.id.title)).setText(item.getTitle());
        ((TextView) view.findViewById(R.id.size)).setText(item.getSize());
        ((TextView) view.findViewById(R.id.seeds)).setText(item.getSeeds());
        ((TextView) view.findViewById(R.id.date)).setText(item.getDate());

        return view;
    }
}
