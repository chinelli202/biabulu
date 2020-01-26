package com.chinellli.gib.biabulu;

import android.app.FragmentManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.util.QueryResult;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements CategoryActionListener {

    private final String NO_PLAYLIST_MESSAGE = "Aucune playlist";
    private final int NEW_CATEGORY_ACTIVITY_REQUEST_CODE = 1;
    private CategoryViewModel categoryViewModel;
    private TextView noCategoryText;
    private ListView categoryListView;
    CategoryListAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_categories);
        setSupportActionBar(toolbar);
        noCategoryText = findViewById(R.id.noCategoryText);
        noCategoryText.setText(R.string.no_category_text);
        categoryListView = findViewById(R.id.playlistList);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        List<Category> catList = categoryViewModel.findAllCategories().getValue();
        /*if(catList == null)
            catList = new ArrayList<>();*/
        //listAdapter
        categoryAdapter = new CategoryListAdapter(this,new ArrayList<>());
        //categoryAdapter.setCategoryViewModel(categoryViewModel);
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(CategoriesActivity.this, CategoryEditActivity.class);
                //startActivityForResult(intent,NEW_CATEGORY_ACTIVITY_REQUEST_CODE);

                Bundle bundle = new Bundle();
                bundle.putInt(CategoryEditDialogFragment.CATEGORY_REQUEST_KEY,CategoryEditDialogFragment.CREATE_CATEGORY_REQUEST_CODE);
                CategoryEditDialogFragment categoryEditDialogFragment = new CategoryEditDialogFragment();
                categoryEditDialogFragment.setArguments(bundle);
                categoryEditDialogFragment.show(getSupportFragmentManager(),"new Category");
                //fragment creation : new fragment, fragment lifecycle manager, fragment action fragment commit
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();
            }
        });
        fab.setBackgroundColor(getColor(R.color.colorPrimary));
        categoryListView.setOnItemClickListener((adapterView, view, i, l) -> {
            int id = categoryAdapter.getItem(i).getId();
            //open songPage activity passing the id as a parameter
            System.out.println("clicked on category item "+i);
            Intent intent = new Intent(this,GroupActivity.class);
            intent.putExtra(GroupActivity.CATEGORY_NAME,categoryAdapter.getItem(i).getName());
            intent.putExtra(GroupActivity.GROUP_ID,id);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        System.out.println("Testing onActivityResult");
        if(requestCode == CategoryEditDialogFragment.CREATE_CATEGORY_REQUEST_CODE && resultCode == RESULT_OK){
            String categoryName = data.getStringExtra(CategoryEditDialogFragment.RETURN_CATEGORY_NAME_KEY);
            Category category = new Category(categoryName);
            categoryViewModel.insertCategory(category);
            Toast.makeText(this, "New Category Created", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == CategoryEditDialogFragment.UPDATE_CATEGORY_REQUEST_CODE && resultCode == RESULT_OK){
            String categoryName = data.getStringExtra(CategoryEditActivity.CATEGORY_NAME_EXTRA);
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
        categoryViewModel.insertCategoryReturn(category, this);
    }

    @Override
    public void notify(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
