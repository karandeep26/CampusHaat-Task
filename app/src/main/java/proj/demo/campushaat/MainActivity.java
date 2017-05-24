package proj.demo.campushaat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import proj.demo.campushaat.network.RequestBody;
import proj.demo.campushaat.network.ResponseBody;
import proj.demo.campushaat.network.RestClient;
import proj.demo.campushaat.network.service.ICreateAddress;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        OnMapReadyCallback, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, android.location.LocationListener {
    Marker marker;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    Spinner citySpinner, stateSpinner, countrySpinner;
    TextInputEditText houseContent, localityContent, pincodeContent;
    TextInputLayout houseLayout, localityLayout, pincodeLayout;
    ArrayAdapter<String> cityAdapter, stateAdapter, countryAdapter;
    int stateId, cityId, countryId;
    private final static int HOUSE_LENGTH = 6;
    private final static int LOCALITY_LENGTH = 20;
    ArrayList<String> states, cities, countries;
    Button submit;
    double longitude, latitude;
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;




    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            setmGoogleApiClient();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        bindViews();
        setData();
        setSpinnerAdapters();
        setInputFilters();
        setItemSelectedListener();
        displayLocationSettingsRequest();
        submit.setOnClickListener(this);

    }

    private void setData() {
        countries = new ArrayList<>();
        countries.add("India");
        states = new ArrayList<>();
        states.add("New Delhi");
        cities = new ArrayList<>();
        cities.add("Delhi");
    }

    private void bindViews() {
        citySpinner = $(R.id.city_spinner);
        stateSpinner = $(R.id.state_spinner);
        houseContent = $(R.id.house_content);
        localityContent = $(R.id.locality_content);
        houseLayout = $(R.id.house_layout);
        localityLayout = $(R.id.locality_layout);
        pincodeContent = $(R.id.pincode_content);
        pincodeLayout = $(R.id.pincode_layout);
        countrySpinner = $(R.id.country_spinner);
        submit = $(R.id.submit);

    }

    private void setSpinnerAdapters() {
        countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                countries);
        countrySpinner.setAdapter(new NothingSelectedSpinnerAdapter(countryAdapter, R.layout
                .spinner_nothing_selected_country, this));
        stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                states);
        cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                cities);
        stateSpinner.setAdapter(new NothingSelectedSpinnerAdapter(stateAdapter,
                R.layout.spinner_nothing_selected_state, this));
        citySpinner.setAdapter(new NothingSelectedSpinnerAdapter(cityAdapter,
                R.layout.spinner_nothing_selected_city, this));

    }

    private <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.country_spinner:
                countryId = position;
                if (position > 0) {
                    spinnerFunction(stateSpinner);
                } else {
                    stateSpinner.setEnabled(false);
                }
                break;
            case R.id.state_spinner:
                stateId = position;
                if (position > 0) {
                    spinnerFunction(citySpinner);
                } else {
                    citySpinner.setEnabled(false);
                }
                break;
            case R.id.city_spinner:
                cityId = position;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void spinnerFunction(Spinner spinner) {
        spinner.setSelection(0);
        spinner.setEnabled(true);
        spinner.performClick();
    }

    private void setInputFilters() {
        houseContent.setFilters(new InputFilter[]{new CustomInputFilter(houseLayout,
                HOUSE_LENGTH)});
        localityContent.setFilters(new InputFilter[]{new CustomInputFilter(localityLayout,
                LOCALITY_LENGTH)});
        pincodeContent.setFilters(new InputFilter[]{new CustomInputFilter(pincodeLayout,HOUSE_LENGTH)});
    }

    private void setItemSelectedListener() {
        countrySpinner.setOnItemSelectedListener(this);
        stateSpinner.setOnItemSelectedListener(this);
        citySpinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        mLastKnownLocation= locationManager.getLastKnownLocation(bestProvider);
        if (mLastKnownLocation != null) {
            onLocationChanged(mLastKnownLocation);
        }

        locationManager.requestLocationUpdates(bestProvider, 2000, 0,this);
        mMap = map;
        marker=mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).draggable(true));
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                latitude=marker.getPosition().latitude;
                longitude=marker.getPosition().longitude;

            }
        });

        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private String toString(TextInputEditText editText) {
        return editText.getText().toString();
    }

    private boolean isStringEmpty(TextInputEditText textInputEditText) {
        return toString(textInputEditText).trim().equals("");
    }

    @Override
    public void onClick(View v) {
        if (!isStringEmpty(localityContent) && !isStringEmpty(houseContent) && !isStringEmpty
                (pincodeContent)
                && cityId != 0 && stateId != 0 && countryId != 0) {
            RequestBody requestBody = new RequestBody("1", toString(houseContent),
                    toString(localityContent), toString(pincodeContent), cityId + "", stateId + "",
                    countryId + "", longitude + "", "" + latitude);
            Call<ResponseBody> createAddress = RestClient
                    .getServiceAuth(ICreateAddress.class).createAddress(requestBody);
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.show();
            createAddress.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        if (response.body().getBaseResponse().getStatusCode().equals("200")) {
                            Log.d("Body", response.body().toString());
                            Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                            intent.putExtra("data",response.body());
                            MainActivity.this.startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();


                }
            });
        } else {
            Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
        }

    }

    private void getDeviceLocation() {
    /*
     * Before getting the device location, you must check location
     * permission, as described earlier in the tutorial. Then:
     * Get the best and most recent location of the device, which may be
     * null in rare cases when a location is not available.
     */

        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            latitude=mLastKnownLocation.getLatitude();
            longitude=mLastKnownLocation.getLongitude();
            marker.setPosition(new LatLng(latitude,longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()),12f));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 12f));
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    setmGoogleApiClient();
                }
            }
        }
        updateLocationUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CHECK_SETTINGS){
            switch (resultCode){
                case RESULT_OK:

                    break;
                case RESULT_CANCELED:
                    finish();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location",location.toString());
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12f));

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
    private void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i(TAG, "All location settings are satisfied.");
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.i(TAG, "PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                    break;
            }
        });
    }
    void setmGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
}
