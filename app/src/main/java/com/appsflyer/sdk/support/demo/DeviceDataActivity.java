package com.appsflyer.sdk.support.demo;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import java.io.IOException;

public class DeviceDataActivity extends Activity {

    private static boolean isDisabled = false;
    private static boolean isAnonymized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_data);

        fetchGAID();
        fetchIMEI();
        fetchAndroidID();
        updateStatus();

        ((CheckBox)findViewById(R.id.anonymize_user_id_btn)).setChecked(isAnonymized);
        ((CheckBox)findViewById(R.id.disable_identifiers_btn)).setChecked(isDisabled);

        ((CheckBox)findViewById(R.id.anonymize_user_id_btn)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isAnonymized = isChecked;
                AppsFlyerLib.getInstance().anonymizeUser(isChecked);
                updateStatus();
            }
        });

        ((CheckBox)findViewById(R.id.disable_identifiers_btn)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isDisabled = isChecked;
                AppsFlyerLib.getInstance().setDisableAdvertisingIdentifiers(isChecked);
                updateStatus();
            }
        });
    }

    private void updateStatus() {
        ((TextView)findViewById(R.id.disable_identifier_bool_tv)).setText("Disable Advertising Identifiers: "+ (isDisabled ?"true" : "false"));
        ((TextView)findViewById(R.id.anonymize_bool_tv)).setText("Anonymize User: "+ (isAnonymized? "true": "false"));
    }

    private void fetchAndroidID() {
        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        ((TextView)findViewById(R.id.android_id_value_tv)).setText(deviceId);
        ((TextView)findViewById(R.id.android_id_value_tv)).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied text", ((TextView)findViewById(R.id.android_id_value_tv)).getText().toString());
            clipboard.setPrimaryClip(clip);
            DemoUtils.getInstance().showToastMessage(DeviceDataActivity.this, "Copied to Clipboard");
        });
    }

    private void fetchIMEI() {
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            ((TextView)findViewById(R.id.imei_value_tv)).setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied text", ((TextView)findViewById(R.id.imei_value_tv)).getText().toString());
                clipboard.setPrimaryClip(clip);
                DemoUtils.getInstance().showToastMessage(DeviceDataActivity.this, "Copied to Clipboard");
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            imei = "Could not fetch IMEI";
        }

        ((TextView)findViewById(R.id.imei_value_tv)).setText(imei);
    }

    private void fetchGAID() {
        Thread gaidThread = new Thread(){
            public void run(){
                AdvertisingIdClient.Info idInfo = null;
                String value;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                    value = idInfo.getId();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.gaid_value_tv)).setOnClickListener(v -> {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Copied text", ((TextView)findViewById(R.id.gaid_value_tv)).getText().toString());
                                clipboard.setPrimaryClip(clip);
                                DemoUtils.getInstance().showToastMessage(DeviceDataActivity.this, "Copied to Clipboard");
                            });
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    value = "Error collecting GAID";
                } catch (GooglePlayServicesNotAvailableException e) {
                    value = "Google Play Services are not available on the device";
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    value = "Google Play services is not installed, up-to-date, or enabled on the device";
                    e.printStackTrace();
                }

                String finalValue = value;
                runOnUiThread(() -> ((TextView)findViewById(R.id.gaid_value_tv)).setText(finalValue));
            }
        };
        gaidThread.start();
    }
}