package com.chinellli.gib.biabulu;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Splashscreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        //textview = findViewById(R.id.textView_splashscreen);

        //spanned text labing
        //String str = "This is some new string line\n\nand this is another one.\nhow does this play out?";
        //SpannableString st = new SpannableString(str);
        //st.setSpan(new VerticalMarginSpan(100),29,54,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //st.setSpan(new VerticalMarginSpan(50),29,29,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //st.setSpan(new VerticalAscentMarginSpan(100),29,29,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //textview.setText(st);

        //hide navbar
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //Create an intent that will create the main activity
                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                Splashscreen.this.startActivity(intent);
                Splashscreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    /*public void onclick(View view){
        DialogFragment dialogFragment = new CategoryEditDialogFragment();
        dialogFragment.show(getFragmentManager(),"newCat");
    }*/
}
