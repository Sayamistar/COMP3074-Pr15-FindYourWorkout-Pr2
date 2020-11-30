package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GymProfile extends AppCompatActivity {
    public TextView nameTxt,txtGymName,txtGymAddress, txtGymPhone, txtGymDescription, txtGymTags;
    private DBHelper Db;
    private String longitude, latitude, email,gymRef, nameRef;
    Cursor cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);

        Db=new DBHelper(this);
        Bundle extras=getIntent().getExtras();
        cur=Db.returnGymById(extras.getString("gymLongitude")); //Suvash
        cur.moveToFirst();
        txtGymName=findViewById(R.id.gymName);
        txtGymAddress=findViewById(R.id.gymAddress);
        txtGymPhone=findViewById(R.id.gymPhone);
        txtGymDescription=findViewById(R.id.gymDescription);
        txtGymTags=findViewById(R.id.gymTags);

        txtGymName.setText("GYM Name: " + cur.getString(cur.getColumnIndex("name")));
        gymRef=cur.getString(cur.getColumnIndex("name"));
        txtGymAddress.setText("GYM Address: " +cur.getString(cur.getColumnIndex("address"))) ;
        txtGymPhone.setText("GYM Phone: " +cur.getString(cur.getColumnIndex("phone")));
        txtGymDescription.setText("GYM Desc: " +cur.getString(cur.getColumnIndex("website")) +"\n Working Hrs:" + cur.getString(cur.getColumnIndex("workingHours")));
        txtGymTags.setText("GYM Tags: " +cur.getString(cur.getColumnIndex("tag")));
        longitude=cur.getString(cur.getColumnIndex("longitude"));
        latitude=cur.getString(cur.getColumnIndex("latitude"));
        email=cur.getString(cur.getColumnIndex("email"));

        cur.moveToFirst();
        String name= cur.getString(cur.getColumnIndex("name"));
        System.out.println(name);


        nameTxt=findViewById(R.id.nameTxt);
        String gymName="";

        if(extras!=null){
            gymName=extras.getString("gymName");
        }
//
        nameTxt.setText("You came along with the gym club name: " + gymName + extras.getString("gymLatitude") +" " +  extras.getString("gymLongitude"));
    }

    public void logOffHandler(View view){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void emailHandler(View v){
        Intent i =new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL,email);
        i.putExtra(Intent.EXTRA_SUBJECT, "About " + gymRef);
        i.putExtra(i.EXTRA_TEXT, "Hello Mail");
        startActivity(i.createChooser(i,"Choose your mail provider"));
    }

    public void faceBookHandler(View v){
        Toast.makeText(getApplicationContext(),"Waiting to implement", Toast.LENGTH_LONG).show();
    }
    public void twitterHandler(View v){
        Toast.makeText(getApplicationContext(),"Waiting to implement", Toast.LENGTH_LONG).show();
    }

    public void imgGymHandler(View v){
        Intent i=new Intent(getApplicationContext(),NavigationMap.class);
        i.putExtra("longitude",longitude);
        i.putExtra("latitude",latitude);
        startActivity(i);
        finish();


    }


}