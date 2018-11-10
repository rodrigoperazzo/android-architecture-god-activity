package com.rperazzo.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rperazzo.weatherapp.WeatherManager.FindResult;
import com.rperazzo.weatherapp.WeatherManager.WeatherService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCE_NAME = "com.rperazzo.weatherapp.shared";
    private static final String TEMPERATURE_UNIT_KEY = "TEMPERATURE_UNIT_KEY";

    private SharedPreferences mSharedPref;
    private EditText mEditText;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private ListView mList;
    private FindItemAdapter mAdapter;
    private ArrayList<WeatherManager.City> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPref = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        mEditText = (EditText) findViewById(R.id.editText);
        mTextView = (TextView) findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mList = (ListView) findViewById(R.id.list);

        mAdapter = new FindItemAdapter(this, cities);
        mList.setAdapter(mAdapter);

        mEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchByName();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_celcius) {
            updateUnitIfNecessary("metric");
            return true;
        } else if (id == R.id.menu_fahrenheit) {
            updateUnitIfNecessary("imperial");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUnitIfNecessary(String newUnits) {
        String currentUnits = getTemperatureUnit();
        if (!currentUnits.equals(newUnits)) {
            setTemperatureUnit(newUnits);
            searchByName();
        }
    }

    public void onSearchClick(View view) {
        searchByName();
    }

    private void onStartLoading() {
        mList.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                     getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void onFinishLoading(WeatherManager.FindResult result){

        mProgressBar.setVisibility(View.GONE);
        cities.clear();

        if (result != null && result.list.size() > 0) {
            cities.addAll(result.list);
            mList.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            mTextView.setText("No results.");
        }
    }

    private void onFinishLoadingWithError() {
        mProgressBar.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
        mTextView.setText("Error");
    }

    public boolean isDeviceConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void searchByName() {
        if (!isDeviceConnected()) {
            Toast.makeText(this, "No connection!", Toast.LENGTH_LONG).show();
            return;
        }

        String search = mEditText.getText().toString();
        if (TextUtils.isEmpty(search)) {
            return;
        }

        onStartLoading();

        WeatherService wService = WeatherManager.getService();
        String units = getTemperatureUnit();
        final Call<FindResult> findCall = wService.find(search, units, WeatherManager.API_KEY);
        findCall.enqueue(new Callback<FindResult>() {
            @Override
            public void onResponse(Call<FindResult> call, Response<FindResult> response) {
                onFinishLoading(response.body());
            }

            @Override
            public void onFailure(Call<FindResult> call, Throwable t) {
                onFinishLoadingWithError();
            }
        });
    }

    public void setTemperatureUnit(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(TEMPERATURE_UNIT_KEY, value);
        editor.apply();
    }

    public String getTemperatureUnit() {
        return mSharedPref.getString(TEMPERATURE_UNIT_KEY, "metric");
    }

    public class FindItemAdapter extends ArrayAdapter<WeatherManager.City> {

        public FindItemAdapter(Context context, ArrayList<WeatherManager.City> cities) {
            super(context, 0, cities);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final WeatherManager.City city = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.city_list_item, parent, false);
            }
            TextView cityName = convertView.findViewById(R.id.cityNameTxt);
            cityName.setText(city.getTitle());

            TextView description = convertView.findViewById(R.id.descriptionTxt);
            description.setText(city.getDescription());

            TextView metric = convertView.findViewById(R.id.metricTxt);
            String units = getTemperatureUnit();
            if ("metric".equals(units)) {
                metric.setText("ºC");
            } else {
                metric.setText("ºF");
            }

            TextView temp = convertView.findViewById(R.id.tempTxt);
            temp.setText(city.getTemperature());

            TextView wind = convertView.findViewById(R.id.windTxt);
            if ("metric".equals(units)) {
                wind.setText(city.getWind() + " m/s");
            } else {
                wind.setText(city.getWind() + " m/h");
            }

            TextView clouds = convertView.findViewById(R.id.cloudsTxt);
            clouds.setText(city.getClouds());

            TextView pressure = convertView.findViewById(R.id.pressureTxt);
            pressure.setText(city.getPressure());

            int resId = getContext().getResources().getIdentifier(
                    "w_"+city.weather.get(0).icon,
                    "drawable",
                    getContext().getPackageName());

            ImageView icon = convertView.findViewById(R.id.weatherIcon);
            icon.setImageResource(resId);

            return convertView;
        }
    }
}
