package com.chinellli.gib.biabulu.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "listed_song",foreignKeys = @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "cat_id", onDelete = CASCADE))
public class ListedSong {

    public ListedSong(){}
    public ListedSong(int num, Category category){
        this.num = num;
        this.catId = category.getId();
    }
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int num;

    @NonNull
    @ColumnInfo(name = "cat_id")
    private int catId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public int getCatId() {
        return catId;
    }

    public void setNum(@NonNull int num) {
        this.num = num;
    }

    public void setCatId(@NonNull int catId) {
        this.catId = catId;
    }
}
