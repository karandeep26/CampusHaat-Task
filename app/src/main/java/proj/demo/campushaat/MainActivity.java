package proj.demo.campushaat;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;

import proj.demo.campushaat.network.RequestBody;
import proj.demo.campushaat.network.ResponseBody;
import proj.demo.campushaat.network.RestClient;
import proj.demo.campushaat.network.service.ICreateAddress;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner citySpinner, stateSpinner, countrySpinner;
    TextInputEditText houseContent, localityContent, pincodeContent;
    TextInputLayout houseLayout, localityLayout, pincodeLayout;
    ArrayAdapter<String> cityAdapter, stateAdapter, countryAdapter;
    int stateId, cityId, countryId;
    private final static int HOUSE_LENGTH=6;
    private final static int LOCALITY_LENGTH=20;
    ArrayList<String> states, cities, countries;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setData();
        setSpinnerAdapters();
        setInputFilters();
        setItemSelectedListener();

        submit.setOnClickListener(v -> {
            RequestBody requestBody=new RequestBody("1","504B","New delhi","110034","delhi","sadasd","asdasd","Sdsds","dsdsd");
            Gson gson=new Gson();
            String data=gson.toJson(requestBody);

            Call<ResponseBody> createAddress= RestClient
                    .getServiceAuth(ICreateAddress.class).createAddress("application/json",data);
            createAddress.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody>
                        response) {
                    if(response.isSuccessful()){
                        if(response.)
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        });
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
        submit=$(R.id.submit);

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
    private void spinnerFunction(Spinner spinner){
        spinner.setSelection(0);
        spinner.setEnabled(true);
        spinner.performClick();
    }
    private void setInputFilters(){
        houseContent.setFilters(new InputFilter[]{new CustomInputFilter(houseLayout, HOUSE_LENGTH)});
        localityContent.setFilters(new InputFilter[]{new CustomInputFilter(localityLayout, LOCALITY_LENGTH)});
    }
    private void setItemSelectedListener(){
        countrySpinner.setOnItemSelectedListener(this);
        stateSpinner.setOnItemSelectedListener(this);
        citySpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
