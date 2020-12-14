package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String USER=((MyGlobalStore) this.getApplication()).getUser();
        String USERROLE=((MyGlobalStore) this.getApplication()).getUserRole();
        Boolean LOGGED=((MyGlobalStore) this.getApplication()).getLoginStatus();
        String ROLE=((MyGlobalStore) this.getApplication()).getUserRole();

        if(!LOGGED) {
            menu.findItem(R.id.mnuLogIn).setVisible(true);
            menu.findItem(R.id.mnulogOff).setVisible(false);
            menu.findItem(R.id.mnuAddClient).setVisible(false);
            menu.findItem(R.id.mnuViewClient).setVisible(false);
        }else{
            menu.findItem(R.id.mnuLogIn).setVisible(false);
            if(USERROLE.equals("USER")) {
                 menu.findItem(R.id.mnuAddClient).setVisible(false);

            }
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
        }else if(id==R.id.mnuLogIn) {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
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


}