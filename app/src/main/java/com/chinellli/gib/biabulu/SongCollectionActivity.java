package com.chinellli.gib.biabulu;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;

import java.util.List;

public class SongCollectionActivity extends FragmentActivity implements AddToCategoryDialogFragment.NoticeDialogListener{

    private static final int numSongs = 300;
    private ViewPager pager;
    private Toolbar toolbar;
    private CategoryViewModel categoryViewModel;
    private SingleCategoryViewModel singleCategoryViewModel;
    private List<String> songList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_collection);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.song_menu);
        singleCategoryViewModel = ViewModelProviders.of(this).get(SingleCategoryViewModel.class);
        int position = getIntent().getIntExtra(SingleSongFragment.POSITION_CANTIQUE,0);

        int groupCategory = getIntent().getIntExtra(SingleSongFragment.TARGETED_GROUP,-1);
        pager = findViewById(R.id.activity_song_collection_viewpager);
        pager.setAdapter(new SongsPagerAdapter(getSupportFragmentManager()));

        new AsyncTask<Integer,Void,Void>(){

            @Override
            protected Void doInBackground(final Integer ...params){
                if(params[0] < 0){
                    songList = singleCategoryViewModel.getListSource();
                }
                else{

                    songList = singleCategoryViewModel.getSongs(params[0]);
                }
                pager.getAdapter().notifyDataSetChanged();
                pager.setCurrentItem(position);
                return null;
            }
        }.execute(groupCategory);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);


        Menu menu = toolbar.getMenu();

        MenuItem cats = menu.findItem(R.id.add_to_playlist);
        cats.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //invoke categories dialog fragment
                int position = pager.getCurrentItem();
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
    public void OnCategorySelected(DialogFragment fragment, Category category) {
        ListedSong song = new ListedSong(pager.getCurrentItem(),category);
        categoryViewModel.insertListedSong(song);
        Toast.makeText(this.getApplicationContext(),"Ajouté à "+category.getName(),2000);
    }

    @Override
    public void OnCategoryCreated(DialogFragment fragment, Category category) {

    }

    private class SongsPagerAdapter extends FragmentStatePagerAdapter {


        public SongsPagerAdapter(FragmentManager fm){
            super(fm);
        }
        //load datasource list : all songs or catetorized songs
        //for each position for the pager, pass the song number at that index in the arraylist as an argument to SingleSongFragment.newInstance(int)


        @Override
        public Fragment getItem(int position) {
                //find song number at given "position" in the songList and pass it as argument
                String songTitle = songList.get(position);
                String tokens[] = songTitle.split("[.]");
                position = Integer.valueOf(tokens[0])-1;
                Fragment fragment = SingleSongFragment.newInstance(position);
                //Bundle args = new Bundle();
                //args.putInt(SingleSongFragment.POSITION_CANTIQUE, position);
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
        }
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
