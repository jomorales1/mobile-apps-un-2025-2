package com.example.reto3;

import android.annotation.SuppressLint;
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
        if (menuItem.getItemId() == R.id.new_game_item) {
            this.activity.startNewGame();
            return true;
        } else if (menuItem.getItemId() == R.id.difficulty_level_item) {
            new DifficultyDialogFragment(this.activity, this.activity.getGame())
                    .show(this.activity.getSupportFragmentManager(), "DifficultyDialog");
            return true;
        } else if (menuItem.getItemId() == R.id.quit_item) {
            new QuitDialogFragment()
                    .show(this.activity.getSupportFragmentManager(), "QuitDialog");
            return true;
        } else if (menuItem.getItemId() == R.id.about_item) {
            new AboutDialogFragment().show(activity.getSupportFragmentManager(), "AboutDialog");
            return true;
        } else if (menuItem.getItemId() == R.id.reset_item) {
            new ResetDialogFragment().show(activity.getSupportFragmentManager(), "ResetDialog");
            return true;
        }
        return false;
    }
}
