package com.chinellli.gib.biabulu;

import android.view.View;

public interface CategoryActionListener {
    void deleteAction(int position);
    void updateAction(int position, String name);
    void createAction(String name);
    void notify(String message);
}