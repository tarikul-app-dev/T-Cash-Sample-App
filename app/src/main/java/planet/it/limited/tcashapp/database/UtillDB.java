package planet.it.limited.tcashapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import planet.it.limited.tcashapp.model.ContactModel;

/**
 * Created by Tarikul on 7/5/2018.
 */

public class UtillDB {
    // db version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "t_cash-info";
    private static final String DATABASE_TABLE_TCASH = "t_cash";
    private UtillDB.DBHelper dbhelper;
    private final Context context;
    private SQLiteDatabase database;

    // insert row
    public static final String KEY_ROWID = "id";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_ID = "trx_id";
    public static final String KEY_STATUS = "status";

    //to use print



    private static class DBHelper extends SQLiteOpenHelper {

        @SuppressLint("NewApi")
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // create table to store msgs
            db.execSQL(" CREATE TABLE " + DATABASE_TABLE_TCASH + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_MOBILE_NUMBER + " TEXT, "
                    + KEY_AMOUNT + " TEXT, "
                    + KEY_ID + " TEXT, "
                    + KEY_STATUS + " TEXT );");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TCASH);


            onCreate(db);
        }

    }
    // constructor
    public UtillDB(Context c) {
        context = c;
    }

    // open db
    public UtillDB open() {
        dbhelper = new  DBHelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    // close db
    public void close() {
        dbhelper.close();
    }


    public long saveAllTransaction(String mobNumber,String amount,String id,String status){
        ContentValues cv = new ContentValues();
        cv.put(KEY_MOBILE_NUMBER, mobNumber);
        cv.put(KEY_AMOUNT, amount);
        cv.put(KEY_ID, id);
        cv.put(KEY_STATUS, status);

        long dbInsert = database.insert(DATABASE_TABLE_TCASH, null, cv);

        if(dbInsert != -1) {

           // Toast.makeText(context, "Contacts Save Success" + dbInsert, Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
        }



        return dbInsert;
    }





//    public ArrayList getInputData(){
//
//        ArrayList<ContactModel> contactList = new ArrayList<>();
//        String select_query = "SELECT  * FROM " + DATABASE_TABLE_CONTACTS ;
//
//
//        Cursor cursor = database.rawQuery(select_query,null);
//
//        // if(cursor != null && cursor.moveToFirst()){
//        //int iDbId = cursor.getColumnIndex(KEY_ROWID);
//        int iUserName = cursor.getColumnIndex(KEY_NAME);
//        int iMobNumber = cursor.getColumnIndex(KEY_MOBILE_NUMBER);
//        int iImage = cursor.getColumnIndex(KEY_IMAGE);
//
//
//        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
//            //    for (cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()) {
//
//            ContactModel contactModel = new ContactModel();
//            contactModel.setUserName(cursor.getString(iUserName));
//            contactModel.setContactNumber(cursor.getString(iMobNumber));
//            contactModel.setUserPic(cursor.getString(iImage));
//            contactList.add(contactModel);
//
//
//        }
//        cursor.close();
//        return contactList;
//    }

//    public ArrayList searchByName(String searchItem){
//        ArrayList<ContactModel> searchList = new ArrayList<>();
//
//
//        String select_query   = " SELECT * FROM " + DATABASE_TABLE_CONTACTS + " WHERE "  + KEY_NAME + " like '%" + searchItem + "%'"
//                + " OR " + KEY_MOBILE_NUMBER + " like '%" + searchItem + "%'" ;
//
//
//        Cursor cursor = database.rawQuery(select_query,null);
//
//        // if(cursor != null && cursor.moveToFirst()){
//        //int iDbId = cursor.getColumnIndex(KEY_ROWID);
//        int iUserName = cursor.getColumnIndex(KEY_NAME);
//        int iMobNumber = cursor.getColumnIndex(KEY_MOBILE_NUMBER);
//        int iImage = cursor.getColumnIndex(KEY_IMAGE);
//
//        for (cursor.moveToLast(); ! cursor.isBeforeFirst(); cursor.moveToPrevious()) {
//            //    for (cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()) {
//
//            ContactModel contactModel = new ContactModel();
//            contactModel.setUserName(cursor.getString(iUserName));
//            contactModel.setContactNumber(cursor.getString(iMobNumber));
//            contactModel.setUserPic(cursor.getString(iImage));
//            searchList.add(contactModel);
//
//
//        }
//        cursor.close();
//
//
//
//        return searchList;
//
//
//    }






}
