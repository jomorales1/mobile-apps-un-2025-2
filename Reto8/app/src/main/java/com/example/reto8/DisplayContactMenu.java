package com.example.reto8;

import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

public class DisplayContactMenu implements MenuProvider {

    private final DisplayContact activity;

    public DisplayContactMenu(DisplayContact activity) {
        this.activity = activity;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        Bundle extras = activity.getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                menuInflater.inflate(R.menu.display_contact, menu);
            }
        }
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Edit_Contact) {
            Button b = (Button) activity.findViewById(R.id.button1);
            b.setVisibility(View.VISIBLE);
            activity.name.setEnabled(true);
            activity.name.setFocusableInTouchMode(true);
            activity.name.setClickable(true);

            activity.url.setEnabled(true);
            activity.url.setFocusableInTouchMode(true);
            activity.url.setClickable(true);

            activity.phone.setEnabled(true);
            activity.phone.setFocusableInTouchMode(true);
            activity.phone.setClickable(true);

            activity.email.setEnabled(true);
            activity.email.setFocusableInTouchMode(true);
            activity.email.setClickable(true);

            activity.products.setEnabled(true);
            activity.products.setFocusableInTouchMode(true);
            activity.products.setClickable(true);

            activity.categoryInput.setEnabled(true);
            activity.categoryInput.setFocusableInTouchMode(true);
            activity.categoryInput.setClickable(true);

            return true;
        } else if (item.getItemId() == R.id.Delete_Contact) {
            new AlertDialogFragment()
                    .show(this.activity.getSupportFragmentManager(), "AlertDialog");
            return true;
        }
        return false;
    }
}
