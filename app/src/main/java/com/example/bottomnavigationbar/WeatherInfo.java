package com.example.bottomnavigationbar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class WeatherInfo extends Fragment {

    private final int REQUEST_LOCATION_PERMISSION = 1;

    private LocationManager locationManager;
    //private String city_data, state_data, temperature_data, weather_summary_data;
    View view;
    TextView city, state, temperature, weather_summary;

    CardView weather_card;

    public void getLocation() throws IOException {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try{Location location = locationManager.getLastKnownLocation("gps");
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String city_name;
        String state_name;
        //temp.setText(Double.toString(latitude));
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
        if (addresses.size() > 0)
        {city_name=addresses.get(0).getLocality();
            state_name=addresses.get(0).getAdminArea();

            getWeather(city_name,state_name);}}catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Tag","here requesting permissions");
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() throws IOException {
        Log.d("Tag","forwarded here");
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(getActivity().getApplicationContext(), perms)) {
            Log.d("Tag","already have permission");
            Toast.makeText(getActivity(), "Permission already granted", Toast.LENGTH_SHORT).show();
            getLocation();
        }
        else {
//            Log.d("Tag","i dont have permissions");
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }



    public void getWeather(final String city_name, final String state_name){

        String url="https://api.openweathermap.org/data/2.5/weather?q="+city_name+"&units=metric&appid=ad3cff7c236b13b05f4db1e8177197be";
        //Log.d("url",url);
        JsonObjectRequest jor=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("response",response.toString());
                try {

                    JSONObject main=response.getJSONObject("main");
                    JSONArray weather=response.getJSONArray("weather");
                    JSONObject object=weather.getJSONObject(0);
                    String temp=String.valueOf(Math.round(main.getDouble("temp")));
                    String summary=object.getString("main");
                    switch(summary){
                        case "Clear":
                            weather_card.setBackgroundResource(R.drawable.clear_weather);
                            break;
                        case "Clouds":
                            weather_card.setBackgroundResource(R.drawable.cloudy_weather);
                            break;
                        case "Snow":
                            weather_card.setBackgroundResource(R.drawable.snowy_weather);
                            break;
                        case "Rain":
                            weather_card.setBackgroundResource(R.drawable.rainy_weather);
                            break;
                        case "Drizzle":
                            weather_card.setBackgroundResource(R.drawable.rainy_weather);
                            break;
                        case "Thunderstorm":
                            weather_card.setBackgroundResource(R.drawable.thunder_weather);
                            break;
                        default:
                            weather_card.setBackgroundResource(R.drawable.sunny_weather);
                            break;
                    }
                    city.setText(city_name);
                    state.setText(state_name);
                    temperature.setText(temp);
                    weather_summary.setText(summary);






                }catch(JSONException e){
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error","Volley Error");
            }
        });
        RequestQueue queue= Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(jor);
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_weather_info, container, false);
        city = view.findViewById((R.id.city));
        state = view.findViewById((R.id.state));
        temperature = view.findViewById((R.id.temperature));
        weather_summary = view.findViewById((R.id.weatherSummary));
        weather_card=view.findViewById(R.id.weatherCard);

        locationManager = (LocationManager)  getActivity().getSystemService(Context.LOCATION_SERVICE);
//
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            try {
                requestLocationPermission();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                getLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return view;
    }
}
