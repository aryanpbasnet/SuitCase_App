package com.example.suit_case;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String db_name = "SuitCaseDB";
    public static final int db_version = 1;
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String USER_TABLE = "Users";
    public static final String ITEM_TABLE = "Items";

    public static final String ITEM_ID = "id";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_DESCRIPTION = "description";
    public static final String ITEM_IMAGE = "image";
    public static final String ITEM_PURCHASED = "purchased";

    public DatabaseHelper(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create the Users table to store user information
        String usersSqlQuery = "CREATE TABLE " + USER_TABLE + "(" +
                EMAIL + " TEXT PRIMARY KEY, " +
                PASSWORD + " TEXT)";

        // Create the Items table to store suitcase items
        String itemsSqlQuery = "CREATE TABLE " + ITEM_TABLE + " (" +
                ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEM_NAME + " TEXT NOT NULL, " +
                ITEM_PRICE + " TEXT NOT NULL, " +
                ITEM_DESCRIPTION + " TEXT, " +
                ITEM_IMAGE + " TEXT, " +
                ITEM_PURCHASED + " INTEGER)";

        try {
            sqLiteDatabase.execSQL(usersSqlQuery);
            sqLiteDatabase.execSQL(itemsSqlQuery);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + USER_TABLE);
        sqLiteDatabase.execSQL("drop table if exists " + ITEM_TABLE);
        onCreate(sqLiteDatabase);
    }

    // Insert a new user into the Users table
    public boolean insertUsers(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);
        Long Result = sqLiteDatabase.insert("Users", null, cv);
        if (Result==-1){
            return false;
        }else {
            return true;
        }
    }

    public boolean checkEmailPassword(String email, String password){
        // Check if a user with the given email and password exists
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from Users where email=? and password=?",
                new String[]{email, password});
        if (cursor.getCount()>0){
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from Users where email=?"
                ,new String[]{email});
        if (cursor.getCount()>0){
            return true;
        } else {
            return false;
        }
    }

    // Reset the password for a user with the given email
    public Boolean resetPassword(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);
        int Result = sqLiteDatabase.update("Users", cv, "email=?", new String[]{email});
        if (Result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor queryData(String sqlQuery){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    // Insert a new item into the Items table
    public Boolean insertItems(String item, double price, String description, String image, boolean purchased){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String insertSql = "INSERT INTO " + ITEM_TABLE + " VALUES (NULL, ?, ? ,?, ?, ?)";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(insertSql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1, item);
        sqLiteStatement.bindDouble(2, price);
        sqLiteStatement.bindString(3, description);
        sqLiteStatement.bindString(4, image);
        sqLiteStatement.bindLong(5, purchased ? 1 : 0);
        long Result = sqLiteStatement.executeInsert();
        sqLiteDatabase.close();
        return Result != -1;
    }

    public Cursor getItemByID(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sqlQuery = "SELECT * FROM " + ITEM_TABLE + " WHERE " + ITEM_ID + "=?";
        return sqLiteDatabase.rawQuery(sqlQuery, new String[]{String.valueOf(id)});
    }

    public Cursor getAllItems(){
        // Retrieve an item by its ID from the Items table
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + ITEM_TABLE;
        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public Boolean updateItem(int id, String item,double price, String description, String image, boolean purchased){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();  // Update an item's information in the Items table
        cv.put(ITEM_NAME, item);
        cv.put(ITEM_PRICE, price);
        cv.put(ITEM_DESCRIPTION, description);
        cv.put(ITEM_IMAGE, image);
        cv.put(ITEM_PURCHASED, purchased);
        int result = sqLiteDatabase.update(ITEM_TABLE, cv, ITEM_ID + "=?", new String[]{String.valueOf(id)});
        Log.d("DatabaseHelper:", "result: " + result);
        sqLiteDatabase.close();
        return result != -1;
    }


    // Delete an item from the Items table
    public void deleteItem(long id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(ITEM_TABLE, ITEM_ID + "=?", new String[]{String.valueOf(id)});
    }
}
