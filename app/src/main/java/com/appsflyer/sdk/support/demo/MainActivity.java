package com.appsflyer.sdk.support.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import java.util.Map;

//Main Entry for this sample app
public class MainActivity extends Activity {

    private final String devKey = "mkYxfjQyGL4iDYUSxkHua5";
    private boolean shouldUseDeferredStart = true;
    private boolean shouldUseApplicationContext = false;
    private boolean shouldUseUDL = true;
    private boolean shouldUseWaitForCUID = false;
    private boolean shouldUseAnonymizeUser = false;
    private boolean isStart = false;
    private boolean isStop = false;
    private boolean isInit = false;
    private String cuid = null;
    private String appInviteId = null;
    private String host = null;
    private String hostPrefix = null;
    private long udlTimeout = 3;
    private TextView status;
    private CheckBox deferredCb, appContextCb, udlCb, waitForCuidCb, anonymizeCb;
    private EditText hostEt, hostPrefixEt,udlTimeoutEt, cuidEt ,appInviteOneLinkEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppsFlyerLib.getInstance().setDebugLog(true);  //Setting debug logs is mandatory for the Demo app


        updateSDKStatus(); //Initial SDK status request
        initViews(); //Init views and logic
        initOptions(); //make sure all the configurations are fetched from persistent storage

        if(!shouldUseDeferredStart) {
            initSDK();
            startSDK();
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


    }

    private void initViews() {
        deferredCb = findViewById(R.id.deferred_start_toggle);
        appContextCb = findViewById(R.id.app_context_toggle);
        udlCb = findViewById(R.id.use_udl_toggle);
        waitForCuidCb = findViewById(R.id.wait_for_cuid_toggle);
        anonymizeCb = findViewById(R.id.anonymize_user_toggle);

        findViewById(R.id.init_sdk_btn).setOnClickListener(v ->  initSDK());
        findViewById(R.id.start_sdk_btn).setOnClickListener(v -> startSDK());
        findViewById(R.id.stop_sdk_btn).setOnClickListener(v -> stopSDK());

        hostEt = findViewById(R.id.host_et);
        hostEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String hostStr = s.toString();
                if(!hostStr.isEmpty()) {
                    host = s.toString();
                } else {
                    host = null;
                }

                DemoUtils.getInstance().saveToSP(MainActivity.this, "host", host);
            }
        });

        hostPrefixEt = findViewById(R.id.host_prefix_et);
        hostPrefixEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String hostPrefixStr = s.toString();
                if(!hostPrefixStr.isEmpty()) {
                    hostPrefix = s.toString();
                } else {
                    hostPrefix = null;
                }

                DemoUtils.getInstance().saveToSP(MainActivity.this, "hostPrefix", hostPrefix);
            }
        });


        cuidEt = findViewById(R.id.cuid_et);
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
                } else {
                    cuid = "";

                }
                DemoUtils.getInstance().saveToSP(MainActivity.this, "cuid", cuid);
            }
        });

        appInviteOneLinkEt = findViewById(R.id.app_invite_onelink_id_timeout_et);
        appInviteOneLinkEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String appInviteIdStr = s.toString();
                if(!appInviteIdStr.isEmpty()) {
                    appInviteId = s.toString();
                } else {
                    appInviteId = "";

                }
                DemoUtils.getInstance().saveToSP(MainActivity.this, "appInviteOneLinkId", appInviteId);
            }
        });

        udlTimeoutEt = findViewById(R.id.udl_timeout_et);
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



        deferredCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shouldUseDeferredStart = isChecked;
                DemoUtils.getInstance().saveToSP(MainActivity.this, "is_deferred", isChecked);
            }
        });

        appContextCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shouldUseApplicationContext = isChecked;
                DemoUtils.getInstance().saveToSP(MainActivity.this, "appContext", isChecked);
            }
        });

        udlCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shouldUseUDL = isChecked;
                DemoUtils.getInstance().saveToSP(MainActivity.this, "is_udl", isChecked);
            }
        });

        waitForCuidCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shouldUseWaitForCUID = isChecked;
                DemoUtils.getInstance().saveToSP(MainActivity.this, "waitForCUID", isChecked);
            }
        });

        anonymizeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DemoUtils.getInstance().saveToSP(MainActivity.this, "anonymize", isChecked);
            }
        });

    }

    private void stopSDK() {
        isStop = true;
        isStart = false;
        updateSDKStatus();
        AppsFlyerLib.getInstance().stop(true, this);
    }

    private void initOptions() {

        deferredCb.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "is_deferred"));
        appContextCb.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "appContext"));
        udlCb.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "is_udl"));
        waitForCuidCb.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "waitForCUID"));
        anonymizeCb.setChecked(DemoUtils.getInstance().getBooleanFromSP(MainActivity.this, "anonymize"));

        udlTimeoutEt.setText(DemoUtils.getInstance().getStringFromSP(MainActivity.this, "udlTimeout"));
        cuidEt.setText(DemoUtils.getInstance().getStringFromSP(MainActivity.this, "cuid"));
        appInviteOneLinkEt.setText(DemoUtils.getInstance().getStringFromSP(MainActivity.this, "appInviteOneLinkId"));
        udlTimeout = udlTimeoutEt.getText().toString().equals("") ? 3 : Long.parseLong(udlTimeoutEt.getText().toString());
        DemoUtils.getInstance().saveToSP(MainActivity.this, "udl", udlTimeout);
        cuid = cuidEt.getText().toString();
        appInviteId = appInviteOneLinkEt.getText().toString();
        DemoUtils.getInstance().saveToSP(MainActivity.this, "cuid", cuid);
        hostEt.setText(DemoUtils.getInstance().getStringFromSP(MainActivity.this, "host"));
        host = hostEt.getText().toString().equals("") ? null : hostEt.getText().toString();
        hostPrefixEt.setText(DemoUtils.getInstance().getStringFromSP(MainActivity.this, "hostPrefix"));
        hostPrefix = hostPrefixEt.getText().toString().equals("") ? null : hostPrefixEt.getText().toString();

        shouldUseDeferredStart = deferredCb.isChecked();
        shouldUseApplicationContext = appContextCb.isChecked();
        shouldUseUDL = udlCb.isChecked();
        shouldUseWaitForCUID = waitForCuidCb.isChecked();
        shouldUseAnonymizeUser = anonymizeCb.isChecked();
    }

    private void initSDK() {

        if(isInit) {
            DemoUtils.getInstance().showToastMessage(this, "SDK already initialized..");
            return;
        }

        isInit = true;
        updateSDKStatus();

        setCuidIfNeeded();
        setHostIfNeeded();

        setUDLIfNeeded();

        if(appInviteId != null && !appInviteId.isEmpty()) {
            AppsFlyerLib.getInstance().setAppInviteOneLink(appInviteId);
        }

        if(shouldUseAnonymizeUser) {
            AppsFlyerLib.getInstance().anonymizeUser(true);
        }

        if(shouldUseWaitForCUID) {
            AppsFlyerLib.getInstance().waitForCustomerUserId(true);
        }

        Context ctx = this;
        if(shouldUseApplicationContext) {
            ctx = ctx.getApplicationContext();
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
        }, ctx);

    }

    private void setUDLIfNeeded() {
        if(shouldUseUDL) {
            AppsFlyerLib.getInstance().subscribeForDeepLink(deepLinkResult -> DemoUtils.getInstance().handleUDLResponse(MainActivity.this, deepLinkResult), udlTimeout * 1000);
        }
    }

    private void setHostIfNeeded() {
        if(host == null || host.isEmpty()) {
            AppsFlyerLib.getInstance().setHost("", "appsflyer.com");
        } else {
            AppsFlyerLib.getInstance().setHost(hostPrefix, host);
        }
    }

    private void startSDK() {

        if(isStart) {
            DemoUtils.getInstance().showToastMessage(this, "SDK already started..");
            return;
        }

        AppsFlyerLib.getInstance().stop(false, this);

        setCuidIfNeeded();
        setHostIfNeeded();

        if(shouldUseWaitForCUID) {
            if(cuid == null || cuid.isEmpty()) {
                DemoUtils.getInstance().showToastMessage(MainActivity.this, "SDK didn't start since CUID is not set, please set CUID");
            } else {
                isStart = true;
                if(shouldUseApplicationContext) {
                    AppsFlyerLib.getInstance().setCustomerIdAndLogSession(cuid, MainActivity.this.getApplicationContext());
                } else {
                    AppsFlyerLib.getInstance().setCustomerIdAndLogSession(cuid, MainActivity.this);
                }

            }

        } else {
            isStart = true;
            if(shouldUseApplicationContext) {
                AppsFlyerLib.getInstance().start(this.getApplicationContext());
            } else {
                AppsFlyerLib.getInstance().start(this);
            }
        }

        isStop = false;

        updateSDKStatus();
    }

    private void setCuidIfNeeded() {
        if(!shouldUseWaitForCUID) {
            if(cuid != null && !cuid.isEmpty()) {
                AppsFlyerLib.getInstance().setCustomerUserId(cuid);
            }
        }

    }

    private void updateSDKStatus() {

        status = findViewById(R.id.sdk_status);

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