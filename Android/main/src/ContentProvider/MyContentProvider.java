package com.example.xm.xianyc.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.xm.xianyc.dbhelper.ContactOpenHelper;

public class MyContentProvider extends ContentProvider {
    public  static  final  String authorities = MyContentProvider.class.getCanonicalName();
    public  static final int CONTACT =1;
    public  static  Uri URI_CONTACT = Uri.parse("content://"+authorities+"/contact");
    static UriMatcher  mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(authorities,"/contact",CONTACT);
    }

    private ContactOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new ContactOpenHelper(getContext());
        if(mHelper != null){
            return true;
        }
        return false;
    }


    @Override
    public Cursor query( Uri uri,  String[] projection, String selection,  String[] selectionArgs,  String sortOrder) {
        int code = mUriMatcher.match(uri);
        Cursor id=null;
        switch (code){
            case CONTACT:
                SQLiteDatabase db = mHelper.getReadableDatabase();
                id = db.query(ContactOpenHelper.table_name,
                    projection,selection,selectionArgs,null,null,sortOrder);
                {
                    System.out.println("query success");

                }
                break;
            default:
                break;

        }
        return id;
    }


    @Override
    public String getType( Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        int code = mUriMatcher.match(uri);
        switch (code){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = db.insert(ContactOpenHelper.table_name,"",values);
                if(id != -1)
                {
                    System.out.println("insert success");
                    uri = ContentUris.withAppendedId(uri,id);

                }
                break;
            default:
                break;

        }
        return uri;
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int id=0;
        switch (code){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                 id = db.delete(ContactOpenHelper.table_name,selection,selectionArgs);
                if(id >0)
                {
                    System.out.println("delete success");
                   // uri = ContentUris.withAppendedId(uri,id);

                }
                break;
            default:
                break;

        }
        return id;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int id=0;
        switch (code){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                 id = db.update(ContactOpenHelper.table_name,values,selection,selectionArgs);
                if(id > 0 )
                {
                    System.out.println("update success");
                    //uri = ContentUris.withAppendedId(uri,id);

                }
                break;
            default:
                break;

        }
        return id;
    }
}
