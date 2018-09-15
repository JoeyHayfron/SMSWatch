package com.engineerskasa.smswatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.engineerskasa.smswatch.Model.SMS_Sent;
import com.engineerskasa.smswatch.Model.SMS_Unsent;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "sms_database";



    public SQLiteDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SMS_Sent.CREATE_TABLE);
        sqLiteDatabase.execSQL(SMS_Unsent.CREATE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ SMS_Sent.SMS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ SMS_Unsent.SMS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public long insertSentSMS(String sender,String message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SMS_Sent.SMS_COLUMN_SENDER,sender);
        values.put(SMS_Sent.SMS_COLUMN_MESSAGE,message);

        long id = db.insert(SMS_Sent.SMS_TABLE_NAME,null,values);

        db.close();
        return id;
    }

    public long insertUnsentSMS(String sender,String message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SMS_Unsent.SMS_COLUMN_SENDER,sender);
        values.put(SMS_Unsent.SMS_COLUMN_MESSAGE,message);

        long id = db.insert(SMS_Unsent.SMS_TABLE_NAME,null,values);

        db.close();
        return id;
    }

    public List<SMS_Sent> getAllSentMessages(){
        List<SMS_Sent> sent = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + SMS_Sent.SMS_TABLE_NAME + " ORDER BY " +
                SMS_Sent.SMS_COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);


        if(cursor.moveToFirst()){
            do{
                SMS_Sent sent_msgs = new SMS_Sent();
                sent_msgs.setId(0);
                sent_msgs.setMessage(cursor.getString(cursor.getColumnIndex(SMS_Sent.SMS_COLUMN_MESSAGE)));
                sent_msgs.setSender(cursor.getString(cursor.getColumnIndex(SMS_Sent.SMS_COLUMN_SENDER)));
                sent_msgs.setTimestamp(cursor.getString(cursor.getColumnIndex(SMS_Sent.SMS_COLUMN_TIMESTAMP)));

                sent.add(sent_msgs);
            }while (cursor.moveToNext());
        }

        db.close();

        return sent;
    }

    public List<SMS_Unsent> getAllUnsentMessages(){
        List<SMS_Unsent> unsent = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + SMS_Unsent.SMS_TABLE_NAME + " ORDER BY " +
                SMS_Unsent.SMS_COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);


        if(cursor.moveToFirst()){
            do{
                SMS_Unsent unsent_msgs = new SMS_Unsent();
                unsent_msgs.setId(0);
                unsent_msgs.setMessage(cursor.getString(cursor.getColumnIndex(SMS_Unsent.SMS_COLUMN_MESSAGE)));
                unsent_msgs.setSender(cursor.getString(cursor.getColumnIndex(SMS_Unsent.SMS_COLUMN_SENDER)));
                unsent_msgs.setTimestamp(cursor.getString(cursor.getColumnIndex(SMS_Unsent.SMS_COLUMN_TIMESTAMP)));

                unsent.add(unsent_msgs);
            }while (cursor.moveToNext());
        }

        db.close();

        return unsent;
    }

    public int getSentCount() {
        String countQuery = "SELECT  * FROM " + SMS_Sent.SMS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int getUnsentCount() {
        String countQuery = "SELECT  * FROM " + SMS_Unsent.SMS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public void deleteSent(SMS_Sent sentsms) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SMS_Sent.SMS_TABLE_NAME, SMS_Sent.SMS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(sentsms.getId())});
        db.close();
    }

    public void deleteUnsent(SMS_Unsent unsentsms) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SMS_Unsent.SMS_TABLE_NAME, SMS_Unsent.SMS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(unsentsms.getId())});
        db.close();
    }

    public SMS_Sent getSent(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SMS_Sent.SMS_TABLE_NAME,
                new String[]{SMS_Sent.SMS_COLUMN_ID, SMS_Sent.SMS_COLUMN_SENDER,SMS_Sent.SMS_COLUMN_MESSAGE, SMS_Sent.SMS_COLUMN_TIMESTAMP},
                SMS_Sent.SMS_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        SMS_Sent sent = new SMS_Sent(
                cursor.getInt(0),
                cursor.getString(cursor.getColumnIndex(SMS_Sent.SMS_COLUMN_SENDER)),
                cursor.getString(cursor.getColumnIndex(SMS_Sent.SMS_COLUMN_MESSAGE)),
                cursor.getString(cursor.getColumnIndex(SMS_Sent.SMS_COLUMN_TIMESTAMP)));



        // close the db connection
        cursor.close();

        return sent;
    }

    public SMS_Unsent getUnsent(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SMS_Unsent.SMS_TABLE_NAME,
                new String[]{SMS_Unsent.SMS_COLUMN_ID, SMS_Unsent.SMS_COLUMN_SENDER,SMS_Unsent.SMS_COLUMN_MESSAGE, SMS_Unsent.SMS_COLUMN_TIMESTAMP},
                SMS_Unsent.SMS_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        SMS_Unsent unsent = new SMS_Unsent(
                cursor.getInt(0),
                cursor.getString(cursor.getColumnIndex(SMS_Unsent.SMS_COLUMN_SENDER)),
                cursor.getString(cursor.getColumnIndex(SMS_Unsent.SMS_COLUMN_MESSAGE)),
                cursor.getString(cursor.getColumnIndex(SMS_Unsent.SMS_COLUMN_TIMESTAMP)));



        // close the db connection
        cursor.close();

        return unsent;
    }

}

