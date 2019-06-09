package com.chinellli.gib.biabulu;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements OnSearchViewActionListener {
    public static final String CATEGORY="com.chinelli.gib.biabulu.songCategory";
    ConstraintLayout buttonsLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonsLayout = findViewById(R.id.buttons_layout);
    }

    public void onCategorySelected(View view){
        Intent intent = new Intent(this, SongPage.class);
        String category = ((Button)view).getText().toString();
        category = category.replaceAll("\\s","");
        String tokens[] = category.split("-");
        int pageSize = 50;
        int page = Integer.valueOf(tokens[1])/pageSize;
        page = -page;
        //intent.putExtra(CATEGORY,category);
        intent.putExtra(SongPage.SONG_LIST_KEY,page);
        startActivity(intent);
    }

    public void onListButtonClicked(View view){
        Intent intent = new Intent(this,PlaylistCollection.class);
        startActivity(intent);
    }

    @Override
    public void onSearchWidgetGetFocus() {
        buttonsLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSearchWidgetLoseFocus() {
        buttonsLayout.setVisibility(View.VISIBLE);
    }
}
