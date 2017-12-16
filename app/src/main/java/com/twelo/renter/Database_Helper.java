package com.twelo.renter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Helper extends SQLiteOpenHelper{

    public static String DATABASE_NAME = "database";
    public static Integer VERSION =1;

    public static String TABLE_NAME = "RENT";

    public static String IMAGE  = "image";
    public static String ROOM_NUM = "room_num";
    public static String NAME = "name";
    public static String CONTACT = "ph_num";
    public static String ADDRESS = "address";
    public static String TRADE = "trade";
    public static String YEAR = "year";


    public Database_Helper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE "+TABLE_NAME+" ( "+
                IMAGE+" BLOB , "+
                ROOM_NUM +" INTEGER NOT NULL , "+
                NAME+" TEXT NOT NULL , "+
                CONTACT+" TEXT UNIQUE NOT NULL , "+
                ADDRESS+" TEXT , "+
                TRADE+" TEXT , "+
                YEAR+" TEXT "+
                " ) ;";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+TABLE_NAME+" ;");
        onCreate(db);
    }
    public long Add_item(Rent_Maker rent_maker){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(IMAGE,rent_maker.getPhoto());
        contentValues.put(ROOM_NUM,rent_maker.getRoom_num());
        contentValues.put(NAME,rent_maker.getName());
        contentValues.put(CONTACT,rent_maker.getCont_num());
        contentValues.put(ADDRESS,rent_maker.getAdd());
        contentValues.put(TRADE,rent_maker.getTrad());
        contentValues.put(YEAR,rent_maker.getYear());

        long res = database.insert(TABLE_NAME,null,contentValues);
        return res;

    }
    public Cursor get_Table(String name,Integer num){
        SQLiteDatabase database = getWritableDatabase();
        String que = "SELECT * FROM "+TABLE_NAME+" WHERE "+NAME+"=\""+name+ "\" AND "+ROOM_NUM+"="+num+" ;";
        Cursor cursor = database.rawQuery(que,null);
        return cursor;
    }

    public void delete_rent(String name, Integer num){
        SQLiteDatabase database = getWritableDatabase();
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+NAME+"=\""+name+"\""+" AND "+ROOM_NUM+"="+num+" ;";
        database.execSQL(query);

    }

    public Cursor total_num_each_room(){
        SQLiteDatabase database = getWritableDatabase();
        String query  = "SELECT "+ROOM_NUM+" , COUNT(*) FROM "+TABLE_NAME+" GROUP BY "+ROOM_NUM;
        Cursor cursor = database.rawQuery(query,null);
        return cursor;
    }

    public Cursor get_inner_list_item(Integer num){
        SQLiteDatabase database = getWritableDatabase();
        String query = "SELECT "+IMAGE+" , "+NAME+" , "+TRADE+" , "+YEAR+" FROM "+TABLE_NAME+" WHERE "+ROOM_NUM+"="+num+" ;";
        return database.rawQuery(query,null);
    }
}
