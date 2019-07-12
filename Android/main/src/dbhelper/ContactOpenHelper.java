package com.example.xm.xianyc.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ContactOpenHelper extends SQLiteOpenHelper {
    public static final String table_name = "contact_table";
    public class ContactTable implements BaseColumns{
        public  static final String title = "title";
        public  static final String desc= "descri";
        public  static final String price= "price";
        public  static final String url="url";
        public  static final String img_url="img_url";
        public  static final String region="region";
    }
    public ContactOpenHelper(Context context) {
        super(context, "contact.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+table_name+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                 ContactTable.title+" TEXT, "+
                 ContactTable.desc+" TEXT, "+
                 ContactTable.price+" TEXT, "+
                 ContactTable.url+" TEXT, "+
                 ContactTable.img_url+" TEXT, "+
                 ContactTable.region+" TEXT);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
