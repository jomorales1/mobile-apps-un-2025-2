package com.example.reto8;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuHost;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Lifecycle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    private TextView name;
    private AutoCompleteTextView category;
    DBHelper mydb;
    HashMap<String, Integer> data = new HashMap<>();
    ArrayList<DBHelper.DataObject> array_list = new ArrayList<>();

    public void loadSampleCompaniesIfEmpty() {
        if (mydb.getAllCompanies().isEmpty()) {
            mydb.insertCompany("Tech Solutions", "+1 555-1234", "https://techsolutions.com", "info@techsolutions.com", "IT Consulting Services", "Consultancy");
            mydb.insertCompany("NextGen Systems", "+1 555-2345", "https://nextgensystems.io", "hello@nextgensystems.io", "Business Process Optimization", "Consultancy");
            mydb.insertCompany("CodeSmiths", "+1 555-3456", "https://codesmiths.dev", "team@codesmiths.dev", "Web & Mobile Apps", "Custom development");
            mydb.insertCompany("SoftForge", "+1 555-4567", "https://softforge.com", "contact@softforge.com", "Enterprise Software Solutions", "Software factory");
            mydb.insertCompany("BrightMind Consulting", "+1 555-5678", "https://brightmindconsulting.com", "info@brightmindconsulting.com", "Digital Strategy and Transformation", "Consultancy");
            mydb.insertCompany("AppCrafters", "+1 555-6789", "https://appcrafters.co", "support@appcrafters.co", "Custom Mobile Development", "Custom development");
            mydb.insertCompany("DevFoundry", "+1 555-7890", "https://devfoundry.io", "contact@devfoundry.io", "Software Outsourcing and QA", "Software factory");
            mydb.insertCompany("Insight Partners", "+1 555-8901", "https://insightpartners.ai", "team@insightpartners.ai", "AI and Data Analytics Consulting", "Consultancy");
            mydb.insertCompany("NovaSoft", "+1 555-9012", "https://novasoft.io", "info@novasoft.io", "Custom Web Platforms", "Custom development");
            mydb.insertCompany("CodeWorks Factory", "+1 555-0123", "https://codeworksfactory.com", "hello@codeworksfactory.com", "Dedicated Software Teams", "Software factory");
            mydb.insertCompany("Stratus Consulting", "+1 555-3141", "https://stratusconsulting.com", "support@stratusconsulting.com", "Cloud Migration Consulting", "Consultancy");
            mydb.insertCompany("PixelForge", "+1 555-4151", "https://pixelforge.dev", "info@pixelforge.dev", "Frontend and UI Development", "Custom development");
            mydb.insertCompany("LogicHub", "+1 555-5161", "https://logichub.io", "sales@logichub.io", "Enterprise Automation Systems", "Software factory");
            mydb.insertCompany("Optima Advisors", "+1 555-6171", "https://optimaadvisors.com", "info@optimaadvisors.com", "Process Improvement Consulting", "Consultancy");
            mydb.insertCompany("SwiftDev Studio", "+1 555-7181", "https://swiftdevstudio.com", "contact@swiftdevstudio.com", "iOS & Android App Development", "Custom development");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Toolbar as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Attach menu
        MenuHost menuHost = this;
        menuHost.addMenuProvider(new MyMenuProvider(this),
                this, Lifecycle.State.RESUMED);

        name = (TextView) findViewById(R.id.filterName);
        category = (AutoCompleteTextView) findViewById(R.id.filterCategory);
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, categories);
        category.setAdapter(adapter);
        category.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) category.showDropDown();
        });


        mydb = new DBHelper(this);
        // Seed database if empty
        loadSampleCompaniesIfEmpty();
        array_list = mydb.getAllCompanies();
        ArrayList<String> companiesNames = new ArrayList<>();
        for (DBHelper.DataObject dataObject : array_list) {
            data.put(dataObject.getName(), dataObject.getId());
            companiesNames.add(dataObject.getName());
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, companiesNames);

        obj = (ListView)findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                int id_To_Search = arg2;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", data.get(array_list.get(id_To_Search).getName()));

                Intent intent = new Intent(getApplicationContext(),DisplayContact.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });


    }

    @SuppressLint("GestureBackNavigation")
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    public void runFilter(View view) {
        // Retrieve text from AutoCompleteTextView instead of Spinner
        String categoryT = ((AutoCompleteTextView) findViewById(R.id.filterCategory))
                .getText().toString().trim();

        String nameT = ((TextInputEditText) findViewById(R.id.filterName))
                .getText().toString().trim();

        // Query the filtered data
        ArrayList<DBHelper.DataObject> array_list = mydb.filter(nameT, categoryT);
        ArrayList<String> companiesNames = new ArrayList<>();

        // Clear old data and rebuild map + list
        data.clear();
        for (DBHelper.DataObject dataObject : array_list) {
            data.put(dataObject.getName(), dataObject.getId());
            companiesNames.add(dataObject.getName());
        }

        // Update the ListView adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                companiesNames
        );
        obj.setAdapter(arrayAdapter);
    }

    public void resetFilters(View view) {
        // Clear input fields
        name.setText("");
        category.setText("", false); // 'false' prevents reopening the dropdown

        // Reset list with all data (replace with your own unfiltered query)
        array_list = mydb.getAllCompanies(); // Or whatever method gets all rows
        ArrayList<String> companiesNames = new ArrayList<>();
        data.clear();

        for (DBHelper.DataObject dataObject : array_list) {
            data.put(dataObject.getName(), dataObject.getId());
            companiesNames.add(dataObject.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                companiesNames
        );
        obj.setAdapter(arrayAdapter);
    }

}