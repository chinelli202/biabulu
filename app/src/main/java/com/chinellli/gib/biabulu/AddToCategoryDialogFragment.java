package com.chinellli.gib.biabulu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.chinellli.gib.biabulu.entities.Category;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddToCategoryDialogFragment extends DialogFragment {
    public static final String SONG_NUMBER_KEY = "SONG_NUMBER";
    public static final String SELECTED_CATEGORY_ID = "CategoryId";

    private SongActionListener listener;
CategoryViewModel categoryViewModel;
CategoryLoadAdapter loadAdapter;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        //content here
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = (View)layoutInflater.inflate(R.layout.available_group_selection_layout,null);
        builder.setView(view);

        //Adding ListView and Button behavior
        ListView listView = view.findViewById(R.id.available_groups_list);
        Button button = (Button)view.findViewById(R.id.add_new_group_button);
        button.setOnClickListener(view1 -> {
            saveOnNewGroup();
        });
        int songNum = (int)getArguments().get(SONG_NUMBER_KEY);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        loadAdapter = new CategoryLoadAdapter(this.getActivity(),new ArrayList<>());
        new AsyncTask<Integer,Void,Void>(){

            @Override
            protected Void doInBackground(final Integer ...params){
                System.out.println("loading category insert");
                List<Category> listCat = categoryViewModel.findMatchingCategory(params[0]);
                for(int i = 0; i < listCat.size(); i++)
                    System.out.println("matching Category to add to : "+listCat.get(i).getName());
                //loadAdapter.doSetup(listCat);
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        loadAdapter.addAll(listCat);
                        loadAdapter.notifyDataSetChanged();

                    }
                });

                return null;
            }
        }.execute(songNum);
        listView.setAdapter(loadAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = loadAdapter.getItem(i);
                Intent intent = new Intent();
                int songNumber = getArguments().getInt(SONG_NUMBER_KEY);

                if(getTargetFragment() ==null){
                    if(listener != null){
                        listener.addSongToCategory(songNumber,category.getId());
                    }
                    else
                        dismiss();
                }
                else{
                    intent.putExtra(AddToCategoryDialogFragment.SELECTED_CATEGORY_ID,category.getId());
                    intent.putExtra(AddToCategoryDialogFragment.SONG_NUMBER_KEY,songNumber);
                    getTargetFragment().onActivityResult(GroupFragment.AVAILABLE_CATEGORIES_REQUEST, RESULT_OK, intent);
                }
                dismiss();
            }
        });
        /*categoryViewModel.findAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                loadAdapter.doSetup(categories);
            }
        });*/

         builder.setTitle(R.string.add_to);

        return builder.create();
    }

    private void saveOnNewGroup() {
        //start addToGroupActivity -> onPositiveReturnResult, send intent with new category name
        //new category creation + addition of song to that category.
        int songNumber = getArguments().getInt(SONG_NUMBER_KEY);
        Intent intent = new Intent();
        intent.putExtra(AddToCategoryDialogFragment.SONG_NUMBER_KEY,songNumber);

        if(getTargetFragment() == null)
            if(listener != null){
                listener.addSongToNewCategory(songNumber,null);
                dismiss();
            }
            else
                dismiss();
        else{
            //getTargetFragment().onActivityResult(GroupFragment.AVAILABLE_CATEGORIES_REQUEST, RESULT_CANCELED, intent);
            getTargetFragment().onActivityResult(GroupFragment.AVAILABLE_CATEGORIES_REQUEST, RESULT_CANCELED, intent);
            this.dismiss();
        }
    }


    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (SongActionListener) context;
        }
        catch (ClassCastException e){
            System.out.println( " doesn't implement category operations, will send notifications to fragment ");
        }
    }
}
