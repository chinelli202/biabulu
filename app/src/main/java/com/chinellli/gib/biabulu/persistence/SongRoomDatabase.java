package com.chinellli.gib.biabulu.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.chinellli.gib.biabulu.dao.CategoryDao;
import com.chinellli.gib.biabulu.dao.ListedSongDao;
import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;

@Database(entities={Category.class,ListedSong.class}, version=1)
public abstract class SongRoomDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract ListedSongDao listedSongDao();
    public static volatile SongRoomDatabase INSTANCE;
    static SongRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SongRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SongRoomDatabase.class, "song_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
