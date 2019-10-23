package com.chinellli.gib.biabulu.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(Category category);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insertReturn(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    //find/select, or whatever you choose to call it
    @Query("SELECT * FROM Category WHERE id = :catId")
    Category find(int catId);

    @Query("SELECT * FROM Category WHERE name = :catName")
    Category findByName(int catName);

    @Query("SELECT * FROM Category") //load all categories
    LiveData<List<Category>> findAll();

    @Query("SELECT * FROM Category")
    List<Category> findAllRaw();

    @Query("SELECT * FROM Category") //load all categories
    List<Category> findAllCats();

    //load all songs stored in a category
    @Query("SELECT * FROM listed_song WHERE cat_id = :catId")
    List<ListedSong> findAllSongsInCategory(int catId);

   // @Query("SELECT category.id, category.name from listed_song INNER JOIN Category on listed_song.cat_id = " +
           // "category.id where listed_song.num!= :songNum group by listed_song.cat_id")
    @Query("select * from Category where category.id not in (select listed_song.cat_id from listed_song where listed_song.num = :songNum )")
    List<Category> getAvailableCategories(int songNum);

    //all categories diff categories in which song is registered
    // select * from category where category.id not in (select listed_song.cat_id from listed_song where listed_song.num = :songNum)
}
