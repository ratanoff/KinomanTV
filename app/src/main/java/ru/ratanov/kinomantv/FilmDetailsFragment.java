package ru.ratanov.kinomantv;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import ru.ratanov.kinomantv.model.Film;
import ru.ratanov.kinomantv.parser.FilmParser;
import ru.ratanov.kinomantv.presenter.CustomFullWidthDetailsOverviewRowPresenter;
import ru.ratanov.kinomantv.presenter.DetailsPresenter;

public class FilmDetailsFragment extends DetailsFragment {

    private static final String LINK = "link";

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
            Action downloadAction = new Action(0, "Скачать");
            Action trailerAction = new Action(1, "Трейлер");
            SparseArrayObjectAdapter sparseArrayObjectAdapter = new SparseArrayObjectAdapter();
            sparseArrayObjectAdapter.set(0, downloadAction);
            sparseArrayObjectAdapter.set(1, trailerAction);
            row.setActionsAdapter(sparseArrayObjectAdapter);

            ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
            classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, mFwdorPresenter);

            ArrayObjectAdapter adapter = new ArrayObjectAdapter(classPresenterSelector);
            adapter.add(row);

            setAdapter(adapter);
        }
    }
}
