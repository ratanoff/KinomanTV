package ru.ratanov.kinomantv.search;


import android.net.Uri;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.ratanov.kinomantv.model.SearchItem;
import ru.ratanov.kinomantv.parser.Cookies;

public class SearchAPI {

    public static List<SearchItem> search(String query) {
        System.out.println("search method invoked");

        try {
            return new SearchTask().execute(query).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class SearchTask extends AsyncTask<String, Void, List<SearchItem>> {

        private static final String BASE_URL_SEARCH = "https://kinozal-tv.appspot.com/browse.php";

        private List<SearchItem> mSearchItems = new ArrayList<>();

        @Override
        protected List<SearchItem> doInBackground(String... strings) {

            String url = Uri.parse(BASE_URL_SEARCH)
                    .buildUpon()
                    .appendQueryParameter("s", strings[0])
                    .appendQueryParameter("t", "1")
                    .build()
                    .toString();

            try {
                Document doc = Jsoup
                        .connect(url)
                        .cookies(Cookies.getCookies())
                        .get();

                Elements elements = doc.select("tr.bg");

                for (Element element : elements) {
                    String title = element.select("td.nam").select("a").text();
                    String link = element.select("td.nam").select("a").attr("href");
                    String size = element.select("td.s").get(1).text();
                    String seeds = element.select("td.sl_s").text();
                    String date = element.select("td.s").get(2).text();

                    SearchItem item = new SearchItem();
                    item.setTitle(title);
                    item.setLink(link);
                    item.setSize(size);
                    item.setSeeds(seeds);
                    item.setDate(date);

                    mSearchItems.add(item);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return mSearchItems;
        }

    }
}
