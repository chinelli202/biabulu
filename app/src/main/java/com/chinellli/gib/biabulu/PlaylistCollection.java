package com.chinellli.gib.biabulu;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.chinellli.gib.biabulu.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class PlaylistCollection extends AppCompatActivity implements CategoryActionListener {

    private final String NO_PLAYLIST_MESSAGE = "Aucune playlist";
    private final int NEW_CATEGORY_ACTIVITY_REQUEST_CODE = 1;
    private CategoryViewModel categoryViewModel;
    private TextView noCategoryText;
    private ListView categoryListView;
    CategoryListAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_collection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noCategoryText = findViewById(R.id.noCategoryText);
        categoryListView = findViewById(R.id.playlistList);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        List<Category> catList = categoryViewModel.findAllCategories().getValue();
        if(catList == null)
            catList = new ArrayList<>();
        //listAdapter
        categoryAdapter = new CategoryListAdapter(this,catList);
        categoryAdapter.setCategoryViewModel(categoryViewModel);
        categoryListView.setAdapter(categoryAdapter);
        categoryViewModel.findAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if(categories == null||categories.isEmpty() ){
                    noCategoryText.setVisibility(View.VISIBLE);
                    categoryListView.setVisibility(View.GONE);
                }
                else{
                    noCategoryText.setVisibility(View.GONE);
                    categoryListView.setVisibility(View.VISIBLE);
                    categoryAdapter.clear();
                    categoryAdapter.addAll(categories);
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PlaylistCollection.this, NewCategoryActivity.class);
                //startActivityForResult(intent,NEW_CATEGORY_ACTIVITY_REQUEST_CODE);

                Bundle bundle = new Bundle();
                bundle.putInt(NewCategoryDialogFragment.CATEGORY_REQUEST_KEY,NewCategoryDialogFragment.CREATE_CATEGORY_REQUEST_CODE);
                NewCategoryDialogFragment newCategoryDialogFragment = new NewCategoryDialogFragment();
                newCategoryDialogFragment.setArguments(bundle);
                newCategoryDialogFragment.show(getFragmentManager(),"new category dialog");
                //fragment creation : new fragment, fragment lifecycle manager, fragment action fragment commit
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();
            }
        });
        categoryListView.setOnItemClickListener((adapterView, view, i, l) -> {
            int id = categoryAdapter.getItem(i).getId();
            //open songPage activity passing the id as a parameter
            System.out.println("clicked on category item "+i);
            Intent intent = new Intent(this,SongPage.class);
            intent.putExtra(SongPage.SONG_LIST_KEY,id);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        System.out.println("Testing onActivityResult");
        if(requestCode == NewCategoryDialogFragment.CREATE_CATEGORY_REQUEST_CODE && resultCode == RESULT_OK){
            String categoryName = data.getStringExtra(NewCategoryDialogFragment.RETURN_CATEGORY_NAME_KEY);
            Category category = new Category(categoryName);
            categoryViewModel.insertCategory(category);
            Toast.makeText(this, "New Category Created", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == NewCategoryDialogFragment.UPDATE_CATEGORY_REQUEST_CODE && resultCode == RESULT_OK){
            String categoryName = data.getStringExtra(NewCategoryActivity.CATEGORY_NAME_EXTRA);
            Category category = new Category(categoryName);
            categoryViewModel.updateCategory(category);
            Toast.makeText(this, "Category's name updated", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateCategories(List<Category> categories){

    }

    @Override
    public void deleteAction(int position) {
        categoryViewModel.deleteCategory(categoryAdapter.getItem(position));
        Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateAction(int position, String name) {
        Category category = categoryAdapter.getItem(position);
        category.setName(name);
        categoryViewModel.updateCategory(category);
        Toast.makeText(this, "Category's name updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createAction(String name) {
        Category category = new Category(name);
        categoryViewModel.insertCategory(category);
        Toast.makeText(this, "New Category Created", Toast.LENGTH_SHORT).show();
    }



    /*public void showPopup(View view, int position){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.delete_category_item :

                        //dialog confirm
                        categoryViewModel.deleteCategory(categoryAdapter.getItem(position));
                        return true;
                    default: return false;
                }

            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_categories_more,popup.getMenu());
        popup.show();
    }*/
}
