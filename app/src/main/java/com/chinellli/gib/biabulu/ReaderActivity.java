package com.chinellli.gib.biabulu;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;

import java.util.List;

public class ReaderActivity extends FragmentActivity implements SongActionListener, SimpleNotifier{

    private static final int numSongs = 300;
    public static final String GROUP_NAME = "GROUP_NAME";
    private ViewPager pager;
    private Toolbar toolbar;
    private CategoryViewModel categoryViewModel;
    private GroupViewModel groupViewModel;
    private List<String> songList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.song_menu);
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        int position = getIntent().getIntExtra(SongReaderFragment.POSITION_CANTIQUE,0);

        int groupCategory = getIntent().getIntExtra(SongReaderFragment.TARGETED_GROUP,-1);
        //toolbar.setTitle(R.string.title_home);

        pager = findViewById(R.id.activity_song_collection_viewpager);
        pager.setAdapter(new SongsPagerAdapter(getSupportFragmentManager()));


        new AsyncTask<Integer,Void,Void>(){

            @Override
            protected Void doInBackground(final Integer ...params){
                customizeTitle(groupCategory);
                if(params[0] < 0){
                    songList = groupViewModel.getListSource();
                }
                else{

                    songList = groupViewModel.getSongs(params[0]);
                }
                //songList = groupViewModel.getSongs(params[0]);
                pager.getAdapter().notifyDataSetChanged();
                pager.setCurrentItem(position);
                return null;
            }
        }.execute(groupCategory);



        Menu menu = toolbar.getMenu();

        MenuItem cats = menu.findItem(R.id.add_to_playlist);
        cats.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //invoke categories dialog fragment
                int position = pager.getCurrentItem() + 1;
                Bundle bundle = new Bundle();
                bundle.putInt(AddToCategoryDialogFragment.SONG_NUMBER_KEY,position);
                AddToCategoryDialogFragment addToCategoryDialogFragment = new AddToCategoryDialogFragment();
                //addToCategoryDialogFragment.setTargetFragment();
                addToCategoryDialogFragment.setArguments(bundle);
                addToCategoryDialogFragment.show(getSupportFragmentManager(),"SelectedCats");
                return true;
            }
        });
    }

    @Override
    public void removeSongFromCategory(int songNumber, int catId) {

    }

    @Override
    public void addSongToCategory(int songNumber, int catId) {
        groupViewModel.addSongToCategory(songNumber, catId, this);
    }

    @Override
    public void addSongToNewCategory(int songNumber, String catName) {
        //if catName is null, launch dialog for creating new category
        if(catName == null){
            CategoryEditDialogFragment categoryEditDialogFragment = new CategoryEditDialogFragment();
            //categoryEditDialogFragment.setTargetFragment(GroupFragment.this,GroupFragment.NEW_CATEGORY_REQUEST);
            //Data
            Bundle bundle = new Bundle();
            bundle.putInt( AddToCategoryDialogFragment.SONG_NUMBER_KEY,songNumber);
            bundle.putInt( CategoryEditDialogFragment.CATEGORY_REQUEST_KEY,CategoryEditDialogFragment.CREATE_CATEGORY_REQUEST_CODE);
            categoryEditDialogFragment.setArguments(bundle);
            //Dialog display
            categoryEditDialogFragment.show(getSupportFragmentManager(),"newCat");
        }
        else {
            if(songNumber == 0)
                songNumber = pager.getCurrentItem() + 1;
            groupViewModel.addSongToNewCategory(songNumber, catName, this);
        }
    }

    @Override
    public void notify(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private class SongsPagerAdapter extends FragmentStatePagerAdapter {


        public SongsPagerAdapter(FragmentManager fm){
            super(fm);
        }
        //load datasource list : all songs or catetorized songs
        //for each position for the pager, pass the song number at that index in the arraylist as an argument to SongReaderFragment.newInstance(int)


        @Override
        public Fragment getItem(int position) {
                //find song number at given "position" in the songList and pass it as argument
                String songTitle = songList.get(position);
                String tokens[] = songTitle.split("[.]");
                position = Integer.valueOf(tokens[0])-1;
                Fragment fragment = SongReaderFragment.newInstance(position);
                //Bundle args = new Bundle();
                //args.putInt(SongReaderFragment.POSITION_CANTIQUE, position);
                //fragment.setArguments(args);
                return fragment;
        }


        @Override
        public int getCount() {
            if(songList==null || songList.size() == 0)
                return 0;
            else
                return songList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String songTitle = songList.get(position);
            String tokens[] = songTitle.split("[.]");
            position = Integer.valueOf(tokens[0])-1;

            return "Cantique " + (position + 1);
            //return songTitle;
            //return "merchand";
        }
    }

    private void customizeTitle(int index){
        //String titleName;
        if(index > 0){
            //toolbar.setTitle(getIntent().getStringExtra(ReaderActivity.GROUP_NAME));
            Category cat = categoryViewModel.findCategory(index);
            if(cat!=null){
                toolbar.setTitle(cat.getName());
            }
        }
        else
            toolbar.setTitle(R.string.app_label);
    }

    private class CustomPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            return null;
        }
    }

    public void isolateActionHandler(MenuItem menuItem){
        String x = menuItem.getTitle().toString();

        //need categories where song isn't registered. if none, item list empty
        //song number, category's list, listedSong List


        //match selected item with category
        //invoque repository's categoryInsert Method
        //toast successful insert

        //create new category : wrap new category dialog into insert item call
        //on success : invoque inserte method and display success in toast
    }
}
