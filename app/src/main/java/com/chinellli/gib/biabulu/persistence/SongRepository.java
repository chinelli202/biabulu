package com.chinellli.gib.biabulu.persistence;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.chinellli.gib.biabulu.dao.CategoryDao;
import com.chinellli.gib.biabulu.dao.ListedSongDao;
import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;

import java.util.List;

public class SongRepository {
    private SongRoomDatabase songRoomDatabase;
    private CategoryDao categoryDao;
    private ListedSongDao listedSongDao;

    public SongRepository(Application application) {
        songRoomDatabase = SongRoomDatabase.getDatabase(application);
        categoryDao = songRoomDatabase.categoryDao();
        listedSongDao = songRoomDatabase.listedSongDao();
    }

    public void insertCategory(Category category){
         new AsyncTask<Category,Void,Void>(){

            @Override
            protected Void doInBackground(final Category... params){
                categoryDao.insert(params[0]);
                return null;
            }

        }.execute(category);
    }

    public void updateCategory(Category category){
        new AsyncTask<Category,Void,Void>(){

            @Override
            protected Void doInBackground(final Category... params){
                categoryDao.update(params[0]);
                return null;
            }

        }.execute(category);
    }

    public void deleteCategory(Category category){
        new AsyncTask<Category,Void,Void>(){

            @Override
            protected Void doInBackground(final Category... params){
                categoryDao.delete(params[0]);
                return null;
            }

        }.execute(category);
    }

    public Category findCategory(Category category){
        return categoryDao.find(category.getId());
    }

    public LiveData<List<Category>> findAllCategories(){
        return categoryDao.findAll();
    }

    public List<ListedSong> findAllSongsInCategory(int catId){
        return categoryDao.findAllSongsInCategory(catId);
    }

    public void insertListedSong(ListedSong listedSong){
        new AsyncTask<ListedSong,Void,Void>(){

            @Override
            protected Void doInBackground(final ListedSong... params){
                listedSongDao.insert(params[0]);
                return null;
            }

        }.execute(listedSong);
    }

    public void deleteListedSong(ListedSong listedSong){
        new AsyncTask<ListedSong,Void,Void>(){

            @Override
            protected Void doInBackground(final ListedSong... params){
                listedSongDao.delete(params[0]);
                return null;
            }

        }.execute(listedSong);
    }

    public List<ListedSong> findAllListedSong(){
        return listedSongDao.findAll();
    }
    public List<Category> findAllCategoriesWithoutSong(int num){return categoryDao.getAvailableCategories(num);}
    public ListedSong findListedSong(ListedSong listedSong){
        return listedSongDao.find(listedSong.getId());
    }

}
