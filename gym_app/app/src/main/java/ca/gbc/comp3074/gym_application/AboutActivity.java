package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    public void logOffHandler(View view){
        Intent i = new Intent(AboutActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}