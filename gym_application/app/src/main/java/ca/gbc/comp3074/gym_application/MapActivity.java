package ca.gbc.comp3074.gym_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private EditText mSearchText, edTxtName,
            edTxtAddress,edTxtPhone, edTxtEmail, edTxtWebsite,edTxtWorkingHours, edTxtTag;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button btnLocate,btnLogOffRegister;
    DBHelper Db;
    Cursor cur;
    LatLng latLng;
    private String gymId, oldLatitude, oldLongitude;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        btnLocate.setEnabled(true);
        if (mLocationPermissionsGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);


            init();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        widgetInitilizer();
        getLocationPermission();

        Db=new  DBHelper(this);

        Bundle extras=getIntent().getExtras();

        if(extras!=null){
            gymId=extras.getString("gymId");
          cur=  Db.returnGymById(extras.getString("gymId"));
          cur.moveToFirst();
          edTxtName.setText(cur.getString(cur.getColumnIndex("name")));
          edTxtAddress.setText(cur.getString(cur.getColumnIndex("address")));
          edTxtPhone.setText(cur.getString(cur.getColumnIndex("phone")));
          edTxtEmail.setText(cur.getString(cur.getColumnIndex("email")));
          edTxtWebsite.setText(cur.getString(cur.getColumnIndex("website")));
          edTxtWorkingHours.setText(cur.getString(cur.getColumnIndex("workingHours")));
          edTxtTag.setText(cur.getString(cur.getColumnIndex("tag")));
          mSearchText.setText(cur.getString(cur.getColumnIndex("location")));
          oldLatitude=cur.getString(cur.getColumnIndex("latitude"));
          oldLongitude=cur.getString(cur.getColumnIndex("longitude"));
          cur.close();

//
        }


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String USER=((MyGlobalStore) this.getApplication()).getUser();
        String ROLE=((MyGlobalStore) this.getApplication()).getUserRole();
        Boolean LOGGED=((MyGlobalStore) this.getApplication()).getLoginStatus();
        menu.findItem(R.id.mnuLogIn).setVisible(false);

        if(ROLE.equals("USER")) {
            menu.findItem(R.id.mnuAddClient).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String ROLE=((MyGlobalStore) this.getApplication()).getUserRole();
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
        }else if(id==R.id.mnuExit){
            ((MyGlobalStore) this.getApplication()).setLoginStatus(false);
            ((MyGlobalStore) this.getApplication()).setUser("");
            ((MyGlobalStore) this.getApplication()).setUserRole(" ");
            finish();
            System.exit(0);
        }else if(id==R.id.mnuViewClient){
            viewClientsHandler(null);

        }else if(id==R.id.mnulogOff){
            Intent i= new Intent(getApplicationContext(),LoginActivity.class);
            ((MyGlobalStore) this.getApplication()).setLoginStatus(false);
            ((MyGlobalStore) this.getApplication()).setUser("");
            ((MyGlobalStore) this.getApplication()).setUserRole("");
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveData(View view){
        String gotLatitude;
        String gotLongitude;
        ArrayList<String> values=getValues();
        Boolean chkUser= Db.checkTagName(values.get(0));
        Boolean flag=true;

        if(latLng==null){
            gotLatitude="0";
            gotLongitude="0";
        }else{
            gotLatitude=String.valueOf(latLng.latitude);
            gotLongitude=String.valueOf(latLng.longitude);
        }

        for (String value : values){
            if (value.isEmpty()) {
                flag=false;
                break;
            }
        }
        if(flag==true && gymId==null){
            if (chkUser) {
                Toast.makeText(this, "Gym Id already taken", Toast.LENGTH_LONG).show();

            } else {
                Boolean success = Db.insertDatatblGym(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5),values.get(6),gotLatitude,gotLongitude,"GIVEN");
                if (success == true) {
                    Toast.makeText(this, "Data Successfully inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
                }

            }
        }
        else{
            if(gymId==null) {
                Toast.makeText(this, "All the fields are mandatory**", Toast.LENGTH_LONG).show();
            }
        }
        if(gymId!=null){
            if(latLng!=null){
                oldLatitude=String.valueOf(latLng.latitude);
               oldLongitude=String.valueOf(latLng.longitude);
            }
            updateGym();
            Toast.makeText(this, "Data updated", Toast.LENGTH_LONG).show();
            gymId=null;
            setEmptyText();



        }


    }


    public void mapFinder(View v){
        geoLocate();
    }

    public void locateMap(View v){
        geoLocate();
    }

    public void widgetInitilizer(){
        mSearchText =  findViewById(R.id.input_search);
        btnLocate=findViewById(R.id.btnLocate);
        edTxtName=findViewById(R.id.edTxtName);
        edTxtAddress=findViewById(R.id.edTxtAddress);
        edTxtPhone=findViewById(R.id.edTxtPhone);
        edTxtEmail=findViewById(R.id.edTxtEmail);
        edTxtWebsite=findViewById(R.id.edTxtWebsite);
        edTxtWorkingHours=findViewById(R.id.edTxtWorkingHrs);
        edTxtTag=findViewById(R.id.edTxtTag);
    }
    public ArrayList<String> getValues(){
        ArrayList<String> value=new ArrayList<String >();
        value.add(edTxtName.getText().toString());
        value.add(edTxtAddress.getText().toString());
        value.add(edTxtPhone.getText().toString());
        value.add(edTxtEmail.getText().toString());
        value.add(edTxtWebsite.getText().toString());
        value.add(edTxtWorkingHours.getText().toString());
        if (edTxtTag.getText().toString().equals("")) {
            value.add(edTxtName.getText().toString());
        }else{
            value.add(edTxtTag.getText().toString());
        }
        return (value);
    }
    public void logOffRegister(View view){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
    }

    private void geoLocate(){

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = null;
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(searchString));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latLng.latitude,latLng.longitude), DEFAULT_ZOOM));
            Log.d(TAG, "geoLocate: found a location: " + address.toString());

        }
    }



    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
            btnLocate.setEnabled(false);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    public void viewClientsHandler(View view){
        Intent i=new Intent(getApplicationContext(),LandingPage.class);
        i.putExtra("user","ADMIN");
        startActivity(i);
        finish();
    }

    public void updateGym(){
                  Db.updateGymByID(gymId,
                  edTxtName.getText().toString(),
                  edTxtAddress.getText().toString(),
                  edTxtPhone.getText().toString(),
                  edTxtEmail.getText().toString(),
                  edTxtWebsite.getText().toString(),
                  edTxtWorkingHours.getText().toString(),
                  edTxtTag.getText().toString(),
                          oldLatitude,
                          oldLongitude,
                  mSearchText.getText().toString()
                  );
    }

    public void setEmptyText(){
        mSearchText.setText("");
        btnLocate.setText("");
        edTxtName.setText("");
        edTxtAddress.setText("");
        edTxtPhone.setText("");
        edTxtEmail.setText("");
        edTxtWebsite.setText("");
        edTxtWorkingHours.setText("");
        edTxtTag.setText("");
    }

}



