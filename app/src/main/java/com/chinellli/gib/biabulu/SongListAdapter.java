package com.chinellli.gib.biabulu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

//Adapter element types are string, corresponding to song titles. Ex : 53. M'aye kane joe yesus.
//main job : get the views right, get the event dispatching right.
public class SongListAdapter extends ArrayAdapter<String> {

    private int songsGroupType;
    public static final int SONG_GROUP_CATEGORY_TYPE = 0;
    public static final int SONG_GROUP_WHOLE_LIST_TYPE = 1;
    //private Button button;
    //parameter for choosing between cat list and whole song list.
    public SongListAdapter(@NonNull Context context, int resource, int songsGroupType) {
        super(context, resource);
        this.songsGroupType = songsGroupType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //inflate view, then return view
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.song_item,parent, false);
        }

        TextView textView = convertView.findViewById(R.id.song_item_text);
        Button button = convertView.findViewById(R.id.song_remove_button);
        button.setTag(position);
        textView.setText(getItem(position));
        if(songsGroupType == this.SONG_GROUP_CATEGORY_TYPE)
            adaptForCategory(button);
        else
            adaptForWholeList(button);

        return convertView;
    }


    //category adaptation : button icon must be changed to delete icon. on click event loads confirm delete dialog.
    //whole list adaptation : button icon changed to add (plus sign of add to list sign). onclick event loads addToListDialogFragment.


    private void adaptForCategory(Button button){
        button.setBackgroundResource(R.drawable.ic_remove_circle_outline_black_24dp);
        button.setOnClickListener(view -> {
            //pass delete event
            ((SongActionListener)getContext()).removeSongFromCategory((int)button.getTag());
        });
    }

    private void adaptForWholeList(Button button){
        button.setBackgroundResource(R.drawable.ic_add_circle_outline_black_24dp);
        button.setOnClickListener(view -> {
            //strip song number
            int position = (int)button.getTag();

            String title = getItem(position);
            String tokens[] = title.split("[.]");
            int songNumber = Integer.valueOf(tokens[0])-1;
            ((SongActionListener)getContext()).addSongToCategory(songNumber);
        });
    }

    private void onRemoveSongFromCategoryEventHandler(){}

    private void onAddSongToCategoryEventHandler(){}
}
