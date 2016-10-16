package com.example.ankit.homify;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.nestlabs.sdk.GlobalUpdate;
import com.nestlabs.sdk.NestAPI;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.NestListener;
import com.nestlabs.sdk.NestToken;
import com.nestlabs.sdk.Structure;
import com.nestlabs.sdk.Thermostat;

public class ThermoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ThermoActivity.class.getSimpleName();

    //private LightActivityFragment mLightActivity;

    SharedPreferences pref;
    private final String high_key = "high";
    private final String low_key = "low";
    private final String default_high = "80";
    private final String default_low = "60";
    private int high_pref;
    private int low_pref;

    private static final String THERMOSTAT_KEY = "thermostat_key";
    private static final String STRUCTURE_KEY = "structure_key";
    private static final String DEG_F = "%d°F";
    private static final String KEY_AWAY = "away";
    private static final String KEY_AUTO_AWAY = "auto-away";
    private static final String KEY_HOME = "home";

    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    private static final String REDIRECT_URL = Constants.REDIRECT_URL;
    private static final int AUTH_TOKEN_REQUEST_CODE = 111;

    private TextView mCurrTemp;
    private TextView mTargetTemp;

    private Button tile1;
    private Button tile2;
    private Button scatter;
    private Switch mSwitch;

    private NestAPI mNest;
    private NestToken mToken;
    private Thermostat mThermostat;
    private Structure mStructure;
    private Activity mActivity;

    // variable to keep track of away state
    private boolean isaway = false;
    private boolean shouldscatter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermo);
        mActivity = this;
        mTargetTemp = (TextView) findViewById(R.id.temp_target);
        mCurrTemp = (TextView) findViewById(R.id.temp_curr);
        //mLightActivity = new LightActivityFragment();

        LayoutInflater inflator = getLayoutInflater();
        View myview = inflator.inflate(R.layout.activity_light, null);
        tile1 = (Button) myview.findViewById(R.id.light1);
        tile2 = (Button) myview.findViewById(R.id.light2);
        scatter = (Button) myview.findViewById(R.id.terror);

        mSwitch = (Switch) findViewById(R.id.set_off);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        high_pref = Integer.parseInt(pref.getString(high_key, default_high));
        low_pref = Integer.parseInt(pref.getString(low_key, default_low));
        System.out.println("high temp " + high_pref);
        System.out.println("low temp " + low_pref);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.temp_incr).setOnClickListener(this);
        findViewById(R.id.temp_decr).setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Authentication Procedure
        NestAPI.setAndroidContext(this);
        mNest = NestAPI.getInstance();

        mToken = ThermoSettings.loadAuthToken(this);

        if (mToken != null) {
            authenticate(mToken);
        } else {
            mNest.setConfig(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
            mNest.launchAuthFlow(this, AUTH_TOKEN_REQUEST_CODE);
        }

        if (savedInstanceState != null) {
            mThermostat = savedInstanceState.getParcelable(THERMOSTAT_KEY);
            mStructure = savedInstanceState.getParcelable(STRUCTURE_KEY);
        }

        updateViews();

        mSwitch.setChecked(false);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tile1.performClick();
                    tile2.performClick();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thermo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(THERMOSTAT_KEY, mThermostat);
        outState.putParcelable(STRUCTURE_KEY, mStructure);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK || requestCode != AUTH_TOKEN_REQUEST_CODE) {
            Log.e(TAG, "Finished with no result.");
            return;
        }

        mToken = NestAPI.getAccessTokenFromIntent(intent);
        if (mToken != null) {
            ThermoSettings.saveAuthToken(this, mToken);
            authenticate(mToken);
        } else {
            Log.e(TAG, "Unable to resolve access token from payload.");
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        mNest.removeAllListeners();
    }

    /**
     * Authenticate with the Nest API and start listening for updates.
     *
     * @param token the token used to authenticate.
     */
    private void authenticate(NestToken token) {
        mNest.authWithToken(token, new NestListener.AuthListener() {

            @Override
            public void onAuthSuccess() {
                Log.v(TAG, "Authentication succeeded.");
                fetchData();
            }

            @Override
            public void onAuthFailure(NestException exception) {
                Log.e(TAG, "Authentication failed with error: " + exception.getMessage());
                ThermoSettings.saveAuthToken(mActivity, null);
                mNest.launchAuthFlow(mActivity, AUTH_TOKEN_REQUEST_CODE);
            }

            @Override
            public void onAuthRevoked() {
                Log.e(TAG, "Auth token was revoked!");
                ThermoSettings.saveAuthToken(mActivity, null);
                mNest.launchAuthFlow(mActivity, AUTH_TOKEN_REQUEST_CODE);
            }
        });
    }

    /**
     * Setup global listener, start listening, and update view when update received.
     */
    private void fetchData() {
        mNest.addGlobalListener(new NestListener.GlobalListener() {
            @Override
            public void onUpdate(@NonNull GlobalUpdate update) {
                mThermostat = update.getThermostats().get(0);
                mStructure = update.getStructures().get(0);
                String awayState = mStructure.getAway();
                long temp = mThermostat.getAmbientTemperatureF();
                //System.out.println("temp is: " + temp);
                /*
                 * Set up the settings for low and high ambient temperature threshold.
                 * Get temperature from shared preferences
                 * if away state and ambient temperature within range, turn off all lights
                 * if temperature not in  ambient range turn on red lights
                 */
                if (isaway != true && (KEY_AUTO_AWAY.equals(awayState) || KEY_AWAY.equals(awayState))) {
                    tile1.performClick();
                    tile2.performClick();
                    isaway = true;
                } else if (KEY_HOME.equals(awayState)) {
                    isaway = false;
                    //System.out.println("isaway is: " + isaway);
                }

                if (shouldscatter != true && (temp > high_pref || temp < low_pref)) {
                    scatter.performClick();
                    shouldscatter = true;
                } else if (temp < high_pref && temp > low_pref) {
                    shouldscatter = false;
                }
                    updateViews();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mThermostat == null || mStructure == null) {
            return;
        }

        String thermostatID = mThermostat.getDeviceId();
        //System.out.println("ThermoID " + thermostatID);
        String mode = mThermostat.getHvacMode();
        //System.out.println("Mode " + mode);
        String awayState = mStructure.getAway();
        long temp = mThermostat.getTargetTemperatureF();
        //System.out.println("Temp "+temp);

        switch (v.getId()) {
            case R.id.temp_incr:
                ++temp;
                //System.out.println("Temp Incr " + temp);
                mTargetTemp.setText(String.format(DEG_F, temp));
                mNest.thermostats.setTargetTemperatureF(mThermostat.getDeviceId(), temp);
                break;
            case R.id.temp_decr:
                --temp;
                //System.out.println("Temp Decr " + temp);
                mTargetTemp.setText(String.format(DEG_F, temp));
                mNest.thermostats.setTargetTemperatureF(mThermostat.getDeviceId(), temp);
                break;
        }
    }

    private void updateViews() {
        if (mStructure == null || mThermostat == null) {
            return;
        }

        long targetTemp = mThermostat.getTargetTemperatureF();
        long currentTemp = mThermostat.getAmbientTemperatureF();
        mTargetTemp.setText(String.format(DEG_F, targetTemp));
        mCurrTemp.setText(String.format(DEG_F, currentTemp));
    }
}
