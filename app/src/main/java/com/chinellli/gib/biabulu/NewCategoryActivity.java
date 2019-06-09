package com.chinellli.gib.biabulu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class NewCategoryActivity extends AppCompatActivity {

    private TextView categoryTextView;
    public static final String CATEGORY_NAME_EXTRA = "NAME_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        categoryTextView = findViewById(R.id.editText);
    }

    public void onConfirmButtonClick(View view){
        if(categoryTextView.getText().length() != 0){
            Intent replyIntent = new Intent();
            if(TextUtils.isEmpty(categoryTextView.getText())){
                setResult(RESULT_CANCELED,replyIntent);
            }
            else{
                String catName = categoryTextView.getText().toString();
                replyIntent.putExtra(CATEGORY_NAME_EXTRA,catName);
                setResult(RESULT_OK,replyIntent);
            }
            finish();
        }
    }
}
