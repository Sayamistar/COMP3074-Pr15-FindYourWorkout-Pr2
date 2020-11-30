package ca.gbc.comp3074.gym_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUser, txtPass;
    private Button btnLogin, btnRegister,btnAbout;
    private TextView txtForgotPassword;
    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAbout=findViewById(R.id.btnAbout);
        txtUser=findViewById(R.id.edUserName);
        txtPass=findViewById(R.id.edPassword);
        DB=new DBHelper(this);

        txtForgotPassword=findViewById(R.id.txtForgotPassword);
        String txt= "Forgot Password??";
        SpannableString str=new SpannableString(txt);

        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(getApplicationContext(),"The Link is Yet to be implemented!!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
                txtForgotPassword.setHighlightColor(Color.TRANSPARENT);

            }

        };

        str.setSpan(clickableSpan,0,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtForgotPassword.setText(str);
        txtForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void loginHandler(View view) {
        String getUser = txtUser.getText().toString();
        String getPass = txtPass.getText().toString();

        if (getUser.equals("") || getPass.equals("")) {
            Toast.makeText(getApplicationContext(),"Please enter all the values", Toast.LENGTH_LONG).show();
        }else{
            Boolean chkUser=DB.checkUserName(getUser);
            if(chkUser==true){
                String chkUserAndPassword= DB.chkUserAndPassword(getUser,getPass);
                if(!chkUserAndPassword.equals("false")){
                    if(chkUserAndPassword.equals("ADMIN")) {
                        Intent i =new Intent(LoginActivity.this,MapActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i =new Intent(LoginActivity.this,LandingPage.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Wrong Password inserted", Toast.LENGTH_LONG).show();
                }

            }else{
                AlertDialog dialog=new AlertDialog.Builder(this)
                        .setTitle("Account Not Registered")
                        .setMessage("Register an Account now")
                        .setPositiveButton("OK",null)
                        .setNegativeButton("Dismiss",null)
                        .show();

                Button positiveButton=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i= new Intent(getApplicationContext(),RegisterActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                        finish();
                    }
                });
                Button negativeButton=dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoginActivity.this, "You can register CLICKING the Register Button", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
//                        finish();
                    }
                });
            }
        }
    }

    public void registerHandler(View view){
        Intent i=new Intent(this,RegisterActivity.class);
//        Intent i=new Intent(this,AdminPage.class);
//        Intent i=new Intent(this,MapActivity.class);
        startActivity(i);
        finish();
    }

    public void aboutHandler(View view){
        Intent i= new Intent(getApplicationContext(),AboutActivity.class);
        startActivity(i);
        finish();
    }

    public void exitHandler(View view){
        finish();
        System.exit(0);
    }

}

