package com.chinellli.gib.biabulu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.chinellli.gib.biabulu.entities.Category;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<Category> {

    private LayoutInflater inflater;
    private CategoryActionListener categoryActionListener;
    private CategoryViewModel categoryViewModel;
    private List<Category> mCategoryList;
    private Context context;
    public CategoryListAdapter(Context context, List<Category> categoryArrayList) {
        super(context, 0, categoryArrayList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        mCategoryList = categoryArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        Category cat = getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.category_item,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.category_item_line_text);
        Button button = convertView.findViewById(R.id.category_delete_button);

        textView.setText(cat.getName());
        button.setTag(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view,(int)button.getTag());
                System.out.println("Clicked button");
            }
        });
        return convertView;
    }

    /*@Override
    public int getCount(){
        if(mCategoryList == null)
            return 0;
        else return mCategoryList.size();
    }*/

    public void showPopup(View view, int position){
        PopupMenu popup = new PopupMenu(this.getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.delete_category_item :

                        //dialog confirm
                        AlertDialog dialog = new AlertDialog.Builder(view.getContext()).create();
                        dialog.setTitle("Confirmation");
                        dialog.setMessage("Voulez-vous vraiment supprimer "+getItem(position).getName());
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //categoryViewModel.deleteCategory(getItem(position));

                                        //make sure the calling context implements the category Action Listener

                                        ((CategoryActionListener)getContext()).deleteAction(position);
                                    }
                                });
                        dialog.show();
                        return true;
                    case R.id.rename_category_item :
                        //Intent intent = new Intent(getContext(), CategoryEditActivity.class);
                        //startActivityForResult(intent,NEW_CATEGORY_ACTIVITY_REQUEST_CODE);

                        Bundle bundle = new Bundle();
                        bundle.putInt(CategoryEditDialogFragment.CATEGORY_REQUEST_KEY,CategoryEditDialogFragment.UPDATE_CATEGORY_REQUEST_CODE);
                        bundle.putCharSequence(CategoryEditDialogFragment.UPDATE_CATEGORY_ARGUMENT_KEY,getItem(position).getName());
                        bundle.putInt(CategoryEditDialogFragment.UPDATE_CATEGORY_POSITION_KEY,position);

                        CategoryEditDialogFragment categoryEditDialogFragment = new CategoryEditDialogFragment();
                        categoryEditDialogFragment.setArguments(bundle);
                        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                        categoryEditDialogFragment.show(manager,"update category dialog");
                        return true;
                        default: return false;
                }

            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_categories_more,popup.getMenu());
        popup.show();
    }

    public void setCategoryViewModel(CategoryViewModel categoryViewModel){
        this.categoryViewModel = categoryViewModel;
    }
}

