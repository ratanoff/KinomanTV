package ru.ratanov.kinomantv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ru.ratanov.kinomantv.parser.Cookies;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }
}
