package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtUserName,txtPassword,txtConfirmPassword,
            txtFirstName,txtLastName,txtPhone,txtEmail;
    private Button btnRegister;
    private CheckBox chkPolicy;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB=new DBHelper(this);
        setContentView(R.layout.activity_register);
        componentInitializer();

    }

    public void setChkPolicy(View view){
        boolean state=chkPolicy.isChecked() ? true:false;
        btnRegister.setEnabled(state);
    }

    public void insertData(View view){
        ArrayList<String>  values=getValues();
        Boolean chkUser=DB.checkUserName(values.get(0));
        Boolean flag=true;
        for (String value : values){
            if (value.isEmpty()) {
                flag=false;
                break;
            }
        }
        if ((values.get(1).equals(values.get(6))) == false)
        {
            Toast.makeText(this, "Password and Confirm Password should match", Toast.LENGTH_LONG).show();
            txtPassword.setText("");
            txtConfirmPassword.setText("");
        }else {
            if(flag==true){
                if (chkUser) {
                    Toast.makeText(this, "User name already taken", Toast.LENGTH_LONG).show();
                    chkPolicy.setChecked(false);
                    btnRegister.setEnabled(false);

                } else {
                    Boolean success = DB.insertData(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5));
                    if (success == true) {
                        Toast.makeText(this, "Data Successfully inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
                    }

//                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    Intent i = new Intent(RegisterActivity.this, LoginSuccess.class);
                    startActivity(i);
                    finish();
                }

            }else{
                Toast.makeText(this, "All the fields are mandatory", Toast.LENGTH_LONG).show();
            }

        }
    }
    public void componentInitializer(){
        btnRegister=findViewById(R.id.btnRegisterUser);
        chkPolicy=findViewById(R.id.chkPolicy);
        txtUserName=findViewById(R.id.edUserName);
        txtPassword=findViewById(R.id.edPassword);
        txtFirstName=findViewById(R.id.edFirstName);
        txtLastName=findViewById(R.id.edLastName);
        txtPhone=findViewById(R.id.edPhone);
        txtEmail=findViewById(R.id.edEmail);
        txtConfirmPassword=findViewById(R.id.edConfirmPassword);
    }

    public ArrayList<String> getValues(){
        ArrayList<String> value=new ArrayList<String >();
        value.add(txtUserName.getText().toString());
        value.add(txtPassword.getText().toString());
        value.add(txtFirstName.getText().toString());
        value.add(txtLastName.getText().toString());
        value.add(txtPhone.getText().toString());
        value.add(txtEmail.getText().toString());
        value.add(txtConfirmPassword.getText().toString());
    return (value);
    }

    public boolean validator(ArrayList<String> values) {
            //waiting to implement
        return true;
    }

    public void logOffHandler(View view){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

}