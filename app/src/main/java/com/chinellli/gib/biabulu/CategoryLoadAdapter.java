package com.chinellli.gib.biabulu;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinellli.gib.biabulu.entities.Category;

import java.util.List;


public class CategoryLoadAdapter extends ArrayAdapter<Category> {
    private List<Category> categories;
    private LayoutInflater inflater;
    public CategoryLoadAdapter(Context context, List<Category> categoryArrayList){
        super(context, 0, categoryArrayList);
        inflater = LayoutInflater.from(context);
        categories = categoryArrayList;
    }

    @Override
    public int getCount(){
        if(categories == null)
            return 0;
        else
            return categories.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Category cat = categories.get(position);//getItem(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.category_table_select_layout,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.catTextView);
        textView.setText(cat.getName());
        return convertView;
    }

    public void doSetup(List<Category> list){
        categories = list;
        notifyDataSetChanged();
    }
}
