package ru.ratanov.kinomantv.parser;


import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ru.ratanov.kinomantv.model.Film;

public class FilmParser {

    public static Film getFilm(String link) {

        try {
            return new GetFilmTask().execute(link).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GetFilmTask extends AsyncTask<String, Integer, Film> {

        @Override
        protected Film doInBackground(String... params) {

            String url = "https://kinozal-tv.appspot.com" + params[0];

            try {
                Document doc = Jsoup.connect(url).get();

                Film film = new Film();
                film.setTitle(doc.select("h1").text());
                film.setDescription(doc.select("div.bx1.justify").select("p").text());
                film.setPosterUrl(doc.select("img.p200").attr("src"));

                return film;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
