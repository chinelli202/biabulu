package com.chinellli.gib.biabulu;

public interface SongActionListener {
    void removeSongFromCategory(int songNumber, int catId);
    void addSongToCategory(int songNumber, int catId);
    void addSongToNewCategory(int songNumber, String catName);
}
