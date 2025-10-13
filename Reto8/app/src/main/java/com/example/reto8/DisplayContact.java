package com.example.reto8;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.lifecycle.Lifecycle;

public class DisplayContact extends AppCompatActivity implements AlertDialogFragment.AlertDialogListener {

    private DBHelper mydb;
    EditText name;
    EditText url;
    EditText phone;
    EditText email;
    EditText products;
    AutoCompleteTextView categoryInput;
    private int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Attach menu
        MenuHost menuHost = this;
        menuHost.addMenuProvider(new DisplayContactMenu(this),
                this, Lifecycle.State.RESUMED);

        // Initialize DB
        mydb = new DBHelper(this);

        // Initialize views
        name = findViewById(R.id.editTextName);
        url = findViewById(R.id.editTextUrl);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextEmail);
        products = findViewById(R.id.editTextProducts);
        categoryInput = findViewById(R.id.editTextCategory); // AutoCompleteTextView

        // Set up category suggestions
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_dropdown_item_1line);
        categoryInput.setAdapter(adapter);
        categoryInput.setThreshold(0); // show dropdown immediately when clicked
        categoryInput.setOnClickListener(v -> categoryInput.showDropDown());

        // Handle edit/view mode
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                loadCompanyData(Value);
            }
        }

        Button mReturn = findViewById(R.id.go_back);
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    @SuppressLint("Range")
    private void loadCompanyData(int id) {
        Cursor rs = mydb.getData(id);
        if (rs.moveToFirst()) {
            id_To_Update = id;

            name.setText(rs.getString(rs.getColumnIndex(DBHelper.COMPANIES_COLUMN_NAME)));
            url.setText(rs.getString(rs.getColumnIndex(DBHelper.COMPANIES_COLUMN_URL)));
            phone.setText(rs.getString(rs.getColumnIndex(DBHelper.COMPANIES_COLUMN_PHONE)));
            email.setText(rs.getString(rs.getColumnIndex(DBHelper.COMPANIES_COLUMN_EMAIL)));
            products.setText(rs.getString(rs.getColumnIndex(DBHelper.COMPANIES_COLUMN_PRODUCTS)));
            categoryInput.setText(rs.getString(rs.getColumnIndex(DBHelper.COMPANIES_COLUMN_CATEGORY)));

            disableEditing();
        }
        rs.close();
    }

    private void disableEditing() {
        name.setFocusable(false);
        name.setClickable(false);
        url.setFocusable(false);
        url.setClickable(false);
        phone.setFocusable(false);
        phone.setClickable(false);
        email.setFocusable(false);
        email.setClickable(false);
        products.setFocusable(false);
        products.setClickable(false);
        categoryInput.setFocusable(false);
        categoryInput.setClickable(false);

        Button saveBtn = findViewById(R.id.button1);
        saveBtn.setVisibility(View.INVISIBLE);
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            boolean success;

            if (Value > 0) {
                success = mydb.updateCompany(
                        id_To_Update,
                        name.getText().toString(),
                        url.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        products.getText().toString(),
                        categoryInput.getText().toString()
                );
                Toast.makeText(getApplicationContext(),
                        success ? "Updated" : "Not Updated",
                        Toast.LENGTH_SHORT).show();
            } else {
                success = mydb.insertCompany(
                        name.getText().toString(),
                        url.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        products.getText().toString(),
                        categoryInput.getText().toString()
                );
                Toast.makeText(getApplicationContext(),
                        success ? "Added successfully" : "Error adding record",
                        Toast.LENGTH_SHORT).show();
            }

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    public void onDeleteConfirmed() {
        mydb.deleteCompany(id_To_Update);
        Toast.makeText(getApplicationContext(), "Deleted successfully",
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
