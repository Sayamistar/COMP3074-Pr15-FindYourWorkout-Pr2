package ca.gbc.comp3074.gym_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private CheckBox chkbox;
    private EditText mSearchText, edTxtName,
            edTxtAddress,edTxtPhone, edTxtEmail, edTxtWebsite,edTxtWorkingHours, edTxtTag;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button btnLocate,btnLogOffRegister;
    DBHelper Db;
    LatLng latLng;

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
        widgetInitilizer();
        getLocationPermission();
        Db=new  DBHelper(this);


    }


    public void saveData(View view){
        ArrayList<String> values=getValues();
        Boolean chkUser= Db.checkTagName(values.get(6));
        Boolean flag=true;

        for (String value : values){
            if (value.isEmpty()) {
                flag=false;
                break;
            }
        }
            if(flag==true){
                if (chkUser) {
                    Toast.makeText(this, "User name already taken", Toast.LENGTH_LONG).show();

                } else {
//                    (name Text , address Text, phone Text, email Text, website Text, workingHours Text, tag Text, latitude Text," +
//                    "longitude Text, location Text)")
                    //
                    Boolean success = Db.insertDatatblGym(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5),values.get(6),String.valueOf(latLng.latitude),String.valueOf(latLng.longitude),"GIVEN");
                    if (success == true) {
                        Toast.makeText(this, "Data Successfully inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
                    }

                }

            }else{
                Toast.makeText(this, "All the fields are mandatory", Toast.LENGTH_LONG).show();
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
        chkbox=findViewById(R.id.chkMapTag);
        btnLocate=findViewById(R.id.btnLocate);
        edTxtName=findViewById(R.id.edTxtName);
        edTxtAddress=findViewById(R.id.edTxtAddress);
        edTxtPhone=findViewById(R.id.edTxtPhone);
        edTxtEmail=findViewById(R.id.edTxtEmail);
        edTxtWebsite=findViewById(R.id.edTxtWebsite);
        edTxtWorkingHours=findViewById(R.id.edTxtWorkingHrs);
        edTxtTag=findViewById(R.id.edTxtTag);
        btnLogOffRegister=findViewById(R.id.btnLogOffRegister);
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
//            System.out.println(latLng.latitude + " " + latLng.longitude);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

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


}



