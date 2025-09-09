package com.example.reto3;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

public class MyMenuProvider implements MenuProvider {

    private final MainActivity activity;

    public MyMenuProvider(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.my_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        activity.startNewGame();
        return true;
    }
}
