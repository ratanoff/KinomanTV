package ru.ratanov.kinomantv.parser;


import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

        HashMap<String, String> hashMap = new HashMap<>();

        @Override
        protected Film doInBackground(String... params) {

            String url = "https://kinozal-tv.appspot.com" + params[0];

            try {
                Document doc = Jsoup
                        .connect(url)
                        .cookies(Cookies.getCookies())
                        .get();

                fillHashMap(doc.select("h2").html());

                Film film = new Film();
                film.setTitle(doc.select("h1").text());
                film.setGenre(getPair("Жанр"));
                film.setDescription(doc.select("div.bx1.justify").select("p").text());
                film.setPosterUrl(doc.select("img.p200").attr("src"));

                String magnetUrl = url.replace("https://kinozal-tv.appspot.com/details.php",
                        "https://s-kinozal-tv.appspot.com/getmagnet?");
                doc = Jsoup.connect(magnetUrl).get();
                String magnetLink = doc.select("a").first().attr("href");
                film.setMagnet(magnetLink);

                return film;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void fillHashMap(String html) {
            Document doc = Jsoup.parse(html);
            doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            doc.select("br").append("_break_");
            String string = Jsoup.clean(doc.html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
            String[] block = string.split("_break_");
            for (int i = 0; i < block.length; i++) {
                block[i] = block[i].trim();
                String[] pairs = block[i].split(": ");
                hashMap.put(pairs[0], pairs[1]);
            }
        }

        private String getPair(String key) {
            return key + ": " + hashMap.get(key);
        }
    }

}
