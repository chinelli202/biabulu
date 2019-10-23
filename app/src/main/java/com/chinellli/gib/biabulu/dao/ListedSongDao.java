package com.chinellli.gib.biabulu.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.chinellli.gib.biabulu.entities.ListedSong;

import java.util.List;

@Dao
public interface ListedSongDao {

    @Insert
     void insert(ListedSong song);

    @Insert (onConflict = OnConflictStrategy.FAIL)
    long insertWithFail(ListedSong song);

    @Delete
    public void delete(ListedSong song);

    //findAll
    @Query("SELECT * FROM listed_song")
    public List<ListedSong> findAll();

    // find a listed song. Wether or not the song is in a category
    @Query("SELECT * FROM listed_song WHERE id = :id")
    public ListedSong find(int id);
}
