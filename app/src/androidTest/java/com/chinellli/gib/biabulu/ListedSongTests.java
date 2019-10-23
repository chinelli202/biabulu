package com.chinellli.gib.biabulu;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.chinellli.gib.biabulu.dao.CategoryDao;
import com.chinellli.gib.biabulu.dao.ListedSongDao;
import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;
import com.chinellli.gib.biabulu.persistence.SongRoomDatabase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ListedSongTests {
    CategoryDao catDao;
    ListedSongDao listedSongDao;
    static SongRoomDatabase songRoomDatabase;
    static Context appContext;

    @Before
    public void before(){
        catDao = songRoomDatabase.categoryDao();
        listedSongDao = songRoomDatabase.listedSongDao();
    }

     @After
    public void after(){
        songRoomDatabase.clearAllTables();
     }

     @BeforeClass
    public static void beforeClass(){
         appContext = InstrumentationRegistry.getTargetContext();
         songRoomDatabase = Room.inMemoryDatabaseBuilder(appContext, SongRoomDatabase.class).allowMainThreadQueries().build();
     }

     @AfterClass
    public static void afterClass(){
        songRoomDatabase.close();
     }

     @Test
    public void insertTest(){
         //create + insert category, create insert categorized song, findAll, test equality on song num.
         Category category = new Category("Deuils");
         catDao.insert(category);
         List<Category> listCat = catDao.findAllCats();
         assertTrue(!listCat.isEmpty());
         assertEquals(listCat.get(0).getName(),category.getName());

         Category inserted = listCat.get(0);
         ListedSong mySong = new ListedSong(185,inserted);
         listedSongDao.insert(mySong);

         List<ListedSong> allSongs = listedSongDao.findAll();
         assertTrue(!allSongs.isEmpty());
         assertEquals(allSongs.get(0).getNum(),mySong.getNum());
     }

     @Test
     public void insertWithFailTest(){
         Category category = new Category("Deuils");
         catDao.insert(category);
         List<Category> listCat = catDao.findAllCats();
         assertTrue(!listCat.isEmpty());
         assertEquals(listCat.get(0).getName(),category.getName());
         Category inserted = listCat.get(0);
         ListedSong mySong = new ListedSong(185,inserted);
         listedSongDao.insert(mySong);

         long id;
         try{
             ListedSong song2 = new ListedSong(185,inserted);
             id = listedSongDao.insertWithFail(song2);
         }

         catch(SQLiteConstraintException e){
             System.out.println("db constraint violated");
         }




     }

     @Test
    public void deleteTest(){
        //insert, query, delete, test (query all + assert)
         Category category = new Category("Deuils");
         catDao.insert(category);
         List<Category> listCat = catDao.findAll().getValue();
         assertTrue(!listCat.isEmpty());
         assertEquals(listCat.get(0).getName(),category.getName());

         Category insertedCat = listCat.get(0);
         ListedSong mySong = new ListedSong(185,insertedCat);
         listedSongDao.insert(mySong);

         List<ListedSong> allListedSongs = listedSongDao.findAll();
         assertTrue(!allListedSongs.isEmpty());
         ListedSong toDelete = allListedSongs.get(0);
         listedSongDao.delete(toDelete);
         allListedSongs = listedSongDao.findAll();
         assertTrue(allListedSongs.isEmpty());
     }

    @Test
    public void findTest(){
        //insert, query, find, assert
        Category category = new Category("Deuils");
        catDao.insert(category);
        List<Category> listCat = catDao.findAll().getValue();
        assertTrue(!listCat.isEmpty());
        assertEquals(listCat.get(0).getName(),category.getName());

        Category insertedCat = listCat.get(0);
        ListedSong mySong = new ListedSong(185,insertedCat);
        listedSongDao.insert(mySong);

        List<ListedSong> allListedSongs = listedSongDao.findAll();
        assertTrue(!allListedSongs.isEmpty());
        ListedSong queriedSong = allListedSongs.get(0);
        ListedSong searched = listedSongDao.find(queriedSong.getId());
        assertTrue(searched!=null);
        assertEquals(searched.getNum(),mySong.getNum());
    }

    @Test
    public void availableCategoriesTest(){
        //insert 3 new categories, lets say sad, christmas and familly
        //find all categories
        //add 2 songs in first, including targetted song
        //add 1 song in last 2,
        //perform query, assert on category names

        Category category = new Category("Sad");
        Category category2 = new Category("Christmas");
        Category category3 = new Category("Familly");
        catDao.insert(category);
        catDao.insert(category2);
        catDao.insert(category3);
        List<Category> list = catDao.findAllCats();
        assertTrue(list.size() == 3);
        //targetted song 49 : Joy to the world
        ListedSong mySong = new ListedSong(53,list.get(2));
        ListedSong mySong2 = new ListedSong(49,list.get(2));
        ListedSong mySong3 = new ListedSong(49,list.get(1));
        ListedSong mySong4 = new ListedSong(185,list.get(0));
        listedSongDao.insert(mySong);
        listedSongDao.insert(mySong2);
        listedSongDao.insert(mySong3);
        listedSongDao.insert(mySong4);
        List<Category> listWithoutTarget = catDao.getAvailableCategories(49);
        List<Category> listWithoutTarget2 = catDao.getAvailableCategories(33);
        System.out.println("size : "+listWithoutTarget.size());
        assertTrue(listWithoutTarget.size()==1);
        assertTrue(listWithoutTarget2.size()==3);
        assertTrue(listWithoutTarget.get(0).getName().equals(category.getName()));
    }
}
