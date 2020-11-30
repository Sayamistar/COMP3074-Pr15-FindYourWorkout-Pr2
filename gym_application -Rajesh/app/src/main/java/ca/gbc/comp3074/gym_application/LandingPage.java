package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class LandingPage extends AppCompatActivity {
    MyAdapter myAdapter;
    List<Gyms> myList=new ArrayList<>();
    DBHelper Db;
    Cursor cur;



    private Button btnLogoff;
    RecyclerView recyclerView;
    private MyAdapter.RecyclerViewClickListner listner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnClickListener();
        setContentView(R.layout.activity_landing_page);

        Db=new DBHelper(this);
        cur=Db.returnAllGyms();
        System.out.println(String.valueOf(cur.getCount())+ " dsfdasf"); //here suvash


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
                Intent i =new Intent(getApplicationContext(),GymProfile.class);
                i.putExtra("gymName",myList.get(position).gymName);
                startActivity(i);
                finish();
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
            gyms.gymDescription=cur.getString(cur.getColumnIndex("address")) + "Phone " + cur.getString(cur.getColumnIndex("phone"));
            gyms.gymPicId =R.drawable.finallogo;
            myList.add(gyms);
            cur.moveToNext();

        }
        cur.close();


//        for(int i=0;i<8;i++){
//            Gyms gyms = new Gyms();
//            switch (i){
//                case 0:
//                    gyms.gymName ="ABC";
//                    gyms.gymDescription ="It is ABC gym";
//                    gyms.gymPicId =R.drawable.finallogo;
//                    myList.add(gyms);
//                    break;
//                case 1:
//                    gyms.gymName ="DEF";
//                    gyms.gymDescription ="It is DEF gym";
//                    gyms.gymPicId =R.drawable.finallogo;
//                    myList.add(gyms);
//                    break;
//                case 2:
//                    gyms.gymName ="GHI";
//                    gyms.gymDescription ="It is GHI gym";
//                    gyms.gymPicId =R.drawable.finallogo;
//                    myList.add(gyms);
//                    break;
//                case 3:
//                    gyms.gymName ="JKL";
//                    gyms.gymDescription ="It is JKL here gym";
//                    gyms.gymPicId =R.drawable.finallogo;
//                    myList.add(gyms);
//                    break;
//                case 4:
//                    gyms.gymName ="MNO";
//                    gyms.gymDescription ="I am MNO gym";
//                    gyms.gymPicId =R.drawable.finallogo;
//                    myList.add(gyms);
//                    break;
//                case 5:
//                    gyms.gymName ="QRS";
//                    gyms.gymDescription ="Here is QRS gym";
//                    gyms.gymPicId =R.drawable.finallogo;
//                    myList.add(gyms);
//                    break;
//                case 6:
//                    gyms.gymName ="TUV";
//                    gyms.gymDescription ="Here is TUV gym";
//                    gyms.gymPicId =R.drawable.finallogo;
//                    myList.add(gyms);
//                    break;
//                case 7:
//                    gyms.gymName ="WXYZ";
//                    gyms.gymDescription ="here is WXYZ gym";
//                    gyms.gymPicId =R.drawable.finallogo;
//                    myList.add(gyms);
//                    break;






    }

    public void logOffHandler(View view){
        Intent i = new Intent(LandingPage.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}