package com.chinellli.gib.biabulu;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SongPage extends AppCompatActivity implements OnSearchViewActionListener, SongActionListener {

    public static final String SONG_LIST_KEY = "SONG_LIST_KEY";
    private static final String ALL_SONGS_VALUE = "ALL_SONGS";
    private static final String CATEGORIZED_SONGS_VALUE = "CATEGORIZED_SONGS";
    private String[] listSource;
    private final int max = 300;
    private final int pageSize = 50;
    private ArrayAdapter<String> listAdapter;
    private int page;
    //private CardView cardView;
    Toolbar navbar;
    TextView navBarTitle;
    MenuItem categoryItem;
    SingleCategoryFragment scfragment;
    private FrameLayout frameLayout;
    /*
    logic : the list source is updated at the activity creation and when the user goes next or previous
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_page);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        frameLayout = findViewById(R.id.frame_list);
        //cardView = findViewById(R.id.songListCard);


        //read intent arguments and then dispatch according to scenario.
        int arg = getIntent().getIntExtra(SongPage.SONG_LIST_KEY,-1);
        if(arg < 0){
            allSongsSetup();
            System.out.println("loading all songs for page : "+arg);
        }
        else{
            System.out.println("loading category songs for category : "+arg);
            categorizedSongsSetup();
        }
        /*TextView textView = new TextView(getApplicationContext());
        textView.setText("Dummy Text here");
        Toolbar.LayoutParams lpView = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lpView);
        frameLayout.addView(textView);*/
        scfragment = SingleCategoryFragment.newInstance(arg);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_list,scfragment).commit();

        //load fragment

        //scenario 1 : loading all songs
        /*
        *  add navbar
        *
        *
        * */
        //scenario 2 : loading categories songs
        /*
        *
        *
        *
        *
        * */
        //argument : positive int -> category id. negative int -> negative all songs category.

        /*navbar.inflateMenu(R.menu.songs_list);
        categoryItem = navbar.getMenu().getItem(1);
        categoryItem.setTitle(category);*/
        /*SpannableString s = new SpannableString(categoryItem.getTitle());

        s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);

        categoryItem.setTitle(s);*/
        //navbar.setTitle(category);*/
        /*
         */
        //loading the arguments passed on to the activity by it's calling intent : the
        //parsing 'category' and extracting the page value


        /*navbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.list_left :
                        navigatePrevious();
                        return true;
                    case R.id.list_right :
                        navigateNext();
                        return true;
                        default:return false;
                }
            }
        });*/

        //filling up list with static data from songList
        /*listSource = new String[max];
        try{
            Resources res = getResources();
            InputStream istream = res.openRawResource(R.raw.songlist);
            BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            String line;
            int i = 0;
            while((line = reader.readLine())!=null && i < max){
                listSource[i] = line;
                i++;
            }
        }
        catch(IOException e){

        }*/

        //setting up the page adapter and other stuff
        /*ListView mainList = findViewById(R.id.songListView);
        ArrayList songs = loadSourceItems();
        listAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,songs);
        mainList.setAdapter(listAdapter);
        mainList.setClickable(true);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView)view;
                String content = textView.getText().toString();
                System.out.println("content value : " + content);
                //content = content.replaceAll("\\s","");
                String tokens[] = content.split("[.]");
                int pos = Integer.valueOf(tokens[0])-1;
                Intent intent = new Intent(SongPage.this,SongCollectionActivity.class);
                intent.putExtra(SingleSongFragment.POSITION_CANTIQUE,pos);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onSearchWidgetGetFocus() {
        frameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSearchWidgetLoseFocus() {
        frameLayout.setVisibility(View.VISIBLE);
    }


    //load the 50 songs corresponding to the given page : open the songs file and read from line (page-1 x 50) + 1 to line (page-1 x 50) + 1 + 51
    private ArrayList<String> loadSourceItems(){
        //java resource loading + read file
        ArrayList<String> pagedSongs = new ArrayList<>();
        int start = ((page-1) * 50) + 1;
        for(int i = start; i <= start + 49; i++){
            if(listSource[i-1]!=null)
                pagedSongs.add(listSource[i-1]);
        }
        return pagedSongs;
    }

    public void navigateNext(View view){
        if(page < max/pageSize){
            page++;
            scfragment.moveNextOrPrevious(page);
            int start = ((page-1) * 50) + 1;
            //categoryItem.setTitle(String.valueOf(start)+" - " + String.valueOf(start + 49));
            navBarTitle.setText(String.valueOf(start)+" - " + String.valueOf(start + 49));
        }
    }

    public void navigatePrevious(View view){
        if(page > 1){
            page--;
            scfragment.moveNextOrPrevious(page);
            int start = ((page-1) * 50) + 1;
            //categoryItem.setTitle(String.valueOf(start)+" - " + String.valueOf(start + 49));
            navBarTitle.setText(String.valueOf(start)+" - " + String.valueOf(start + 49));
        }
    }

    private void allSongsSetup(){
        page = -getIntent().getIntExtra(SongPage.SONG_LIST_KEY,-1);
        navbar = findViewById(R.id.navBar);
        navBarTitle = findViewById(R.id.navbar_title);
        //since we're not passing the category string as an argunent, we're going to have to rebuild it programmatically
        int start = ((page-1) * 50) + 1;
        navBarTitle.setText(String.valueOf(start)+" - " + String.valueOf(start + 49));
    }

    private void categorizedSongsSetup(){

    }

    @Override
    public void removeSongFromCategory(int songNumber) {
        scfragment.removeSongFromCategory(songNumber);
    }

    @Override
    public void addSongToCategory(int songNumber) {
        scfragment.addSongToCategory(songNumber);
    }
}