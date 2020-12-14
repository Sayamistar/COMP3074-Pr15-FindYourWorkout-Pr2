package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class GymProfile extends AppCompatActivity {
    public TextView nameTxt,txtGymName,txtGymAddress, txtGymPhone, txtGymDescription, txtGymTags,
    txtUserRating;
    private DBHelper Db;
    private RatingBar curRatingBar, userRating;
    private String longitude, latitude, email,gymRef, nameRef,gymName;
    Cursor cur,curRating;
    private String gymId,USERROLE;
    private Button btnSaveRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);
        Db=new DBHelper(this);
        USERROLE=((MyGlobalStore) this.getApplication()).getUserRole();

        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            gymId = extras.getString("gymLongitude");
            cur=Db.returnGymById(extras.getString("gymLongitude")); //Suvash get the gymId in proper name
            curRating=Db.returnAllGymRankById(extras.getString("gymLongitude")); //Suvash get the gymId in proper name
            gymName= extras.getString("gymName");
        }
        bringInitialData(cur);
        nameTxt=findViewById(R.id.nameTxt);
        curRatingBar=findViewById(R.id.currentRating);
        userRating=findViewById(R.id.ratingBar);
        btnSaveRating=findViewById(R.id.btnRate);

        if(USERROLE.equals("ADMIN")){
            userRating.setEnabled(false);
            btnSaveRating.setEnabled(false);
        }


        if(curRating!=null){
            displayGymRating(curRating);

        }
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
        Toast.makeText(getApplicationContext(),"Emailed handled, Facebook Yet to be handled", Toast.LENGTH_LONG).show();
    }
    public void twitterHandler(View v){
        Toast.makeText(getApplicationContext(),"Waiting to implement", Toast.LENGTH_LONG).show();
    }

    public void imgGymHandler(View v){
        Intent i=new Intent(getApplicationContext(),NavigationMap.class);
        i.putExtra("longitude",longitude);
        i.putExtra("latitude",latitude);
        i.putExtra("gymName",gymName);
        startActivity(i);
        finish();
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
    public boolean onOptionsItemSelected( MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.mnuAboutUs){
            Intent i= new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);
            finish();
        }else if(id==R.id.mnuExit){
            ((MyGlobalStore) this.getApplication()).setLoginStatus(false);
            ((MyGlobalStore) this.getApplication()).setUser("");
            ((MyGlobalStore) this.getApplication()).setUserRole(" ");
            finish();
            System.exit(0);
        }else if(id==R.id.mnuViewClient){
            Intent i= new Intent(getApplicationContext(),LandingPage.class);
            startActivity(i);
            finish();

        }else if(id==R.id.mnulogOff){
            Intent i= new Intent(getApplicationContext(),LoginActivity.class);
            ((MyGlobalStore) this.getApplication()).setLoginStatus(false);
            ((MyGlobalStore) this.getApplication()).setUser("");
            ((MyGlobalStore) this.getApplication()).setUserRole("");
            startActivity(i);
            finish();
        }else if(id==R.id.mnuAddClient) {
            Intent i = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.login_menu,menu);
        return true;
    }
//
private void displayGymRating(Cursor curRating ){
    txtUserRating=findViewById(R.id.txtUserRating);
    curRating.moveToFirst();
    String score= curRating.getString(curRating.getColumnIndex("gymScore"));
    String gymRatedBy=curRating.getString(curRating.getColumnIndex("gymRatedBy"));
    Float userRating= Float.parseFloat(score)/Float.parseFloat(gymRatedBy);
    curRatingBar.setMax(5);
    curRatingBar.setNumStars(5);
    curRatingBar.setStepSize(.1f);
    curRatingBar.setRating(userRating);
    curRatingBar.setEnabled(false);
    curRating.close();

}

private void bringInitialData(Cursor cur){
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
    cur.close();

}

public void saveRatingData(View view){
        if(curRating!=null) {
            Float getRating = userRating.getRating();
            Db.insertRatingData(gymId, getRating);
            Toast.makeText(getApplicationContext(),"User Rating Updated",  Toast.LENGTH_SHORT).show();
            btnSaveRating.setEnabled(false);
        }else{
            Db.insertDatatblGymRanking(gymId,String.valueOf(userRating.getRating()),"1");
            Toast.makeText(getApplicationContext(),"First User Rated", Toast.LENGTH_SHORT).show();
            btnSaveRating.setEnabled(false);
        }
}

}