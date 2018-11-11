package com.rperazzo.weatherapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rperazzo.weatherapp.R;
import com.rperazzo.weatherapp.model.settings.SettingsRepository;
import com.rperazzo.weatherapp.model.settings.SettingsRepositoryImpl;
import com.rperazzo.weatherapp.model.settings.local.SettingsLocal;
import com.rperazzo.weatherapp.model.settings.local.SettingsLocalImpl;
import com.rperazzo.weatherapp.model.weather.City;
import com.rperazzo.weatherapp.model.weather.WeatherRepository;
import com.rperazzo.weatherapp.model.weather.WeatherRepositoryImpl;
import com.rperazzo.weatherapp.model.weather.remote.WeatherRemote;
import com.rperazzo.weatherapp.model.weather.remote.WeatherRemoteImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WeatherView {

    private static final String PREFERENCE_NAME = "com.rperazzo.weatherapp.shared";
    private static final String TEMPERATURE_UNIT_KEY = "TEMPERATURE_UNIT_KEY";

    private SharedPreferences mSharedPref;
    private EditText mEditText;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private ListView mList;
    private FindItemAdapter mAdapter;
    private ArrayList<City> cities = new ArrayList<>();

    WeatherRepository mWeatherRepository;
    SettingsRepository mSettingsRepository;

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

        WeatherRemote weatherRemote = new WeatherRemoteImpl();
        mWeatherRepository = new WeatherRepositoryImpl(this, weatherRemote);

        SettingsLocal settingsLocal = new SettingsLocalImpl(this);
        mSettingsRepository = new SettingsRepositoryImpl(settingsLocal);
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
        String currentUnits = mSettingsRepository.getTemperatureUnit();
        if (!currentUnits.equals(newUnits)) {
            mSettingsRepository.setTemperatureUnit(newUnits);
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

    @Override
    public void onFinishLoading(List<City> list) {

        mProgressBar.setVisibility(View.GONE);
        cities.clear();

        if (list != null && list.size() > 0) {
            cities.addAll(list);
            mList.setVisibility(View.VISIBLE);
            mAdapter.setUnits(mSettingsRepository.getTemperatureUnit());
            mAdapter.notifyDataSetChanged();
        } else {
            mTextView.setText("No results.");
        }
    }

    @Override
    public void onFinishLoadingWithError(String error) {
        mProgressBar.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
        mTextView.setText(error);
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
        String units = mSettingsRepository.getTemperatureUnit();

        mWeatherRepository.search(search, units);
    }

}
