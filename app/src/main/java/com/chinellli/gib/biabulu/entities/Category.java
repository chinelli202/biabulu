package com.chinellli.gib.biabulu.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Category {

    public Category(){}

    public Category(String name){
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
