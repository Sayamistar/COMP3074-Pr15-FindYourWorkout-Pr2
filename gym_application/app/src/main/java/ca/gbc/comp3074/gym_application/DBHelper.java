package ca.gbc.comp3074.gym_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
                "values('admin','admin','Admin','GbcAdminUser','9787087435','admin@admin.com','ADMIN')");
        myDb.execSQL("insert into users (username,password,firstName,lastName,phone,email,role) " +
                "values('user','admin','User','GbcUser','9787087444','user@user.com','USER')");


        myDb.execSQL("drop table if exists tblGym");
        myDb.execSQL("create table tblGym(Id Integer primary key autoincrement, name Text , address Text, phone Text, email Text, website Text, workingHours Text, tag Text, latitude Text," +
                "longitude Text, location Text)");
        myDb.execSQL("insert into tblGym (name,address,phone,email,website,workingHours,tag,latitude,longitude,  location) " +
                "values('GoodLife fitness','Toronto Bloor and Park','123456789','goodlife@gmail.com','www.goodlifefitness.com'," +
                "'7Am-9Pm','GoodLife, gym, sauna, spa','43.6669419','-79.3690824','Toronto')");
        myDb.execSQL("insert into tblGym (name,address,phone,email,website,workingHours,tag,latitude,longitude,  location) " +
                "values('Wellness fitness','Toronto, Scarbrough ','0000000000','wellness@gmail.com','www.wellnessfitness.com'," +
                "'7Am-9Pm','Tagged','43.6669419','-79.3690824','Toronto')");

        myDb.execSQL("drop table if exists tblGymRanking");
        myDb.execSQL("create table tblGymRanking(Id Integer primary key autoincrement,gymId Text, gymScore Text, gymRatedBy Text)");
        myDb.execSQL("insert into tblGymRanking(gymId,gymScore,gymRatedBy)" +
                "values(1,9,2)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase myDb, int i, int i1) {
        myDb.execSQL("drop table if exists users");
        myDb.execSQL("drop table if exists tblGym");
        myDb.execSQL("drop table if exists tblGymRanking");

    }
    public Boolean insertData(String username, String password,String firstName, String lastName, String phoneNo, String email){
        SQLiteDatabase myDb=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
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
       Log.d("TAG","I am good");
        Cursor cur=myDb.rawQuery("Select * from users where username=?", new String[]{username});
        Log.d("TAG","I am bad");
        if(cur.getCount()>0){
            return true;
        }else{
            return false;
        }

    }
    public String chkUserAndPassword(String username, String password){
        SQLiteDatabase myDb=this.getWritableDatabase();
        String tableName="users";
//        Cursor cur=myDb.rawQuery("Select * from " + tableName + " where " + "username=? and password=?", new String[]{username,password});
        Cursor cur=myDb.rawQuery("Select * from users where username=? and password=?", new String[]{username,password});
        cur.moveToFirst();
        if(cur.getCount()>0){
            String str=(cur.getString(cur.getColumnIndex("role")));
            cur.close();
            return str;
        }
            return "false";
    }

//    myDb.execSQL("create table tblGymRanking(Id Integer primary key autoincrement,gymId Text unique, gymScore Text, gymRatedBy Text, extra Text)");

    public Boolean insertDatatblGymRanking(String gymId, String gymScore,String gymRatedBy){
        SQLiteDatabase myDb=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("gymId",gymId);
        cv.put("gymScore",gymScore);
        cv.put("gymRatedBy",gymRatedBy);
        long result=myDb.insert("tblGymRanking",null,cv);
        if(result==-1){
            return false;
        }else{
            return true;
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
        Cursor cur=myDb.rawQuery("Select * from tblGym where name=?", new String[]{gymName});
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
        Cursor cur=myDb.rawQuery(query,null);
        if(cur.getCount()>0){
            return cur;
        }else{
            return null;
        }

    }

    public Cursor returnAllGymRankById(String id){          //returns all value from gymRanking table
        SQLiteDatabase myDb=this.getWritableDatabase();
        Cursor cur=myDb.rawQuery("Select * from tblGymRanking where gymId =?", new String[]{id} );
        if(cur.getCount()>0){
            return cur;
        }else{
            return null;
        }
    }

    public boolean deleteGym(String id){
        SQLiteDatabase myDb=this.getWritableDatabase();
        return myDb.delete("tblGym", "Id" + "=?", new String[]{id}) > 0;
    }


    public void insertRatingData(String id, Float score){
        SQLiteDatabase myDb=this.getWritableDatabase();
        Cursor cur=myDb.rawQuery("Select * from tblGymRanking where gymId =?", new String[]{id} );
        cur.moveToFirst();
        Float getScore=Float.parseFloat(cur.getString(cur.getColumnIndex("gymScore")));
        Float countUsers=Float.parseFloat(cur.getString(cur.getColumnIndex("gymRatedBy")));
        String newScore=String.valueOf(getScore +score);
        String gymRatedby=String.valueOf(countUsers+1);

        ContentValues cv= new ContentValues();
        cv.put("gymScore",newScore);
        cv.put("gymRatedBy",gymRatedby);
        myDb.update("tblGymRanking",cv,"gymId = ?", new String []{id});
    }

    public boolean updateGymByID(String id, String name, String address,String phone, String email, String website, String workingHours, String tag,String lat, String longi, String location){
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
        int result= myDb.update("tblGym",cv,"id = ?", new String []{id});
        if(result==1){
            return true;
        }else{
            return false;
        }

    }





}
