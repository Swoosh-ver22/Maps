package com.example.anirudh.googlemaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.anirudh.googlemaps.models.Directions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String TAG = "MAP ACT : " ;
    public static final Integer PLACE_PICKER_REQUEST_START = 1 ;
    public static final Integer PLACE_PICKER_REQUEST_END = 2 ;
    public static final String API_KEY = "AIzaSyDSx9PUr0jG8g0lt4chi6MQOwq40twAcKc";

    private GoogleMap mMap;
    LocationManager locMan;
    LocationListener loclin;
    LatLng currentLocation ;
    private GoogleApiClient mGoogleApiClient ;
    private Button btnStartLocation , btnEndLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used

        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        currentLocation = new LatLng(28.69 ,77.14) ;
        btnStartLocation = (Button) findViewById(R.id.btnStartLocation);
        btnEndLocation = (Button)findViewById(R.id.btnEndLocation) ;
       // fetchDataFromApi(currentLocation , new LatLng(28.5962 , 77.0759));

        btnStartLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MapsActivity.this) , PLACE_PICKER_REQUEST_START);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        btnEndLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MapsActivity.this) , PLACE_PICKER_REQUEST_END);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        loclin = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = new LatLng(location.getLatitude() ,location.getLongitude()) ;
                Log.d(TAG, "onLocationChanged:  \n lat" + location.getLatitude() +" \nLong :" + location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location")) ;

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION ,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        } ,
                        234

                );

            Toast.makeText(this, "Please Grant the Location Access", Toast.LENGTH_SHORT).show();
        }else {
            requestLocation();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressWarnings("MissingPermission")
    void requestLocation(){
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 30, loclin);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng delhi = new LatLng(28.69, 77.14);
        //mMap.addMarker(new MarkerOptions().position(currentLocation).title("Saddi dilli"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(delhi ,10));
        mMap.addCircle(new CircleOptions()
        .center(delhi)
        .radius(20)
        .fillColor(Color.argb(50,50 ,50 ,200))
        .strokeColor(Color.alpha(50))) ;



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requestLocation();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST_START){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(MapsActivity.this ,data) ;
                Toast.makeText(this, "place = " + place.getName(), Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                currentLocation = place.getLatLng() ;


            }

        }else if(requestCode == PLACE_PICKER_REQUEST_END){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(MapsActivity.this ,data) ;
                Toast.makeText(this, "place = " + place.getName(), Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                fetchDataFromApi(currentLocation , place.getLatLng());
            }
        }
    }

    void fetchDataFromApi(final LatLng latLngStart , final LatLng latLngEnd){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build() ;
        String start =  latLngStart.latitude + "," + latLngStart.longitude ;
        String end =  latLngEnd.latitude + "," + latLngEnd.longitude ;
        DirectionsApi directionsApi = retrofit.create(DirectionsApi.class) ;
        directionsApi.getDirections(
                 start,
                 end,
                API_KEY
                ) .enqueue(new Callback<Directions>() {
            @Override
            public void onResponse(Call<Directions> call, Response<Directions> response) {

                Double prevlat = latLngStart.latitude  , prevlng = latLngStart.longitude ;

                for(int j = 0 ;j < response.body().getRoutes().length ; j++) {


                    for (int i = 0;
                         i < response.body().getRoutes()[j].getLegs()[0].getSteps().length
                            ; i++) {
                        Double lat = response.body().getRoutes()[0].getLegs()[0].getSteps()[i].getEnd_location().getLat();
                        Double lng = response.body().getRoutes()[0].getLegs()[0].getSteps()[i].getEnd_location().getLng();

                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(lat, lng))
                                .fillColor(Color.rgb(50, 50, 255))
                                .radius(20)
                        );

                        mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(lat, lng))
                                .add(new LatLng(prevlat, prevlng))
                        );

                        prevlat = lat;  prevlng = lng;
                    }

                }
            }

            @Override
            public void onFailure(Call<Directions> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " );
            }
        });
    }
}
