package ru.ratanov.kinomantv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.widget.Toast;

import java.util.List;

import ru.ratanov.kinomantv.model.FilmPoster;
import ru.ratanov.kinomantv.parser.PosterParser;
import ru.ratanov.kinomantv.presenter.CardPresenter;


public class MainFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupUIElements();

        loadRows();

        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private void setupUIElements() {
        setTitle("Киноман ТВ");
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));
    }

    private void loadRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        HeaderItem filmHeader = new HeaderItem(0, "Фильмы");
        ArrayObjectAdapter filmsAdapter = new ArrayObjectAdapter(cardPresenter);
        List<FilmPoster> films = PosterParser.getTopFilms(PosterParser.FILMS);
        filmsAdapter.addAll(0, films);
        mRowsAdapter.add(new ListRow(filmHeader, filmsAdapter));

        HeaderItem multsHeader = new HeaderItem(1, "Мультфильмы");
        ArrayObjectAdapter multsAdapter = new ArrayObjectAdapter(cardPresenter);
        List<FilmPoster> mults = PosterParser.getTopFilms(PosterParser.MULTS);
        multsAdapter.addAll(0, mults);
        mRowsAdapter.add(new ListRow(multsHeader, multsAdapter));

        HeaderItem serialsHeader = new HeaderItem(2, " Сериалы");
        ArrayObjectAdapter serialsAdapter = new ArrayObjectAdapter(cardPresenter);
        List<FilmPoster> serials = PosterParser.getTopFilms(PosterParser.SERIALS);
        serialsAdapter.addAll(0, serials);
        mRowsAdapter.add(new ListRow(serialsHeader, serialsAdapter));

        setAdapter(mRowsAdapter);
    }

    private class ItemViewClickedListener implements OnItemViewClickedListener {

        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            FilmPoster poster = (FilmPoster) item;
            String link = poster.getLink();
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(DetailActivity.LINK, link);
            startActivity(intent);
        }
    }
}
