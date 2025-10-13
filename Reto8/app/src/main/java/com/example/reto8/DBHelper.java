package com.example.reto8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "reto8.db";
    public static final String COMPANIES_TABLE_NAME = "companies";
    public static final String COMPANIES_COLUMN_ID = "id";
    public static final String COMPANIES_COLUMN_NAME = "name";
    public static final String COMPANIES_COLUMN_URL = "url";
    public static final String COMPANIES_COLUMN_PHONE = "phone";
    public static final String COMPANIES_COLUMN_EMAIL = "email";
    public static final String COMPANIES_COLUMN_PRODUCTS = "products";
    public static final String COMPANIES_COLUMN_CATEGORY = "category";
    private HashMap hp;

    class DataObject {
        Integer id;
        String name;

        public DataObject(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table companies " +
                        "(id integer primary key, name text, url text, phone text, email text, products text, category text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS companies");
        onCreate(db);
    }

    public boolean insertCompany (String name, String phone, String url, String email, String products, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("url", url);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("products", products);
        contentValues.put("category", category);
        db.insert("companies", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from companies where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, COMPANIES_TABLE_NAME);
        return numRows;
    }

    public boolean updateCompany (Integer id, String name, String url, String phone, String email, String products, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("url", url);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("products", products);
        contentValues.put("category", category);
        db.update("companies", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteCompany (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("companies",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    @SuppressLint("Range")
    public ArrayList<DataObject> getAllCompanies() {
        ArrayList<DataObject> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from companies", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            // res.getString(res.getColumnIndex(COMPANIES_COLUMN_NAME))
            array_list.add(new DataObject(res.getInt(res.getColumnIndex(COMPANIES_COLUMN_ID)),
                    res.getString(res.getColumnIndex(COMPANIES_COLUMN_NAME))));
            res.moveToNext();
        }
        return array_list;
    }

    @SuppressLint({"Range"})
    public ArrayList<DataObject> filter(String name, String category) {
        ArrayList<DataObject> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Build the WHERE clause dynamically
        StringBuilder query = new StringBuilder("SELECT * FROM companies");
        ArrayList<String> argsList = new ArrayList<>();

        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasCategory = category != null && !category.trim().isEmpty();

        if (hasName || hasCategory) {
            query.append(" WHERE ");
            ArrayList<String> conditions = new ArrayList<>();

            if (hasName) {
                // Use LIKE for partial matches (case-insensitive)
                conditions.add("LOWER(name) LIKE ?");
                argsList.add("%" + name.toLowerCase() + "%");
            }

            if (hasCategory) {
                conditions.add("category = ?");
                argsList.add(category);
            }

            query.append(TextUtils.join(" AND ", conditions));
        }

        Cursor res = db.rawQuery(query.toString(), argsList.toArray(new String[0]));

        if (res.moveToFirst()) {
            do {
                int id = res.getInt(res.getColumnIndex(COMPANIES_COLUMN_ID));
                String companyName = res.getString(res.getColumnIndex(COMPANIES_COLUMN_NAME));
                array_list.add(new DataObject(id, companyName));
            } while (res.moveToNext());
        }

        res.close();
        db.close();
        return array_list;
    }

}
