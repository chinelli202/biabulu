package com.chinellli.gib.biabulu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddToCategoryDialogFragment extends DialogFragment {
    public static final String SONG_NUMBER_KEY = "SONG_NUMBER";
    public static final String SELECTED_CATEGORY_ID = "CategoryId";
    public interface NoticeDialogListener{
        public void OnCategorySelected(DialogFragment fragment, Category category);
        public void OnCategoryCreated(DialogFragment fragment, Category category);
    }

    private NoticeDialogListener listener;
CategoryViewModel categoryViewModel;
CategoryLoadAdapter loadAdapter;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        //content here
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        int songNum = (int)getArguments().get(SONG_NUMBER_KEY);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        loadAdapter = new CategoryLoadAdapter(this.getContext(),new ArrayList<>());
        new AsyncTask<Integer,Void,Void>(){

            @Override
            protected Void doInBackground(final Integer ...params){
                System.out.println("loading category insert");
                List<Category> listCat = categoryViewModel.findMatchingCategory(params[0]);
                for(int i = 0; i < listCat.size(); i++)
                    System.out.println("matching Category to add to : "+listCat.get(i).getName());
                //loadAdapter.doSetup(listCat);
                loadAdapter.addAll(listCat);
                loadAdapter.notifyDataSetChanged();
                return null;
            }
        }.execute(songNum);
        /*categoryViewModel.findAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                loadAdapter.doSetup(categories);
            }
        });*/

        builder.setTitle(R.string.add_to)
                .setView(layoutInflater.inflate(R.layout.new_category_fragment_dialog,null))
                .setAdapter(loadAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Category category = loadAdapter.getItem(i);
                        //pass them results
                        Intent intent = new Intent();
                        int songNumber = getArguments().getInt(SONG_NUMBER_KEY);
                        intent.putExtra(AddToCategoryDialogFragment.SELECTED_CATEGORY_ID,category.getId());
                        intent.putExtra(AddToCategoryDialogFragment.SONG_NUMBER_KEY,songNumber);
                        getTargetFragment().onActivityResult(SingleCategoryFragment.AVAILABLE_CATEGORIES_REQUEST, RESULT_OK, intent);
                        //listener.OnCategorySelected(AddToCategoryDialogFragment.this,category);
                    }
                });
        return builder.create();
    }

    /*@Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (NoticeDialogListener)context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implememt NoticeDialogListener");
        }
    }*/
}
