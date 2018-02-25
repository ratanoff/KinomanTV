package ru.ratanov.kinomantv.parser;


import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ru.ratanov.kinomantv.model.FilmPoster;

public class PosterParser {

    public static final String TAG = PosterParser.class.getSimpleName();

    public static final int FILMS = 1;
    public static final int MULTS = 2;
    public static final int SERIALS = 3;


    public static List<FilmPoster> getTopFilms(int category) {
        try {
            return new GetTopTask().execute(String.valueOf(category)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GetTopTask extends AsyncTask<String, Void, List<FilmPoster>> {

        private String login = "rbaloo";
        private String pass = "756530";

        @Override
        protected List<FilmPoster> doInBackground(String... params) {

            List<FilmPoster> filmPosters = new ArrayList<>();

            String authString = login + ":" + pass;
            String encodedString = new String(Base64.encode(authString.getBytes(), 0));

            String url = "https://kinozal-tv.appspot.com/top.php?t=" + params[0];
//            String url = "https://kinozal-tv.appspot.com/login.php";


            Log.i(TAG, "doInBackground: " + url);

            try {
                Document doc = Jsoup
                        .connect(url)
                        .cookies(Cookies.getCookies())
                        .get();

//                System.out.println(doc.html());

                Elements elements = doc.select("div.bx1").select("a");
                Log.i(TAG, "doInBackground: elements.size() = " + elements.size());
                for (Element entry : elements) {
                    String posterUrl = entry.select("a").select("img").attr("src");
                    String link = entry.select("a").attr("href");

                    FilmPoster filmPoster = new FilmPoster();
                    filmPoster.setPosterUrl(posterUrl);
                    filmPoster.setLink(link);

                    filmPosters.add(filmPoster);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return filmPosters;
        }

        private Map<String, String> getCookies() {

            Connection.Response response;

            try {
                response = Jsoup
                        .connect("https://kinozal-tv.appspot.com/login.php")
                        .method(Connection.Method.GET)
                        .execute();

                response = Jsoup
                        .connect("https://kinozal-tv.appspot.com/takelogin.php")
                        .cookies(response.cookies())
                        .data("username", "rbaloo")
                        .data("password", "756530")
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            System.out.println("Cookies: " + response.cookies());

            return response.cookies();
        }
    }
}
