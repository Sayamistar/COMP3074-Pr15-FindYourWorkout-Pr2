package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LandingPage extends AppCompatActivity {
    MyAdapter myAdapter;
    List<Gyms> myList=new ArrayList<>();
    DBHelper Db;
    Cursor cur;
    Spinner spnColNames;
    EditText txtSearchWord;
    CheckBox chkUpdate, chkDelete;
    String Role;




    private Button btnLogoff;
    RecyclerView recyclerView;
    private MyAdapter.RecyclerViewClickListner listner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnClickListener();
        setContentView(R.layout.activity_landing_page);

        Db=new DBHelper(this);
        chkUpdate=findViewById(R.id.chkUpdateClient);
        chkDelete=findViewById(R.id.chkDeleteClient);

        Bundle extras=getIntent().getExtras();
        if (extras==null){
            System.out.println("User Role " + "USER" );
        }else{
            chkDelete.setVisibility(View.VISIBLE);
            chkUpdate.setVisibility(View.VISIBLE);
            Role=extras.getString("user");
            System.out.println("User Roles " + Role );
        }



        System.out.println(Db.listColumns().length);
        txtSearchWord=findViewById(R.id.edSearchWord);

        spnColNames=findViewById(R.id.spnColNames);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Db.listColumns());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColNames.setAdapter(adapter);

        cur=Db.returnAllGyms();
        btnLogoff=findViewById(R.id.btnLogOff);
        recyclerView=findViewById(R.id.recyclerView);
        populateList(cur);

        myAdapter=new MyAdapter(LandingPage.this,myList,listner);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

}

    private void setOnClickListener() {
        listner=new MyAdapter.RecyclerViewClickListner() {

            @Override
            public void onClick(View v, int position) {
//                System.out.println(ROLE);
//                if(ROLE.equals("ADMIN")){
//                    Intent i=new Intent(getApplicationContext(),UpdateDeleteClient.class);
//                    i.putExtra("gymName",myList.get(position).gymName);
//                    i.putExtra("gymLatitude",myList.get(position).gymLatitude);
//                    i.putExtra("gymLongitude",myList.get(position).gymLongitude);
//                    i.putExtra("ROLE","ADMIN");
//                    startActivity(i);
//                    finish();
//                }
                System.out.println("IM here");
                System.out.println("User Roles " + Role );

                    Intent i =new Intent(getApplicationContext(),GymProfile.class);
                    i.putExtra("gymName",myList.get(position).gymName);
                    i.putExtra("gymLatitude",myList.get(position).gymLatitude);
                    i.putExtra("gymLongitude",myList.get(position).gymLongitude);
                    i.putExtra("ROLE","USER");
                    startActivity(i);
                    finish();
//                }


            }
        };
    }

//    public int CountRecords(){
//
//    }

    public void populateList(Cursor cur){
        cur.moveToFirst();
        while (!cur.isAfterLast()){
            Gyms gyms = new Gyms();
            gyms.gymName=cur.getString(cur.getColumnIndex("tag"));
            gyms.gymDescription=cur.getString(cur.getColumnIndex("address")) + "Phone  " + cur.getString(cur.getColumnIndex("phone"));
            gyms.gymLatitude=cur.getString(cur.getColumnIndex("latitude"));
            gyms.gymLongitude=cur.getString(cur.getColumnIndex("Id"));
            gyms.gymPicId =R.drawable.finallogo;
            myList.add(gyms);
            cur.moveToNext();

        }
        cur.close();


    }

    public void logOffHandler(View view){
        Intent i = new Intent(LandingPage.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void searchHandler(View v){
        cur=Db.returnAllBySearch(spnColNames.getSelectedItem().toString(),txtSearchWord.getText().toString());
//        System.out.println(cur.getCount());
        if(cur!=null) {
            myList.clear();
            populateList(cur);
            myAdapter = new MyAdapter(LandingPage.this, myList, listner);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }else {
            Toast.makeText(getApplicationContext(),"No data exists",Toast.LENGTH_LONG).show();
        }

    }

}