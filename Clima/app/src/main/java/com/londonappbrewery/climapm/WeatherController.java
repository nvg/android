package com.londonappbrewery.climapm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;


public class WeatherController extends AppCompatActivity {

    final int REQUEST_LOCATION = 1;

    private static final String LOCATION_PROVIDER = "gps";

    // Constants:  http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID={APIKEY}
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "API_KEY";
    private static final long MIN_TIME = 5000;
    private static final float MIN_DISTANCE = 1000;

    private TextView mCityLabel;
    private ImageView mWeatherImage;
    private TextView mTemperatureLabel;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private String mAppId;

    // TODO: Declare a LocationManager and a LocationListener here:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        try {
            mAppId = Utils.getProperty(API_KEY, getApplicationContext());
        } catch (IOException e) {
            throw new RuntimeException("Unable to get APP_ID", e);
        }
        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);

        getWeather();

        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherController.this, ChangeCityController.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManager == null) {
            return;
        }

        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        getWeather(city);
    }

    private void getWeather() {
        getWeather(null);
    }

    private void getWeather(final String city) {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                RequestParams params = new RequestParams();

                if (city == null) {
                    params.put("lat", location.getLatitude());
                    params.put("lon", location.getLongitude());
                } else {
                    params.put("q", city);
                }
                params.put("appid", mAppId);

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            WeatherDataModel model = new WeatherDataModel(response);
                            mTemperatureLabel.setText(model.getTemp() + "°");
                            mCityLabel.setText(model.getCity());

                            int resId = getResources().getIdentifier(model.getIcon(),
                                    "drawable", getPackageName());
                            mWeatherImage.setImageResource(resId);
                        } catch (JSONException e) {
                            throw new RuntimeException("Unable to parse data model", e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("Clima", "Unable to fetch weather: " + statusCode, throwable);
                        Toast.makeText(WeatherController.this,
                                "Unable to fetch weather info", Toast.LENGTH_LONG).show();
                    }
                });
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
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        Log.i("Clima", "" + mLocationManager.getAllProviders());
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeather();
                return;
            }
        }

        Log.i("Clima", "Permission denied");
    }

    // TODO: Add getWeatherForNewCity(String city) here:


    // TODO: Add getWeatherForCurrentLocation() here:


    // TODO: Add letsDoSomeNetworking(RequestParams params) here:


    // TODO: Add updateUI() here:


    // TODO: Add onPause() here:


}
