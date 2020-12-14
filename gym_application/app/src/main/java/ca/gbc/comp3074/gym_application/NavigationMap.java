package ca.gbc.comp3074.gym_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NavigationMap extends AppCompatActivity implements OnMapReadyCallback {
    Float longitude, latitude;
    Button btnLogOff;
    String gymName;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            longitude = Float.parseFloat(extras.getString("longitude"));
            latitude = Float.parseFloat(extras.getString("latitude"));
            gymName = extras.getString("gymName");
        } else {
           latitude = 0f;
           longitude = 0f;
            gymName = "Not Found";

        }

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




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(loc).title(""));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,10));
    }
    public void navLogOffHandler(View v){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }




}