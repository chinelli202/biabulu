package com.chinellli.gib.biabulu;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;
import com.chinellli.gib.biabulu.persistence.SongRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupViewModel extends AndroidViewModel{
    private SongRepository songRepository;
    private List<String> categorySongList;
    private List<String> pagedSongList;
    private List<ListedSong> listedSongsList;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId){
        this.catId = catId;
    }

    private int catId;
    private final int max = 300;
    private final int pageSize = 50;
    private String[] listSource;
    public GroupViewModel(Application application){
        super(application);
        songRepository = new SongRepository(application);
        categorySongList = new ArrayList<>();
        pagedSongList = new ArrayList<>();
        listedSongsList = new ArrayList<>();
        loadAllSongsList();
    }

    public List<String> getSongs(int catId) {
        //return categorys song list if catId is positive, paged song list if id is negative.
        if(catId > 0){
            setCategorySongList(catId);
            return categorySongList;
        }
        else{
            setPagedSongList(-catId);
            return pagedSongList;
        }
    }

    private void setPagedSongList(int page){
        System.out.println("loading paged songs from viewModel for page : "+page);
        int start = ((page-1) * 50) + 1;
        pagedSongList.clear();
        for(int i = start; i <= start + 49; i++){
            if(listSource[i-1]!=null){
             //   System.out.println("effectively loading songs");
                pagedSongList.add(listSource[i-1]);
            }
        }
    }

    private void loadAllSongsList(){
        listSource = new String[max];
        try{
            Resources res = getApplication().getResources();
            InputStream istream = res.openRawResource(R.raw.songlist);
            BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            String line;
            int i = 0;
            while((line = reader.readLine())!=null && i < max){
                listSource[i] = line;
                System.out.println("listsource[i] now is " + i);
                i++;
            }
        }
        catch(IOException e){
            //log exception
            System.out.println(e.getMessage());
        }
    }

    public List<String> getListSource(){
        return Arrays.asList(listSource);
    }



    public void setCategorySongList(int page){
        System.out.println("loading category songs from viewModel for category : "+page);
        setCatId(page);
        listedSongsList = songRepository.findAllSongsInCategory(page);
        List<String> allSongTitles = new ArrayList<>();
        //read songTitles file
        try{
            Resources res = getApplication().getResources();
            InputStream istream = res.openRawResource(R.raw.songlist);
            BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            String line;
            int i = 0;
            while((line = reader.readLine())!=null){
                allSongTitles.add(i,line);//listSource[i] = line;
                i++;
            }
        }
        catch(IOException e){

        }
        for(int j = 0; j < listedSongsList.size(); j++){
            int songNumber = listedSongsList.get(j).getNum();
            categorySongList.add(allSongTitles.get(songNumber-1));
        }
        //return songRepository.findAllSongsInCategory(catId);
    }

    public void deleteListedSong(int position){
        ListedSong song = listedSongsList.get(position);
        songRepository.deleteListedSong(song);
    }

    /*public void addSongToCategory(int songNumber){
        addSongToCategory(songNumber, catId);
    }*/

    public void addSongToCategory(int songNumber, int catId, Context context){
        Category category = new Category();
        category.setId(catId);
        ListedSong toAdd = new ListedSong(songNumber,category);
        songRepository.insertListedSong(toAdd, context);
    }

    public void addSongToNewCategory (int songNumber, String catName, Context context){
        //create new category and insert it, if everything goes right, insert song into that category
        Category cat = new Category(catName);
        ListedSong song = new ListedSong(songNumber,cat);
        songRepository.insertListedSongInNewCategory(song,context);
    }

    /*public void addSongToNewCategory(int songNumber, String catName) {
        new AddSongToNewCategoryTask(songNumber, catName).execute();
    }*/
    //fresh
    /*private class AddSongToNewCategoryTask extends AsyncTask<Void, Void, Void>{

        private int number;
        private String catName;

        public AddSongToNewCategoryTask(int number, String catName){
            this.number = number;
            this.catName = catName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Category cat = new Category();
            cat.setName(catName);
            long id = songRepository.insertCategoryAndReturnIndex(cat);
            if(id != 0){
                cat.setId((int)id);
                ListedSong listed = new ListedSong(number,cat);
                songRepository.insertListedSong(listed);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void object){
            Toast.makeText(getApplication(),"Song Successfully Added to "+ catName, Toast.LENGTH_SHORT);
        }
    }*/

    public List<ListedSong> getListedSongsList(){
        return listedSongsList;
    }
}
