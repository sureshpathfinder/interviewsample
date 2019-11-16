package com.example.contactsample.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contact_db";

    private static DatabaseHelper instance;
    public static DatabaseHelper getInstance(Context context)
    {
        if(instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create ContactModels table
        db.execSQL(ContactModel.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ContactModel.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertContact(ContactModel contactModel) {
        // get writable database as we want to write data
        ContactModel existingData= getContactModel(contactModel.getId());
        if(existingData == null) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            Gson gson = new Gson();
            values.put(ContactModel.COLUMN_ID, contactModel.getId());
            values.put(ContactModel.COLUMN_NAME, contactModel.getName());
            values.put(ContactModel.COLUMN_USERNAME, contactModel.getUserName());
            values.put(ContactModel.COLUMN_EMAIL, contactModel.getEmail());
            values.put(ContactModel.COLUMN_PHONE, contactModel.getPhone());
            values.put(ContactModel.COLUMN_WEBSITE, contactModel.getWebsite());
            values.put(ContactModel.COLUMN_ADDRESS, gson.toJson(contactModel.getAddress()));
            values.put(ContactModel.COLUMN_COMPANY, gson.toJson(contactModel.getCompany()));
            // insert row
            long id = db.insert(ContactModel.TABLE_NAME, null, values);

            // close db connection
            db.close();
            // return newly inserted row id
            return id;
        }
        return existingData.getId();
    }

    public ContactModel getContactModel(int id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ContactModel.TABLE_NAME,
                new String[]{ContactModel.COLUMN_ID, ContactModel.COLUMN_NAME, ContactModel.COLUMN_USERNAME,
                        ContactModel.COLUMN_EMAIL, ContactModel.COLUMN_PHONE, ContactModel.COLUMN_WEBSITE,
                        ContactModel.COLUMN_ADDRESS},
                ContactModel.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()){

            // prepare ContactModel object
            ContactModel contactModel = new ContactModel(
                    cursor.getInt(cursor.getColumnIndex(ContactModel.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(ContactModel.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactModel.COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(ContactModel.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(ContactModel.COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(ContactModel.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(ContactModel.COLUMN_WEBSITE)),
                    ""
            );

            // close the db connection
            cursor.close();

            return contactModel;
        }
        else
            return null;
    }

    public List<ContactModel> getAllNotes() {
        List<ContactModel> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContactModel.TABLE_NAME + " ORDER BY " +
                ContactModel.COLUMN_NAME +"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel note = new ContactModel(
                cursor.getInt(cursor.getColumnIndex(ContactModel.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ContactModel.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(ContactModel.COLUMN_USERNAME)));
                
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }
}
