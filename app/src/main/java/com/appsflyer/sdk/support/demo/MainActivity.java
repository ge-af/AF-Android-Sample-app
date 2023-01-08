package com.appsflyer.sdk.support.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.deeplink.DeepLinkResult;

import java.util.Map;

public class MainActivity extends Activity {

    private final String devKey = "mkYxfjQyGL4iDYUSxkHua5";
    private boolean shouldUseDeferredStart = true;
    private boolean shouldInitWithGCD = false;
    private boolean shouldUseApplicationContext = false;
    private boolean useUDL = true;
    private long udlTimeout = 3;
    private boolean shouldUseWaitForCUID = false;
    private boolean shouldUseAnonymizeUser = false;
    private String cuid = "default_cuid";
    private boolean isInit = false;
    private boolean isStart = false;
    private boolean isStop = false;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.sdk_status);

        AppsFlyerLib.getInstance().setDebugLog(true);
        updateSDKStatus();
        initOptions();

        if(!shouldUseDeferredStart) { // Take from Prefs, default is deferred start.
            initSDK();
            if(useUDL) {
                AppsFlyerLib.getInstance().subscribeForDeepLink(deepLinkResult -> DemoUtils.getInstance().handleUDLResponse(MainActivity.this, deepLinkResult), udlTimeout * 1000);
            }
            startSDK();
        } else {
           DemoUtils.getInstance().showToastMessage(this,"SDK wasn't start, set to use deferred start");
        }

        findViewById(R.id.more_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, MoreActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.clear_all_button).setOnClickListener(v -> {
            DemoUtils.getInstance().showClearDataPopup(this, new OnOptionChangedListener() {
                @Override
                public void onReset(boolean isReset) {
                    if(isReset) {
                        initOptions();
                    }
                }
            });

        });

        findViewById(R.id.init_sdk_btn).setOnClickListener(v -> { initSDK(); });

        findViewById(R.id.start_sdk_btn).setOnClickListener(v -> startSDK());

        findViewById(R.id.stop_sdk_btn).setOnClickListener(v -> stopSDK());
    }

    private void stopSDK() {
        isStop = true;
        updateSDKStatus();
        AppsFlyerLib.getInstance().stop(true, this);
    }

    private void initOptions() {
        EditText cuidEt = findViewById(R.id.cuid_et);
        cuidEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String cuidStr = s.toString();
                if(!cuidStr.isEmpty()) {
                    cuid = s.toString();
                    DemoUtils.getInstance().saveToSP(MainActivity.this, "cuid", cuid);
                }
            }
        });

        EditText udlTimeoutEt = findViewById(R.id.udl_timeout_et);
        udlTimeoutEt.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        udlTimeoutEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String udlTimeOutStr = s.toString();
                if(!udlTimeOutStr.isEmpty()) {
                    udlTimeout = Integer.parseInt(s.toString());
                } else {
                    udlTimeout = 3000;
                }
                DemoUtils.getInstance().saveToSP(MainActivity.this, "udlTimeout", udlTimeOutStr);
            }
        });

        CheckBox deferred = findViewById(R.id.deferred_start_toggle);
        CheckBox appContext = findViewById(R.id.app_context_toggle);
        CheckBox udl = findViewById(R.id.use_udl_toggle);
        CheckBox waitForCUID = findViewById(R.id.wait_for_cuid_toggle);
        CheckBox anonymize = findViewById(R.id.anonymize_user_toggle);

        deferred.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DemoUtils.getInstance().saveToSP(MainActivity.this, "is_deferred", isChecked);
            }
        });

        appContext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DemoUtils.getInstance().saveToSP(MainActivity.this, "appContext", isChecked);
            }
        });

        udl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DemoUtils.getInstance().saveToSP(MainActivity.this, "is_udl", isChecked);
            }
        });

        waitForCUID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DemoUtils.getInstance().saveToSP(MainActivity.this, "waitForCUID", isChecked);
            }
        });

        anonymize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DemoUtils.getInstance().saveToSP(MainActivity.this, "anonymize", isChecked);
            }
        });



        deferred.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "is_deferred"));
        appContext.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "appContext"));
        udl.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "is_udl"));
        waitForCUID.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "waitForCUID"));
        anonymize.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "anonymize"));
        udlTimeoutEt.setText(DemoUtils.getInstance().getStringFromSP(MainActivity.this, "udlTimeout"));
        cuidEt.setText(DemoUtils.getInstance().getStringFromSP(MainActivity.this, "cuid"));
        udlTimeout = udlTimeoutEt.getText().toString().equals("") ? 3 : Long.parseLong(udlTimeoutEt.getText().toString());
        DemoUtils.getInstance().saveToSP(MainActivity.this, "udl", udlTimeout);
        cuid = cuidEt.getText().toString();
        DemoUtils.getInstance().saveToSP(MainActivity.this, "udl", cuid);

        shouldUseDeferredStart = deferred.isChecked();
        shouldUseApplicationContext = appContext.isChecked();
        useUDL = udl.isChecked();
        shouldUseWaitForCUID = waitForCUID.isChecked();
        shouldUseAnonymizeUser = anonymize.isChecked();
    }

    private void initSDK() {

        isInit = true;
        updateSDKStatus();
        if(cuid != null && !cuid.isEmpty()) {
            AppsFlyerLib.getInstance().setCustomerUserId(cuid);
        }
        if(useUDL) {
            AppsFlyerLib.getInstance().subscribeForDeepLink(deepLinkResult -> DemoUtils.getInstance().handleUDLResponse(MainActivity.this, deepLinkResult), udlTimeout * 1000);
        }

        if(shouldUseAnonymizeUser) {
            AppsFlyerLib.getInstance().anonymizeUser(true);
        }

        AppsFlyerLib.getInstance().init(devKey, new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) { DemoUtils.getInstance().handleGcdData(MainActivity.this, map); }
            @Override
            public void onConversionDataFail(String s) { DemoUtils.getInstance().handleGcdFailure(MainActivity.this, s); }
            @Override
            public void onAppOpenAttribution(Map<String, String> map) { DemoUtils.getInstance().handleOAOA(MainActivity.this, map); }
            @Override
            public void onAttributionFailure(String s) { DemoUtils.getInstance().handleOAOAFailure(MainActivity.this, s);}
        }, this);

    }

    private void startSDK() {
        AppsFlyerLib.getInstance().stop(false, this);

        if(cuid != null && !cuid.isEmpty()) {
            AppsFlyerLib.getInstance().setCustomerUserId(cuid);
        }

        if(shouldUseWaitForCUID) {
            AppsFlyerLib.getInstance().setCustomerIdAndLogSession(cuid, this);
        } else {
            if(shouldUseApplicationContext) {
                AppsFlyerLib.getInstance().start(this.getApplicationContext());
            } else {
                AppsFlyerLib.getInstance().start(this);
            }
        }

        isStop = false;
        isStart = true;
        updateSDKStatus();
    }

    private void updateSDKStatus() {
        String statusStr = "The SDK is ";
        if(isInit) {
            statusStr = statusStr + "initialized ";
        } else {
            statusStr = statusStr + "not initialized ";
        }

        if(isStop) {
            statusStr = statusStr +"and stopped";
        } else {
            if(isStart) {
                statusStr = statusStr +"and started";
            } else {
                statusStr = statusStr +"and not started";
            }
        }

        status.setText(statusStr);
    }
}