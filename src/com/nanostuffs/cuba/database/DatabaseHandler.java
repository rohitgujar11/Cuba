package com.nanostuffs.cuba.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nanostuffs.cuba.Class.Save_location;
import com.nanostuffs.cuba.Class.Alert;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_ALERTS = "contacts_alert";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NAMELOCATION = "namelocation";
    private static final String KEY_PH_NO = "phone_number";
    
    private static final String KEY_ID_ALERT = "id";
    private static final String KEY_NAME_ALERT = "name";
    private static final String KEY_PH_NO_ALERTS = "phone_number";
    private static final String STATUS_ALERTS = "status";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+ KEY_NAMELOCATION + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        String CREATE_CONTACTS_TABLE_ALERT = "CREATE TABLE " + TABLE_ALERTS + "("
                + KEY_ID_ALERT + " INTEGER PRIMARY KEY," + KEY_NAME_ALERT + " TEXT,"
                + KEY_PH_NO_ALERTS + " TEXT," + STATUS_ALERTS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE_ALERT);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTS);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new row
   public void addContact(Save_location contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.get_lat_lng()); // Contact Phone
        values.put(KEY_NAMELOCATION, contact.getNameLocation()); 
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
 
   public void addContact_alert(Alert contact) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(KEY_NAME_ALERT, contact.get_title_string()); // Contact Name
       values.put(KEY_PH_NO_ALERTS, contact.get_Data_String()); // Contact Phone
       values.put(STATUS_ALERTS,contact.getStatus());
       // Inserting Row
       db.insert(TABLE_ALERTS, null, values);
       db.close(); // Closing database connection
   }
   
    // Getting single row
    Save_location getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Save_location contact = new Save_location(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        cursor.close();
        return contact;
    }

    public List<Save_location> getAllContacts() {
        List<Save_location> contactList = new ArrayList<Save_location>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Save_location contact = new Save_location();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setNameLocation(cursor.getString(2));
                
                contact.set_lat_lng(cursor.getString(3));
                
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }
 
    // Getting All rows
    public List<Alert> getAllContacts_alert() {
        List<Alert> contactList = new ArrayList<Alert>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ALERTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Alert contact = new Alert();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.set_title_string(cursor.getString(1));
                contact.set_Data_String(cursor.getString(2));
                contact.setStatus(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }
    
    
    
    // Updating single row
    public int updateContact(Save_location contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_NAMELOCATION, contact.getNameLocation()); 
        values.put(KEY_PH_NO, contact.get_lat_lng());
 
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
    
    
    public int updateContact_alert(Alert contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.get_title_string());
        values.put(KEY_PH_NO, contact.get_Data_String());
        values.put(STATUS_ALERTS,"1");
        // updating row
        return db.update(TABLE_ALERTS, values, KEY_ID_ALERT + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
    
    
    // Deleting single contact
    public void deleteContact(Save_location contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }
 
    public void deleteContact_alert(int id) {
    	int count =getWritableDatabase().delete(TABLE_ALERTS, KEY_ID_ALERT + "="+id,null);
    }
    // Getting rows Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
    
    public int getContactsCount_alert() {
        String countQuery = "SELECT  * FROM " + TABLE_ALERTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
}