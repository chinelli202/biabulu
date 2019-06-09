package com.chinellli.gib.biabulu;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends ListActivity {

    List<String> searchResults = new ArrayList<String>();
    ArrayAdapter<String> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        //setting up adapter
        mAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,searchResults);
        setListAdapter(mAdapter);

        //get search query from intent
        //
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
        else if(Intent.ACTION_VIEW.equals(intent.getAction())){
            Uri data = intent.getData();
            int pos = Integer.valueOf(data.toString());
            Intent intent2 = new Intent(SearchableActivity.this,SongCollectionActivity.class);
            intent.putExtra(SingleSongFragment.POSITION_CANTIQUE,pos);
            startActivity(intent2);
        }
        //run search method
    }

    protected void doMySearch(String query){
        InputStream istream = getResources().openRawResource(R.raw.songlist);
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
        String line;
        try {
            while((line = reader.readLine())!=null){
                if(line.indexOf(query) != -1){
                    mAdapter.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id){
        super.onListItemClick(list, view, position, id);
        String content = ((TextView)view).getText().toString();
        //logic: parse text and open songpage activitty with position = song number
        content = content.replaceAll("\\s","");
        String tokens[] = content.split("-");
        int pos = Integer.valueOf(tokens[0]);
        Intent intent = new Intent(SearchableActivity.this,SongCollectionActivity.class);
        intent.putExtra(SingleSongFragment.POSITION_CANTIQUE,pos);
        startActivity(intent);
    }
}
