package com.chinellli.gib.biabulu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity implements OnSearchViewActionListener, SongActionListener, SimpleNotifier {

    public static final String GROUP_ID = "GROUP_ID";
    private static final String ALL_SONGS_VALUE = "ALL_SONGS";
    private static final String CATEGORIZED_SONGS_VALUE = "CATEGORIZED_SONGS";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    private String[] listSource;
    private final int max = 300;
    private final int pageSize = 50;
    private ArrayAdapter<String> listAdapter;
    private int page;
    //private CardView cardView;
    Toolbar navbar;
    TextView navBarTitle;
    MenuItem categoryItem;
    GroupFragment scfragment;
    private FrameLayout frameLayout;
    /*
    logic : the list source is updated at the activity creation and when the user goes next or previous
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        frameLayout = findViewById(R.id.frame_list);

        //read intent arguments and then dispatch according to scenario.
        int arg = getIntent().getIntExtra(GroupActivity.GROUP_ID,-1);
        if(arg < 0){
            pagedGroupSetup();
            System.out.println("loading all songs for page : "+arg);
        }
        else{
            System.out.println("loading category songs for category : "+arg);
            customGroupSetup();
        }

        scfragment = GroupFragment.newInstance(arg);
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

    private void pagedGroupSetup(){
        page = -getIntent().getIntExtra(GroupActivity.GROUP_ID,-1);
        navbar = findViewById(R.id.navBar);
        navBarTitle = findViewById(R.id.navbar_title);
        //since we're not passing the category string as an argunent, we're going to have to rebuild it programmatically
        int start = ((page-1) * 50) + 1;
        navBarTitle.setText(String.valueOf(start)+" - " + String.valueOf(start + 49));
    }

    private void customGroupSetup(){
        //set navbar title : category's name,
        navbar = findViewById(R.id.navBar);
        navbar.setTitle(R.string.title_categories);
        navBarTitle = findViewById(R.id.navbar_title);
        String title = getIntent().getStringExtra(GroupActivity.CATEGORY_NAME);
        if(title!=null)
            navBarTitle.setText(title);
        //remove left and right arrows
        View left_arrow = findViewById(R.id.left_arrow);
        View right_arrow = findViewById(R.id.right_arrow);

        left_arrow.setVisibility(View.INVISIBLE);
        right_arrow.setVisibility(View.INVISIBLE);

    }

    @Override
    public void removeSongFromCategory(int songNumber) {
        scfragment.removeSongFromCategory(songNumber);
    }

    @Override
    public void addSongToCategory(int songNumber) {
        scfragment.addSongToCategory(songNumber);
    }

    @Override
    public void notify(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        System.out.println("Song successfully added");
    }
}