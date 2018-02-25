package ru.ratanov.kinomantv;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import ru.ratanov.kinomantv.model.Film;
import ru.ratanov.kinomantv.parser.FilmParser;
import ru.ratanov.kinomantv.presenter.CustomFullWidthDetailsOverviewRowPresenter;
import ru.ratanov.kinomantv.presenter.DetailsPresenter;

public class FilmDetailsFragment extends DetailsFragment {

    private static final String LINK = "link";

    private static final int ACTION_DOWNLOAD = 1;
    private static final int ACTION_TRAILER = 2;

    private String mLink;
    private Film mFilm;

    private CustomFullWidthDetailsOverviewRowPresenter mFwdorPresenter;
    private DetailsRowBuilderTask mDetailsRowBuilderTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLink = getActivity().getIntent().getStringExtra(LINK);
        mFilm = FilmParser.getFilm(mLink);

        mFwdorPresenter = new CustomFullWidthDetailsOverviewRowPresenter(new DetailsPresenter());
        mDetailsRowBuilderTask = (DetailsRowBuilderTask) new DetailsRowBuilderTask().execute(mFilm);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDetailsRowBuilderTask.cancel(true);
    }

    private class DetailsRowBuilderTask extends AsyncTask<Film, Integer, DetailsOverviewRow> {

        @Override
        protected DetailsOverviewRow doInBackground(Film... films) {
            DetailsOverviewRow row = new DetailsOverviewRow(mFilm);
            try {
                Bitmap poster = Picasso.with(getActivity())
                        .load(mFilm.getPosterUrl())
//                        .centerCrop()
                        .get();
                row.setImageBitmap(getActivity(), poster);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return row;
        }

        @Override
        protected void onPostExecute(DetailsOverviewRow row) {
            /* Actions */
            Action downloadAction = new Action(ACTION_DOWNLOAD, "Скачать");
            Action trailerAction = new Action(ACTION_TRAILER, "Трейлер");


            SparseArrayObjectAdapter sparseArrayObjectAdapter = new SparseArrayObjectAdapter();
            sparseArrayObjectAdapter.set(0, downloadAction);
            sparseArrayObjectAdapter.set(1, trailerAction);
            row.setActionsAdapter(sparseArrayObjectAdapter);

            mFwdorPresenter.setOnActionClickedListener(new OnActionClickedListener() {
                @Override
                public void onActionClicked(Action action) {
                    if (action.getId() == ACTION_DOWNLOAD) {
//                        Toast.makeText(getActivity(), "DOWNLOAD " + mFilm.getMagnet(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setType("application/x-bittorrent");
                        intent.setData(Uri.parse(mFilm.getMagnet()));
                        getActivity().startActivity(intent);
                    }
                }
            });

            ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
            classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, mFwdorPresenter);

            ArrayObjectAdapter adapter = new ArrayObjectAdapter(classPresenterSelector);
            adapter.add(row);

            setAdapter(adapter);
        }
    }
}
