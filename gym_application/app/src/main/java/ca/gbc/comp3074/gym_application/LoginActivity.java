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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUser, txtPass;
    private Button btnLogin, btnRegister;
    private TextView txtForgotPassword;
    private DBHelper DB;
    private boolean isLoggedIn=false;
    private TextInputLayout inputLayoutUserName, inputLayoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        inputLayoutUserName=findViewById(R.id.inputLayoutUserName);
        inputLayoutPassword=findViewById(R.id.inputLayoutPassword);

        txtUser=findViewById(R.id.edUserName);
        txtPass=findViewById(R.id.edPassword);
        DB=new DBHelper(this);

        txtForgotPassword=findViewById(R.id.txtForgotPassword);
        String txt= "Forgot Password??";
        SpannableString str=new SpannableString(txt);

        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(getApplicationContext(),"You will receive reseset password form in your email",Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.mnulogOff).setVisible(false);
        menu.findItem(R.id.mnuAddClient).setVisible(false);
        menu.findItem(R.id.mnuViewClient).setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
//            finish();
        }else if(id==R.id.mnuLogIn){
            loginHandler(null);
        }
        else if(id==R.id.mnuExit){
            ((MyGlobalStore) this.getApplication()).setLoginStatus(false);
            ((MyGlobalStore) this.getApplication()).setUser("");
            ((MyGlobalStore) this.getApplication()).setUserRole(" ");
            finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public void loginHandler(View view) {
        String getUser = txtUser.getText().toString();
        String getPass = txtPass.getText().toString();
        boolean isValid=true;
        if(getUser.isEmpty()){
            inputLayoutUserName.setError("User Id is Mandatory");
            isValid=false;
        }else{
            inputLayoutUserName.setErrorEnabled(false);
        }
        if(getPass.isEmpty()){
            inputLayoutPassword.setError("Password is Mandatory");
            isValid=false;
        }else{
            inputLayoutPassword.setErrorEnabled(false);
        }


            Boolean chkUser=DB.checkUserName(getUser);
            if(chkUser==true){
                String chkUserAndPassword= DB.chkUserAndPassword(getUser,getPass);
                if(!chkUserAndPassword.equals("false")){
                    ((MyGlobalStore) this.getApplication()).setUser(getUser);   //Storing user name and role for future reference
                    if(chkUserAndPassword.equals("ADMIN")) {
                        Intent i =new Intent(LoginActivity.this,MapActivity.class);
                        ((MyGlobalStore) this.getApplication()).setUserRole("ADMIN");
                        ((MyGlobalStore) this.getApplication()).setLoginStatus(true);
                        startActivity(i);
                       finish();
                    }else{
                        Intent i =new Intent(LoginActivity.this,LandingPage.class);
                        ((MyGlobalStore) this.getApplication()).setUserRole("USER");
                        ((MyGlobalStore) this.getApplication()).setLoginStatus(true);
                        startActivity(i);
                        finish();
                    }
                }else{
                    inputLayoutPassword.setError("Wrong Password inserted");
                    isValid=false;
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

    public void registerHandler(View view){
        Intent i=new Intent(this,RegisterActivity.class);
        startActivity(i);
        finish();
    }
}

