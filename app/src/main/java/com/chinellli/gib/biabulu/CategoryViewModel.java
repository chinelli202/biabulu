package com.chinellli.gib.biabulu;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.chinellli.gib.biabulu.entities.Category;
import com.chinellli.gib.biabulu.entities.ListedSong;
import com.chinellli.gib.biabulu.persistence.SongRepository;
import com.chinellli.gib.biabulu.util.QueryResult;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private SongRepository songRepository;
    //private LiveData<List<Category>> categoryList;
    public CategoryViewModel(Application application){
        super(application);
        songRepository = new SongRepository(application);
        //categoryList = songRepository.findAllCategories();
    }

    public void insertCategory(Category category){
        songRepository.insertCategory(category);
    }
    public void updateCategory(Category category){
        songRepository.updateCategory(category);
    }
    public void deleteCategory(Category category){songRepository.deleteCategory(category);}
    public Category findCategory(int id){
        return songRepository.findCategoryById(id);
    }

    public void insertCategoryReturn(Category category, Context context){
        songRepository.insertCategoryReturndResult(category, context);
    }

    public List<Category> findMatchingCategory(int num){return songRepository.findAllCategoriesWithoutSong(num);}
    public LiveData<List<Category>> findAllCategories(){
        return songRepository.findAllCategories();//categoryList;
    }
    public void insertListedSong(ListedSong listedSong, Context context){songRepository.insertListedSong(listedSong, context);}
}
