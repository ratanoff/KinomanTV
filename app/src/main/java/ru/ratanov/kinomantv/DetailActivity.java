package ru.ratanov.kinomantv;

import android.app.Activity;
import android.os.Bundle;

public class DetailActivity extends Activity {

    public static final String LINK = "link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
