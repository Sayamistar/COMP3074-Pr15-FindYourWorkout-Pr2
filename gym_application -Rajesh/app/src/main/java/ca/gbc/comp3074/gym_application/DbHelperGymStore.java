package ca.gbc.comp3074.gym_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelperGymStore extends SQLiteOpenHelper {
    public static final String DBNAME="Login.db";

    public DbHelperGymStore(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDb) {
//        myDb.execSQL("drop table if exists tblGym");
//        myDb.execSQL("create table tblGym(name Text , address Text, phone Text, email Text, website Text, workingHours Text, tag Text, latitude Text," +
//                "longititude Text, location Text)");
//        myDb.execSQL("insert into tblGym (name,address,phone,email,website,workingHours,tag,latitude,longititude,  location) " +
//                "values('GoodLife fitness','Toronto, Bloor and Park','123456789','goodlife@gmail.com','www.goodlifefitness.com'," +
//                "'7Am-9Pm','Tagged','43.6669419','-79.3690824')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDb, int i, int i1) {
        myDb.execSQL("drop table if exists tblGym");

    }

    public String chkUserAndPassword(String username, String password){
        SQLiteDatabase myDb=this.getWritableDatabase();
        Cursor cur=myDb.rawQuery("Select * from users where username=? and password=?", new String[]{username,password});
        cur.moveToFirst();
        String str=cur.getString(cur.getColumnIndex("role"));
        Log.d("TAG",str);
        if(cur.getCount()>0){
            return str;
        }else{
            return "false";
        }

    }

}
