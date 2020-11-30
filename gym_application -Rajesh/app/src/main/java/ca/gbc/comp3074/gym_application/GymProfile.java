package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GymProfile extends AppCompatActivity {
    public TextView nameTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);
        nameTxt=findViewById(R.id.nameTxt);
        String gymName="";
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            gymName=extras.getString("gymName");
        }
        nameTxt.setText("You came along with the gym club name: " + gymName);
    }

    public void logOffHandler(View view){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
}