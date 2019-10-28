package com.chinellli.gib.biabulu;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Group;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class GroupFragment extends Fragment implements SongActionListener, CategoryActionListener{

    private GroupViewModel mViewModel;
    private int page;
    GroupListAdapter groupListAdapter;
    public static final int AVAILABLE_CATEGORIES_REQUEST = 20;
    public static final int NEW_CATEGORY_REQUEST = 30;
    public static final String NEW_CATEGORY_NAME_KEY = "NEW_CATEGORY_NAME";
    public static GroupFragment newInstance(int id) {
        GroupFragment groupFragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(GroupActivity.GROUP_ID,id);
        groupFragment.setArguments(args);
        return groupFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        //retrieve the list from the inflated view and add  click listeners to all list items, start songcollection activity

        //split cases : SongCollection for all songs, SongCollection for categorized songs

        //datasource : set the adapter's datasource (arraylist of all songs) at initialisation
        //perform binding view action based on data from the datasource.

        View view = inflater.inflate(R.layout.group_fragment, container, false);
        page = (int)getArguments().get(GroupActivity.GROUP_ID);
        int arg;
        if(page > 0)
            arg = GroupListAdapter.CUSTOM_GROUP;
        else
            arg = GroupListAdapter.PAGED_GROUP;
        ListView listView = view.findViewById(R.id.songListView);

        Context ctx = getActivity();

        groupListAdapter = new GroupListAdapter(ctx,R.layout.song_item,arg);
        //groupListAdapter = new ArrayAdapter<String>(getContext(),R.layout.song_item,new ArrayList<String>(){});



        listView.setAdapter(groupListAdapter);
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
            Intent intent = new Intent(this.getActivity(),ReaderActivity.class);
            intent.putExtra(SongReaderFragment.POSITION_CANTIQUE,pos);
            intent.putExtra(SongReaderFragment.TARGETED_GROUP,page);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        // TODO: Use the ViewModel
        //retrieve data from the parent activity
        new AsyncTask<Integer,Void,Void>(){

            @Override
            protected Void doInBackground(final Integer ...params){
                groupListAdapter.addAll(mViewModel.getSongs(params[0]));
                return null;
            }
        }.execute(page);

        //groupListAdapter.addAll(mViewModel.getSongs(testInt));
    }

    public void moveToPrevious(int page){
        groupListAdapter.clear();
        groupListAdapter.addAll(mViewModel.getSongs(page));
    }

    public void moveToNext(int page){

    }

    public void moveNextOrPrevious(int page){
        groupListAdapter.clear();
        groupListAdapter.addAll(mViewModel.getSongs(-page));//passing the opposite value of page so the getSongs method will switch to pagedsongs list
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
    public void removeSongFromCategory(int position, int catId){
        //Confirm dialog
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle("Confirmation");
        dialog.setMessage("Voulez-vous vraiment supprimer "+ groupListAdapter.getItem(position));
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //categoryViewModel.deleteCategory(getItem(position));

                        //make sure the calling context implements the category Action Listener

                        //((CategoryActionListener)getContext()).deleteAction(position);
                        mViewModel.deleteListedSong(position);
                        groupListAdapter.remove(groupListAdapter.getItem(position));
                        groupListAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(),"song correctly removed from category",Toast.LENGTH_SHORT);
                    }
                });
        dialog.show();

    }
    @Override
    public void addSongToCategory(int songNumber, int catId) {
        Bundle bundle = new Bundle();
        bundle.putInt(AddToCategoryDialogFragment.SONG_NUMBER_KEY,songNumber);
        AddToCategoryDialogFragment addToCategoryDialogFragment = new AddToCategoryDialogFragment();
        addToCategoryDialogFragment.setTargetFragment(GroupFragment.this,GroupFragment.AVAILABLE_CATEGORIES_REQUEST);
        addToCategoryDialogFragment.setArguments(bundle);
        addToCategoryDialogFragment.show(getFragmentManager(),"SelectedCats");
        //mViewModel.addSongToCategory(songNumber);
        //load avalaible categories dialog
    }

    @Override
    public void addSongToNewCategory(int songNumber, String catName) {

    }

    //
    public void addSongToNewCategory(int songNumber){

        //dialog, data, dialog show
        //openCatEditDialog
        //have this fragment implement categoryEditEventListener
        //on Event completion, return group name, song position,

        //Dialog
        CategoryEditDialogFragment categoryEditDialogFragment = new CategoryEditDialogFragment();
        categoryEditDialogFragment.setTargetFragment(GroupFragment.this,GroupFragment.NEW_CATEGORY_REQUEST);
        //Data
        Bundle bundle = new Bundle();
        bundle.putInt( AddToCategoryDialogFragment.SONG_NUMBER_KEY,songNumber);
        bundle.putInt( CategoryEditDialogFragment.CATEGORY_REQUEST_KEY,CategoryEditDialogFragment.CREATE_CATEGORY_REQUEST_CODE);
        categoryEditDialogFragment.setArguments(bundle);
        //Dialog display
        categoryEditDialogFragment.show(getFragmentManager(),"newCat");
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult( requestCode,  resultCode,  data);
        System.out.println("Ready to add song to available category");

        //adding song to selected category
        if(requestCode == GroupFragment.AVAILABLE_CATEGORIES_REQUEST && resultCode == RESULT_OK){
            System.out.println("Ready to add song to available category");
            int catId = data.getIntExtra(AddToCategoryDialogFragment.SELECTED_CATEGORY_ID,1);
            int songNum = data.getIntExtra(AddToCategoryDialogFragment.SONG_NUMBER_KEY,1);
            mViewModel.addSongToCategory(songNum,catId, this.getContext());
            //Toast.makeText(this.getContext(),"Song successfully added to Category",Toast.LENGTH_SHORT).show();
        }

        //creating new category and adding song to it
        if(requestCode == GroupFragment.AVAILABLE_CATEGORIES_REQUEST && resultCode == RESULT_CANCELED){
            if(data.getIntExtra(AddToCategoryDialogFragment.SONG_NUMBER_KEY,0)!=0){
                int num = data.getIntExtra(AddToCategoryDialogFragment.SONG_NUMBER_KEY,0);
                addSongToNewCategory(num);
            }
        }
        //this condition is reached after the user has entered the name for the new category to add the song in
        if(requestCode == GroupFragment.NEW_CATEGORY_REQUEST && resultCode == RESULT_OK){
            //create new category with given name, retrieve said category with id, add song to list


            int songNum = data.getIntExtra(AddToCategoryDialogFragment.SONG_NUMBER_KEY,1);
            String catName = data.getStringExtra(GroupFragment.NEW_CATEGORY_NAME_KEY);

            mViewModel.addSongToNewCategory(songNum,catName, this.getContext());
            Toast.makeText(this.getContext(),"Song successfully added to " + catName,Toast.LENGTH_LONG).show();

            //mViewModel.
        }
    }
    /*private void addSongToList(int songNumber){
        //build. category, then listedsong, then add
        mViewModel.addSongToCategory(songNumber);
    }*/

    @Override
    public void deleteAction(int position) {

    }

    @Override
    public void updateAction(int position, String name) {

    }

    @Override
    public void createAction(String name) {

    }

    @Override
    public void notify(String message) {
        Toast.makeText(this.getActivity(),message,Toast.LENGTH_SHORT).show();
        System.out.println("Song successfully added");
    }
}