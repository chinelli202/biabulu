package com.chinellli.gib.biabulu;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;

import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

public class SingleCategoryFragment extends Fragment implements SongActionListener{

    private SingleCategoryViewModel mViewModel;
    private int page;
    SongListAdapter songsAdapter;
    public static final int AVAILABLE_CATEGORIES_REQUEST = 20;
    public static SingleCategoryFragment newInstance(int id) {
        SingleCategoryFragment singleCategoryFragment = new SingleCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(SongPage.SONG_LIST_KEY,id);
        singleCategoryFragment.setArguments(args);
        return singleCategoryFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        //retrieve the list from the inflated view and add  click listeners to all list items, start songcollection activity

        //split cases : SongCollection for all songs, SongCollection for categorized songs

        //datasource : set the adapter's datasource (arraylist of all songs) at initialisation
        //perform binding view action based on data from the datasource.

        View view = inflater.inflate(R.layout.single_category_fragment, container, false);
        page = (int)getArguments().get(SongPage.SONG_LIST_KEY);
        int arg;
        if(page > 0)
            arg = SongListAdapter.SONG_GROUP_CATEGORY_TYPE;
        else
            arg = SongListAdapter.SONG_GROUP_WHOLE_LIST_TYPE;
        ListView listView = view.findViewById(R.id.songListView);
        songsAdapter = new SongListAdapter(getContext(),R.layout.song_item,arg);
        //songsAdapter = new ArrayAdapter<String>(getContext(),R.layout.song_item,new ArrayList<String>(){});



        listView.setAdapter(songsAdapter);
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            TextView textView = view1.findViewById(R.id.song_item_text);
            String content = textView.getText().toString();
            System.out.println("content value : " + content);
            //content = content.replaceAll("\\s","");
            String tokens[] = content.split("[.]");
            int pos;
            if(page < 0)
                pos = Integer.valueOf(tokens[0])-1;
            else
                pos = i;
            Intent intent = new Intent(this.getContext(),SongCollectionActivity.class);
            intent.putExtra(SingleSongFragment.POSITION_CANTIQUE,pos);
            intent.putExtra(SingleSongFragment.TARGETED_GROUP,page);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SingleCategoryViewModel.class);
        // TODO: Use the ViewModel
        //retrieve data from the parent activity
        new AsyncTask<Integer,Void,Void>(){

            @Override
            protected Void doInBackground(final Integer ...params){
                songsAdapter.addAll(mViewModel.getSongs(params[0]));
                return null;
            }
        }.execute(page);

        //songsAdapter.addAll(mViewModel.getSongs(testInt));
    }

    public void moveToPrevious(int page){
        songsAdapter.clear();
        songsAdapter.addAll(mViewModel.getSongs(page));
    }

    public void moveToNext(int page){

    }

    public void moveNextOrPrevious(int page){
        songsAdapter.clear();
        songsAdapter.addAll(mViewModel.getSongs(-page));//passing the opposite value of page so the getSongs method will switch to pagedsongs list
    }

    /****
     *
     * Songs event handlers :
     *
     * Group songs : remove song from group handler
     *
     * All list songs : add song to list handler
     *
     * ****/


    //params : category, ListedSong
    @Override
    public void removeSongFromCategory(int position){
        //Confirm dialog
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("Confirmation");
        dialog.setMessage("Voulez-vous vraiment supprimer "+songsAdapter.getItem(position));
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //categoryViewModel.deleteCategory(getItem(position));

                        //make sure the calling context implements the category Action Listener

                        //((CategoryActionListener)getContext()).deleteAction(position);
                        mViewModel.deleteListedSong(position);
                        songsAdapter.remove(songsAdapter.getItem(position));
                        songsAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(),"song correctly removed from category",Toast.LENGTH_SHORT);
                    }
                });
        dialog.show();

    }
    @Override
    public void addSongToCategory(int songNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(AddToCategoryDialogFragment.SONG_NUMBER_KEY,songNumber);
        AddToCategoryDialogFragment addToCategoryDialogFragment = new AddToCategoryDialogFragment();
        addToCategoryDialogFragment.setTargetFragment(this,SingleCategoryFragment.AVAILABLE_CATEGORIES_REQUEST);
        addToCategoryDialogFragment.setArguments(bundle);
        addToCategoryDialogFragment.show(getFragmentManager(),"SelectedCats");
        //mViewModel.addSongToCategory(songNumber);
        //load avalaible categories dialog
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult( requestCode,  resultCode,  data);
        System.out.println("Ready to add song to available category");
        if(requestCode == SingleCategoryFragment.AVAILABLE_CATEGORIES_REQUEST && resultCode == RESULT_OK){
            System.out.println("Ready to add song to available category");
            int catId = data.getIntExtra(AddToCategoryDialogFragment.SELECTED_CATEGORY_ID,1);
            int songNum = data.getIntExtra(AddToCategoryDialogFragment.SONG_NUMBER_KEY,1);
            mViewModel.addSongToCategory(songNum,catId);
            Toast.makeText(getContext(),"Song successfully added to Category",Toast.LENGTH_SHORT);
        }
    }
    private void addSongToList(int songNumber){
        //build. category, then listedsong, then add
        mViewModel.addSongToCategory(songNumber);
    }
}