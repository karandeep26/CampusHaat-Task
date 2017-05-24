package proj.demo.campushaat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import proj.demo.campushaat.network.ResponseBody;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView house, locality, city, state, country, pin;
    TextView houseLabel,localityLabel,cityLabel,stateLabel,countryLabel,pinLabel;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ResponseBody responseBody = (ResponseBody) getIntent().getExtras().getSerializable("data");
        bindId();
        ResponseBody.Address address = responseBody.getAddress();
        house.setText(address.getRoom());
        locality.setText(address.getLocality());
        city.setText(address.getCity());
        state.setText(address.getState());
        country.setText(address.getCountry());
        pin.setText(address.getZipCode());
        latLng = new LatLng(Double.valueOf(address.getLattitude()), Double.valueOf(address.getLongitude()));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        $(R.id.topPanel).setOnClickListener(v -> finish());


    }

    private void bindId() {
        house = $(R.id.house_content);
        setTypeFace(house,false);
        locality = $(R.id.locality_content);
        setTypeFace(locality,false);
        city = $(R.id.city_content);
        setTypeFace(city,false);
        state = $(R.id.state_content);
        setTypeFace(state,false);
        country = $(R.id.country_content);
        setTypeFace(country,false);
        pin = $(R.id.pincode_content);
        setTypeFace(pin,false);
        localityLabel=$(R.id.locality_label);
        setTypeFace(localityLabel,true);
        cityLabel=$(R.id.city_label);
        setTypeFace(cityLabel,true);
        countryLabel=$(R.id.country_label);
        setTypeFace(countryLabel,true);
        houseLabel=$(R.id.house_label);
        setTypeFace(houseLabel,true);
        pinLabel=$(R.id.pincode_label);
        setTypeFace(pinLabel,true);
        stateLabel=$(R.id.state_label);
        setTypeFace(stateLabel,true);


    }

    private <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12f));
    }
    private void setTypeFace(TextView textView,boolean regular){
        String font;
        if(regular){
            font="fonts/Montserrat-Regular.otf";
        }
        else{
            font="fonts/Montserrat-Light.otf";
        }
        textView.setTypeface(Typeface.createFromAsset(getAssets(),font));
    }
}
