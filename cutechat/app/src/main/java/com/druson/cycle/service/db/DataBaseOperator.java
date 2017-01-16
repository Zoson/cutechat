package com.druson.cycle.service.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.druson.cycle.enity.Enity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoson on 6/17/15.
 */
public class DataBaseOperator {
    private DatabaseService databaseService;
    private SQLiteDatabase db;
    public DataBaseOperator(Context context){
        //databaseService = new DatabaseService(context);
    }

    public long insert(Enity enity, String table_name) {
        db = databaseService.getWritableDatabase();
        ContentValues values = enity.getContentValues();
        long rowid = -1;
        rowid = db.insert(table_name,null,values);
        db.close();
        return rowid;
    }

    public int delete(String table_name, String[] attr, String[] value) {
        db = databaseService.getWritableDatabase();
        int rows = db.delete(table_name,genWhereClause(attr),value);
        db.close();
        return rows;
    }

    public int deleteAll(String table_name) {
        db = databaseService.getWritableDatabase();
        int i = db.delete(table_name,null,null);
        db.close();
        return i;
    }


    public int update(Enity enity,String table_name,String[] attr, String[] value) {
        db = databaseService.getWritableDatabase();
        ContentValues contentValues = enity.getContentValues();
        int i = db.update(table_name,contentValues,attr[0]+"=?",value);
        db.close();
        return i;
    }


    public ArrayList<Map<String,String>> query(String table_name, String[] cols,String[] attr, String[] value) {
        String where = null;
        db = databaseService.getReadableDatabase();
        Cursor cursor = db.query(table_name, cols, where, value, null, null, null, null);
        int c = cursor.getCount();
        int cc = cursor.getColumnCount();
        cursor.moveToFirst();
        ArrayList<Map<String,String>> list_map = new ArrayList<Map<String, String>>();
        System.out.println(c+"ssss"+cc);
        for (int i = 0;i<cursor.getCount();i++){
            Map<String,String> map = new HashMap<String, String>();
            for (int j=0;j<cursor.getColumnCount();j++){
                String col_name = cursor.getColumnName(j);
                String val = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(j)));
                map.put(col_name,val);
            }
            list_map.add(map);
            cursor.moveToNext();
        }
        return list_map;
    }
    protected String genWhereClause(String[]attr){
        String where = null;
        if (attr != null){
            return null;
        }
        int len = attr.length;
        for(int i =0;i<len;i++){
            where += attr[i] + "=?" ;
            if (len>1&&i!=(len-1)){
                where += " and ";
            }
        }
        return where;
    }

    class DatabaseService extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "database";
        public static final String USER_TABLE_NAME = "user";
        public static final String BROMESSAGE_TABLE_NAME = "bromessage";

        private static final int DATABASE_VISION = 1;

        public DatabaseService(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VISION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(DataBaseTable.create_user_table);
            //db.execSQL(DataBaseTable.create_message_table);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE_NAME);
            onCreate(db);

            db.execSQL("DROP TABLE IF EXISTS "+BROMESSAGE_TABLE_NAME);
            onCreate(db);
        }
    }

    interface IDataObject{

    }
}
