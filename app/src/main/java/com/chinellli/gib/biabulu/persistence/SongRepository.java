package com.chinellli.gib.biabulu.persistence;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import com.chinellli.gib.biabulu.CategoryActionListener;
import com.chinellli.gib.biabulu.SimpleNotifier;
import com.chinellli.gib.biabulu.dao.CategoryDao;
import com.chinellli.gib.biabulu.dao.ListedSongDao;
import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;
import com.chinellli.gib.biabulu.util.QueryResult;

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

    /*public Long insertCategoryAndReturnIndex(Category category){
        return categoryDao.insertReturn(category);
    }*/


    public void insertCategory(Category category) {
        new AsyncTask<Category, Void, Void>() {

            @Override
            protected Void doInBackground(final Category... params) {
                categoryDao.insert(params[0]);
                return null;
            }

        }.execute(category);
    }

    public void insertCategoryReturndResult(Category category, Context context) {
        new InsertCategories(categoryDao, context).execute(category);
    }

    public void updateCategory(Category category) {
        new AsyncTask<Category, Void, Void>() {

            @Override
            protected Void doInBackground(final Category... params) {
                categoryDao.update(params[0]);
                return null;
            }

        }.execute(category);
    }

    public void deleteCategory(Category category) {
        new AsyncTask<Category, Void, Void>() {

            @Override
            protected Void doInBackground(final Category... params) {
                categoryDao.delete(params[0]);
                return null;
            }

        }.execute(category);
    }

    public Category findCategory(Category category) {
        return categoryDao.find(category.getId());
    }

    public LiveData<List<Category>> findAllCategories() {
        return categoryDao.findAll();
    }

    public List<ListedSong> findAllSongsInCategory(int catId) {
        return categoryDao.findAllSongsInCategory(catId);
    }

    public Category findCategoryById(int id) {
        return categoryDao.find(id);
    }

    public void insertListedSong(ListedSong listedSong, Context context) {
        /*new AsyncTask<ListedSong, Void, Void>() {

            @Override
            protected Void doInBackground(final ListedSong... params) {
                listedSongDao.insert(params[0]);
                return null;
            }

        }.execute(listedSong);*/
        System.out.println("inside insert listed song");
        new InsertListedSong(context).execute(listedSong);
    }

    public void insertListedSongInNewCategory(ListedSong listedSong, Context context){
        new InsertSongToNewCategory(context).execute(listedSong);
    }

    public void deleteListedSong(ListedSong listedSong) {
        new AsyncTask<ListedSong, Void, Void>() {

            @Override
            protected Void doInBackground(final ListedSong... params) {
                listedSongDao.delete(params[0]);
                return null;
            }

        }.execute(listedSong);
    }

    public List<ListedSong> findAllListedSong() {
        return listedSongDao.findAll();
    }

    public List<Category> findAllCategoriesWithoutSong(int num) {
        return categoryDao.getAvailableCategories(num);
    }

    public ListedSong findListedSong(ListedSong listedSong) {
        return listedSongDao.find(listedSong.getId());
    }

    private class InsertCategories extends AsyncTask<Category, Void, Boolean>{

        private CategoryDao categoryDao;
        private Context context;


        public InsertCategories(CategoryDao categoryDao, Context context) {
            this.categoryDao = categoryDao;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Category... categories) {
            //QueryResult<Category> insertResult = new QueryResult<>();
            Boolean dbQuerySuccess = true;
            try {
                long id = categoryDao.insertReturn(categories[0]);

            } catch (SQLiteConstraintException exception) {
                dbQuerySuccess = false;
            }
            return dbQuerySuccess;
        }

        @Override
        protected void onPostExecute(Boolean querySuccess){
            try{
                CategoryActionListener listener = (CategoryActionListener) context;
                if(querySuccess){
                    listener.notify("Categorie correctement enregistrée");
                }
                else
                    listener.notify("une catégorie du même nom existe déjà");
            }
            catch (ClassCastException exception){
                System.out.println("context not implenting categoryActionListener");
            }
        }
        //notify ui of changes
    }

    private class InsertListedSong extends AsyncTask<ListedSong, Void, String>{


        private Context context;

        public InsertListedSong(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(ListedSong... listedSongs) {

            String message;
            try{
                listedSongDao.insertWithFail(listedSongs[0]);
                message = "la chanson a ete ajoutee correctement";
                System.out.println("just added song to list");
            }
            catch (SQLiteConstraintException ex){
                message = "la chanson a déja été ajoutée à cette liste";
            }

            return message;
        }

        @Override
        protected void onPostExecute(String message){
            try{
                SimpleNotifier listener = (SimpleNotifier)context;
                listener.notify(message);
                System.out.println("class context : " + context.getClass());
            }
            catch (ClassCastException e){
                new RuntimeException("Attention, le contexte doit implementer SimpleNotifier");
                System.out.println("context not implementing SimpleNotifier");
            }
        }
    }

    private class InsertSongToNewCategory extends AsyncTask<ListedSong, Void, String>{

        private Context context;

        public InsertSongToNewCategory(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(ListedSong... listedSongs) {

            //perform insert
            String message;
            try {

                int id = (int)categoryDao.insertReturn(listedSongs[0].getCategory());
                listedSongs[0].setCatId(id);
                listedSongDao.insertWithFail(listedSongs[0]);
                message = "chanson correctement ajoutee a : "+ listedSongs[0].getCategory().getName();
            }
            catch (SQLiteConstraintException e){
                message = "Une categorie du meme nom existe deja";
            }
            return message;
        }

        @Override
        public void onPostExecute(String message){
            try{
                SimpleNotifier listener = (SimpleNotifier)context;
                listener.notify(message);
                System.out.println("class context : " + context.getClass());
            }
            catch (ClassCastException e){
                new RuntimeException("Attention, le contexte doit implementer SimpleNotifier");
                System.out.println("context not implementing SimpleNotifier");
            }
        }
    }
}
    //Asynchronous task for the insertion of categories. updates the queryinsert object


