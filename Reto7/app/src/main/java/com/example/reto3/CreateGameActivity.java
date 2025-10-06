package com.example.reto3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGameActivity extends AppCompatActivity {
    EditText editText;
    Button submitName;
    Button bReturn;
    DatabaseReference reference;
    String gameName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);

        FirebaseApp.initializeApp(this);

        editText = findViewById(R.id.game_name);
        submitName = findViewById(R.id.create_game);
        bReturn = findViewById(R.id.go_back2);

        submitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = editText.getText().toString();
                editText.setText("");
                if (!gameName.isEmpty()) {
                    reference = FirebaseDatabase.getInstance().getReference("games/" + gameName + "/state");
                    SharedPreferences preferences = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("currentGame", gameName);
                    editor.putBoolean("host", true);
                    editor.apply();
                    reference.setValue("created");
                    startActivity(new Intent(getApplicationContext(), OnlineActivity.class));
                    finish();
                }
            }
        });
        bReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}
