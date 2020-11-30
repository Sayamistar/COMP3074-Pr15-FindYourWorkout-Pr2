package ca.gbc.comp3074.gym_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME="Login.db";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDb) {
        myDb.execSQL("drop table if exists users");
        myDb.execSQL("create table users(Id Integer primary key autoincrement,username Text unique, password Text, firstName Text, lastName Text, phone Text, email Text, role Text)");

        myDb.execSQL("insert into users (username,password,firstName,lastName,phone,email,role) " +
                "values('admin','admin','Admin','admin','9787087435','admin@admin.com','ADMIN')");


        myDb.execSQL("drop table if exists tblGym");
        myDb.execSQL("create table tblGym(Id Integer primary key autoincrement, name Text , address Text, phone Text, email Text, website Text, workingHours Text, tag Text, latitude Text," +
                "longitude Text, location Text)");
        myDb.execSQL("insert into tblGym (name,address,phone,email,website,workingHours,tag,latitude,longitude,  location) " +
                "values('GoodLife fitness','Toronto Bloor and Park','123456789','goodlife@gmail.com','www.goodlifefitness.com'," +
                "'7Am-9Pm','Tagged','43.6669419','-79.3690824','Noad')");


    }

    @Override
    public void onUpgrade(SQLiteDatabase myDb, int i, int i1) {
        myDb.execSQL("drop table if exists users");
        myDb.execSQL("drop table if exists tblGym");

    }
    public Boolean insertData(String username, String password,String firstName, String lastName, String phoneNo, String email){
        SQLiteDatabase myDb=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        Log.d("TAG", password);
        Log.d("TAG", username);
        cv.put("username",username);
        cv.put("password",password);
        cv.put("firstName",firstName);
        cv.put("lastName",lastName);
        cv.put("phone",phoneNo);
        cv.put("email",email);
        cv.put("role","USER");
        long result=myDb.insert("users",null,cv);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }
    public Boolean checkUserName(String username){
        SQLiteDatabase myDb=this.getWritableDatabase();
        Cursor cur=myDb.rawQuery("Select * from users where username=?", new String[]{username});
        if(cur.getCount()>0){
            return true;
        }else{
            return false;
        }

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

    public Boolean insertDatatblGym(String name, String address,String phone, String email, String website, String workingHours, String tag,String lat, String longi, String location){
        SQLiteDatabase myDb=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("address",address);
        cv.put("phone",phone);
        cv.put("email",email);
        cv.put("website",website);
        cv.put("workingHours",workingHours);
        cv.put("tag",tag);
        cv.put("latitude",lat);
        cv.put("longitude",longi);
        cv.put("location",location);
        long result=myDb.insert("tblGym",null,cv);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }
    public Boolean checkTagName(String gymName){
        SQLiteDatabase myDb=this.getWritableDatabase();
        Cursor cur=myDb.rawQuery("Select * from tblGym where tag=?", new String[]{gymName});
        if(cur.getCount()>0){
            return true;
        }else{
            return false;
        }

    }

    public Cursor returnAllGyms(){
        SQLiteDatabase myDb=this.getWritableDatabase();
        Cursor cur=myDb.rawQuery("Select * from tblGym",null);
        if(cur.getCount()>0){
            return cur;
        }else{
            return null;
        }
    }
    public Cursor returnGymById(String id){
        SQLiteDatabase myDb=this.getWritableDatabase();
        Cursor cur=myDb.rawQuery("Select * from tblGym where id=?", new String[]{id});
        if(cur.getCount()>0){
            return cur;
        }else{
            return null;
        }

    }

    public String[]  listColumns(){
        SQLiteDatabase myDb=this.getWritableDatabase();
        Cursor cur=myDb.rawQuery("Select * from tblGym",null);
        String[] colNames=cur.getColumnNames();
        return colNames;
    }

    public Cursor returnAllBySearch(String colName, String key){
        SQLiteDatabase myDb=this.getWritableDatabase();
        String query= "Select * from tblGym where " + colName + " like '%" +  key + "%'";
        System.out.println(query);
        Cursor cur=myDb.rawQuery(query,null);
        if(cur.getCount()>0){
            return cur;
        }else{
            return null;
        }

    }

}
