package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
    }

    public void backToLoginPage(View view){
        Intent i = new Intent(LoginSuccess.this, LoginActivity.class);
        startActivity(i);
        finish();

    }
}