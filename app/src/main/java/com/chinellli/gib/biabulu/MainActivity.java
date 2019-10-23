package com.chinellli.gib.biabulu;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnSearchViewActionListener {
    public static final String CATEGORY="com.chinelli.gib.biabulu.songCategory";
    ConstraintLayout buttonsLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonsLayout = findViewById(R.id.buttons_layout);
    }

    public void onCategorySelected(View view){
        Intent intent = new Intent(this, GroupActivity.class);
        String category = ((Button)view).getText().toString();
        category = category.replaceAll("\\s","");
        String tokens[] = category.split("-");
        int pageSize = 50;
        int page = Integer.valueOf(tokens[1])/pageSize;
        page = -page;
        intent.putExtra(GroupActivity.GROUP_ID,page);
        startActivity(intent);
    }

    public void onListButtonClicked(View view){
        Intent intent = new Intent(this,CategoriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearchWidgetGetFocus() {
        buttonsLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSearchWidgetLoseFocus() {
        buttonsLayout.setVisibility(View.VISIBLE);
    }
}
