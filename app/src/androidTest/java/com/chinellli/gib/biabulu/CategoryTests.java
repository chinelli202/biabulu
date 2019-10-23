package com.chinellli.gib.biabulu;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CategoryTests {

    private CategoryDao catDao;
    private SongRoomDatabase songRoomDatabase;
    //state variables
 @Before
 public void before(){
     Context appContext = InstrumentationRegistry.getTargetContext();
     songRoomDatabase = Room.inMemoryDatabaseBuilder(appContext, SongRoomDatabase.class).allowMainThreadQueries().build();
     catDao = songRoomDatabase.categoryDao();
 }

 @After
    public void after(){
     songRoomDatabase.clearAllTables();
     songRoomDatabase.close();
 }

    @Test
    public void testInsert() {
        Category cat = new Category();
        cat.setName("Jean Jacques");
        catDao.insert(cat);
        //load all categories, make sure only category in there has name == name
        List<Category> list = catDao.findAllRaw();

        assertTrue(!list.isEmpty());
        Category category = list.get(0);
        assertEquals(category.getName(),cat.getName());
    }

    @Test
    public void testFailInsert(){
        Category cat = new Category();
        cat.setName("Jean Jacques");
        catDao.insert(cat);
        //load all categories, make sure only category in there has name == name
        List<Category> list = catDao.findAllRaw();

        assertTrue(!list.isEmpty());
        Category category = list.get(0);
        Category cat2 = new Category();
        cat.setName("Jean Jacques");
        long id = 0;
        try{
            id = catDao.insertReturn(cat2);
        }
        catch(SQLiteConstraintException exception){
            System.out.println("insert failed with constraint exception");
            assertEquals(id,0);
        }
    }
    @Test
    public void testUpdate(){
     //create, insert, load, update, load, test
        Category category = new Category();
        category.setName("Marie");
        catDao.insert(category);
        List<Category> list = catDao.findAll().getValue();

        assertTrue(!list.isEmpty());
        Category upCat = list.get(0);
        assertEquals(category.getName(),upCat.getName());
        upCat.setName("Marie Madeleine");
        catDao.update(upCat);
        list = catDao.findAll().getValue();
        assertEquals(list.get(0).getName(),upCat.getName());
    }
    @Test
    public void testDelete(){
     //insert, load, delete, load, test, test delete cascade : make sure all songs in category have been deleted
        Category category = new Category();
        category.setName("Marie");
        catDao.insert(category);
        List<Category> list = catDao.findAll().getValue();

        assertTrue(!list.isEmpty());
        Category delCat = list.get(0);
        catDao.delete(delCat);
        list = catDao.findAll().getValue();
        assertTrue(list.isEmpty());
    }

    @Test
    public void findTest(){
     //create, insert, findAll, retrieve, get first Id, test find existing, test find non existent.
        Category cat = new Category();
        cat.setName("Jean Jacques");
        catDao.insert(cat);
        //load all categories, make sure only category in there has name == name
        List<Category> list = catDao.findAll().getValue();

        assertTrue(!list.isEmpty());
        Category category = list.get(0);
        Category queriedCat = catDao.find(category.getId());
        assertTrue(queriedCat!=null);
        Category unexistingQueriedCat = catDao.find(category.getId()+1);
        assertTrue(unexistingQueriedCat==null);
    }

    @Test
    public void findAllSongsInCategoryTest(){
     // fill in data. add 2 categories, add 4 songs to 1rst category, issue query test, assert 4 songs found
        Category cat1 = new Category("Sad");
        Category cat2 = new Category("Christmas");
        catDao.insert(cat1);
        catDao.insert(cat2);

        List<Category> allCat = catDao.findAll().getValue();
        assertTrue(!allCat.isEmpty());
        Category queriedCat = allCat.get(0);
        ListedSong song1 = new ListedSong(28,queriedCat);
        ListedSong song2 = new ListedSong(172,queriedCat);
        ListedSong song3 = new ListedSong(185,queriedCat);
        ListedSong song4 = new ListedSong(215,queriedCat);

       ListedSongDao lsDao = songRoomDatabase.listedSongDao();
       lsDao.insert(song1);
       lsDao.insert(song2);
       lsDao.insert(song3);
       lsDao.insert(song4);

       List<ListedSong> sadSongsInDb = catDao.findAllSongsInCategory(queriedCat.getId());
       assertEquals(sadSongsInDb.size(),4);

       //test delete cascade
        catDao.delete(queriedCat);
        //assert all songs in cat have been deleted as well
        sadSongsInDb = catDao.findAllSongsInCategory(queriedCat.getId());
        assertTrue(sadSongsInDb.isEmpty());
    }
}
