package ru.ratanov.kinomantv.presenter;


import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import ru.ratanov.kinomantv.model.Film;

public class DetailsPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Film film = (Film) item;

        if (film != null) {
            viewHolder.getTitle().setText(film.getTitle());
            viewHolder.getSubtitle().setText(film.getGenre());
            viewHolder.getBody().setText(film.getDescription());

        }
    }
}
