package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
//    String Role;
    RecyclerView recyclerView;
    private MyAdapter.RecyclerViewClickListner listner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnClickListener();
        setContentView(R.layout.activity_landing_page);

        Db=new DBHelper(this);

        Bundle extras=getIntent().getExtras();
        if (extras!=null){
//            Role=extras.getString("user");
        }


        txtSearchWord=findViewById(R.id.edSearchWord);

        spnColNames=findViewById(R.id.spnColNames);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Db.listColumns());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnColNames.setAdapter(adapter);

        cur=Db.returnAllGyms();

        recyclerView=findViewById(R.id.recyclerView);
        populateList(cur);

        myAdapter=new MyAdapter(LandingPage.this,myList,listner);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

}



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String USER=((MyGlobalStore) this.getApplication()).getUser();
        Boolean LOGGED=((MyGlobalStore) this.getApplication()).getLoginStatus();
        menu.findItem(R.id.mnuLogIn).setVisible(false);
        String ROLE=((MyGlobalStore) this.getApplication()).getUserRole();
        if(ROLE.equals("USER")) {
            menu.findItem(R.id.mnuAddClient).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
            MenuInflater menuInflater=getMenuInflater();
            menuInflater.inflate(R.menu.login_menu,menu);
            return true;



    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.mnuAboutUs){
            Intent i= new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);
            finish();
        }else if(id==R.id.mnulogOff){

            Intent i= new Intent(getApplicationContext(),LoginActivity.class);
            ((MyGlobalStore) this.getApplication()).setLoginStatus(false);
            ((MyGlobalStore) this.getApplication()).setUser("");
            ((MyGlobalStore) this.getApplication()).setUserRole("");
            startActivity(i);
            finish();
        }
        else if(id==R.id.mnuExit){
            ((MyGlobalStore) this.getApplication()).setLoginStatus(false);
            ((MyGlobalStore) this.getApplication()).setUser("");
            ((MyGlobalStore) this.getApplication()).setUserRole(" ");
            finish();
            System.exit(0);
        }else if(id==R.id.mnuAddClient){
            Intent i= new Intent(getApplicationContext(),MapActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private void setOnClickListener() {
        listner=new MyAdapter.RecyclerViewClickListner() {

            @Override
            public void onClick(View v, int position) {
                   Intent i =new Intent(getApplicationContext(),GymProfile.class);
                    i.putExtra("gymName",myList.get(position).gymName);
                    i.putExtra("gymLatitude",myList.get(position).gymLatitude);
                    i.putExtra("gymLongitude",myList.get(position).gymId);
                    i.putExtra("gymUserROLE","USER");  //can use from global role
                    startActivity(i);
                    finish();
            }
        };
    }


    public void populateList(Cursor cur){
        cur.moveToFirst();
        while (!cur.isAfterLast()){
            Gyms gyms = new Gyms();
            gyms.gymName=cur.getString(cur.getColumnIndex("name"));
            gyms.gymDescription=cur.getString(cur.getColumnIndex("address")) + "\nPhone  " + cur.getString(cur.getColumnIndex("phone"));
            gyms.gymLatitude=cur.getString(cur.getColumnIndex("latitude"));
            gyms.gymId =cur.getString(cur.getColumnIndex("Id"));
            gyms.gymPicId =R.drawable.finallogo;
            gyms.gymPopUpMenuId=R.drawable.ic_baseline_more;
            gyms.loggedUserRole= ((MyGlobalStore) this.getApplication()).getUserRole(); //got userrole
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