package com.chinellli.gib.biabulu;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;


public class SingleSongFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String POSITION_CANTIQUE = "pcantique";
    public static final String TARGETED_GROUP = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private List<LineBuilder> lineBuilderList;

    public SingleSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SingleSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleSongFragment newInstance(int param1) {
        SingleSongFragment fragment = new SingleSongFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_CANTIQUE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(POSITION_CANTIQUE);
            mParam2 = getArguments().getString(TARGETED_GROUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_song, container, false);

        TextView textView = rootView.findViewById(R.id.fragment_page_cantique);
        Bundle args = getArguments();
        int song_position = mParam1 + 1;
        //int position = 1;
        String songTitle = "song_" + song_position;
        //loading resource
        StringBuilder songText = new StringBuilder();
        lineBuilderList = new ArrayList<>();
        try {
            Resources res = getResources();
            InputStream istream = res.openRawResource(res.getIdentifier(songTitle, "raw", "com.chinellli.gib.biabulu"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            String line;
            int indexPos = 0;
            while ((line = reader.readLine()) != null) {
                StringBuilder builder = new StringBuilder(line.trim());
                processLine(builder, indexPos);
                indexPos += builder.length()+1;
                songText.append(builder.toString() + "\n");
            }
        } catch (IOException e) {
            songText.append("File Not Found");
        }

        SpannableString spstr = new SpannableString(songText);
        for(int i = 0; i < lineBuilderList.size(); i++){
            lineBuilderList.get(i).buildLine(spstr);
        }
        textView.setText(spstr);
        // textView.setText(Html.fromHtml(chanson));

        /*WebView webView = rootView.findViewById(R.id.song_webview);
        Bundle args = getArguments();
        int song_position = mParam1+1;
        //int position = 1;
        String songTitle = "song_"+song_position+".html";
        webView.loadUrl("file:///android_asset/"+songTitle);*/

        return rootView;
    }

    private void processLine(StringBuilder sb, int cursor) {
        //check start of line -> switch starting tags
        if (sb.indexOf("<h>") == 0) {
            LineSpanner spanner = new LineSpanner() {
                @Override
                public void spanSong(SpannableString spannable, int begin, int end) {
                    spannable.setSpan(new
                            StyleSpan(BOLD), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(
                            new RelativeSizeSpan(1.2f),
                            begin, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            };
            applySpanner(sb,cursor,spanner,"<h>");
        }
        if (sb.indexOf("<t>") == 0) {
            LineSpanner spanner = new LineSpanner() {
                @Override
                public void spanSong(SpannableString spannable, int begin, int end) {
                    spannable.setSpan(new VerticalAscentMarginSpan(100),begin,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new
                            StyleSpan(ITALIC), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            };
            applySpanner(sb,cursor,spanner,"<t>");
        }
        if (sb.indexOf("<v>") == 0) {
            LineSpanner spanner = new LineSpanner() {
                @Override
                public void spanSong(SpannableString spannable, int begin, int end) {
                    LeadingMarginSpan span = new LeadingMarginSpan.Standard(200);
                    spannable.setSpan(span,begin,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new VerticalMarginSpan(100),begin,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            };
            applySpanner(sb,cursor,spanner,"<v>");
        }
        if (sb.indexOf("<c>") == 0) {
            LineSpanner spanner = new LineSpanner() {
                @Override

                public void spanSong(SpannableString spannable, int begin, int end) {
                    LeadingMarginSpan span = new LeadingMarginSpan.Standard(200);
                    spannable.setSpan(span,begin,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new
                            StyleSpan(ITALIC), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            };
            applySpanner(sb,cursor,spanner,"<c>");
        }
        if (sb.indexOf("<cl1>") == 0) {
            LineSpanner spanner = new LineSpanner() {
                @Override
                public void spanSong(SpannableString spannable, int begin, int end) {
                    spannable.setSpan(new VerticalAscentMarginSpan(100),begin,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    LeadingMarginSpan span = new LeadingMarginSpan.Standard(200);
                    spannable.setSpan(span,begin,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new StyleSpan(ITALIC), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            };
            applySpanner(sb,cursor,spanner,"<cl1>");
        }
        if (sb.indexOf("<a>") == 0) {
            LineSpanner spanner = new LineSpanner() {
                @Override
                public void spanSong(SpannableString spannable, int begin, int end) {
                    spannable.setSpan(new VerticalAscentMarginSpan(100),begin,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new
                            StyleSpan(ITALIC), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            };
            applySpanner(sb,cursor,spanner,"<a>");
        }
    }

    private void applySpanner(StringBuilder sb, int cursor, LineSpanner spanner, String tag){
        sb.delete(0, tag.length());
        sb.delete(sb.length() - (tag.length()+1), sb.length());
        if(sb.length() > 0){
            LineBuilder builder = new LineBuilder(cursor, cursor+sb.length());
            System.out.println("Just set new lineBuilder with begin = "+cursor+" and end = "+(sb.length()-1));
            builder.setLineSpanner(spanner);
            lineBuilderList.add(builder);
        }
    }
}