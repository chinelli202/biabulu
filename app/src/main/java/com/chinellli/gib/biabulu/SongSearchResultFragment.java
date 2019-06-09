package com.chinellli.gib.biabulu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SongSearchResultFragment extends Fragment {

    private final String NO_RESULT_MESSAGE = "Aucun résultat trouvé pour ";
    private CardView cardView;
    private SearchView searchView;
    private ArrayAdapter<String> suggestionsAdapter;
    List<String> suggestionsList = new ArrayList<>();
    ListView listView;
    TextView textView;
    private OnSearchViewActionListener searchViewActionListener;

    public SongSearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this Fragment View Layout
        System.out.println("Inflating View Layout");
        View rootView =  inflater.inflate(R.layout.fragment_content_test, container, false);
        cardView = rootView.findViewById(R.id.searchViewCard);
        cardView.setVisibility(View.GONE);
        textView = rootView.findViewById(R.id.no_result_text);
        textView.setVisibility(View.GONE);

        //listAdapter
        suggestionsAdapter = new ArrayAdapter<>(this.getActivity(),R.layout.search_result_list_item,suggestionsList);

        //listView
        listView = rootView.findViewById(R.id.suggestionSearchList);
        listView.setAdapter(suggestionsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String content = ((TextView)view).getText().toString();
                //logic: parse text and open songpage activitty with position = song number
                //content = content.replaceAll("\\s","");
                String tokens[] = content.split("[.]");
                int pos = Integer.valueOf(tokens[0])-1;
                Intent intent = new Intent(getActivity(),SongCollectionActivity.class);
                intent.putExtra(SingleSongFragment.POSITION_CANTIQUE,pos);
                startActivity(intent);
            }
        });
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //inflation
        inflater.inflate(R.menu.menu_fragment,menu);
        //collapse event listener
        MenuItem.OnActionExpandListener listener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                searchViewActionListener.onSearchWidgetGetFocus();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                searchViewActionListener.onSearchWidgetLoseFocus();
                suggestionsAdapter.clear();
                cardView.setVisibility(View.GONE);
                return true;
            }
        };

        //
        MenuItem item = menu.findItem(R.id.app_bar_search2);
        item.setOnActionExpandListener(listener);
        searchView =  (SearchView)item.getActionView();

        //searchView event handlers
        searchView.setQueryHint("Ex. 100, M'aye kane joe...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() < 1){
                    cardView.setVisibility(View.GONE);
                    return false;
                }
                //fill up the adapter with suggestions

                suggestionsAdapter.clear();
                List<String> response = getResponse(query);
                if(response.isEmpty()){
                    String msg = NO_RESULT_MESSAGE + "\"" + query + "\"";
                    textView.setText(msg);
                    textView.setVisibility(View.VISIBLE);
                }
                else{
                    textView.setVisibility(View.GONE);
                    suggestionsAdapter.addAll(getResponse(query));
                }
                cardView.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            searchViewActionListener = (OnSearchViewActionListener) getActivity();
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement OnSearchViewActionListener");
        }
    }
    public List<String> getResponse(String query) {
        InputStream istream = getResources().openRawResource(R.raw.songlist);
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
        String line;
        List<String> responseList = new ArrayList<>();
        List<Pair<Integer, String>> res = new ArrayList<>();
        query = query.toLowerCase();
        try{
            while ((line = reader.readLine()) != null) {
                String sanitize = line.toLowerCase();
                //replace special characters
                StringBuilder builder = new StringBuilder(sanitize);
                for(int x = 0; x < builder.length(); x++){
                    char ch = builder.charAt(x);
                    switch (ch){
                        case 'ñ' : builder.setCharAt(x,'n');
                        case 'ô' : builder.setCharAt(x,'o');
                        case 'é' : builder.setCharAt(x,'e');
                        case 'è' : builder.setCharAt(x,'e');
                        case 'à' : builder.setCharAt(x,'a');
                    }
                }
                //builder.delete(0, builder.indexOf(".")+2);
                sanitize = builder.toString();
                if (sanitize.indexOf(query) != -1) {
                    res.add(new Pair<>(sanitize.indexOf(query), line));
                    System.out.println("New match : " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!res.isEmpty()) {
            //implementing the sorting algorithm myself; bubble sort is the choice here
            for (int i = 0; i < res.size(); i++) {
                for (int j = 0; j < res.size() - i - 1; j++) {
                    if (res.get(j).first > res.get(j + 1).first) {
                        Pair<Integer, String> ech = res.get(j);
                        res.set(j, res.get(j + 1));
                        res.set(j + 1, ech);
                    }
                }
            }
            for (int i = 0; i < res.size(); i++){
                responseList.add(res.get(i).second);
                System.out.println(res.get(i).second);
            }
        }
        return responseList;
    }

}
