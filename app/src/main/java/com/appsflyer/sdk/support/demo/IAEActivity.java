package com.appsflyer.sdk.support.demo;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class IAEActivity extends Activity {

    private int counter = 0;
    private int purchaseEventCounter = 0;
    private EditText eventName;
    private EditText eventParams;
    private TextView output_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_iae);

        eventName = findViewById(R.id.custom_event_name);
        eventParams = findViewById(R.id.custom_json_et);
        output_tv = findViewById(R.id.output_tv);
        findViewById(R.id.regular_iae_button).setOnClickListener(v -> {
            String eventNameStr = "Demo Regular Event";
            Map<String, Object> map = new HashMap<>();
            map.put("app", "Demo App");
            map.put("version", BuildConfig.VERSION_NAME);
            map.put("counter", ++counter);
            map.put("timestamp", System.currentTimeMillis());
            map.put("af_id", AppsFlyerLib.getInstance().getAppsFlyerUID(IAEActivity.this));
            AppsFlyerLib.getInstance().logEvent(IAEActivity.this, eventNameStr, map, new AppsFlyerRequestListener() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String str = "Success!\n\n";
                            str = str + "Event Name: \n"+ eventNameStr;
                            str = str + "\n\nEvent values: \n";
                            str = str + new Gson().toJson(map);
                            output_tv.setText(str);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String str = "Error..\n\n"+ s;
                            output_tv.setText(str);
                        }
                    });
                }
            });
        });

        findViewById(R.id.purchase_iae_button).setOnClickListener(v -> {
            String eventNameStr = "Demo Purchase Event";
            Map<String, Object> map = new HashMap<>();
            map.put(AFInAppEventParameterName.PRICE, 7);
            map.put(AFInAppEventParameterName.REVENUE, 5);
            map.put(AFInAppEventParameterName.CURRENCY, "USD");
            map.put(AFInAppEventParameterName.COUPON_CODE, "CODE_20");
            map.put("purchaseEventCounter", ++purchaseEventCounter);
            map.put("timestamp", System.currentTimeMillis());
            map.put("af_id", AppsFlyerLib.getInstance().getAppsFlyerUID(IAEActivity.this));
            AppsFlyerLib.getInstance().logEvent(IAEActivity.this, eventNameStr, map, new AppsFlyerRequestListener() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String str = "Success!\n\n";
                            str = str + "Event Name: \n"+ eventNameStr;
                            str = str + "\n\nEvent values: \n";
                            str = str + new Gson().toJson(map);
                            output_tv.setText(str);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String str = "Error..\n\n"+ s;
                            output_tv.setText(str);
                        }
                    });
                }
            });
        });

        findViewById(R.id.custom_iae_button).setOnClickListener(v -> {
            String eventNameStr = eventName.getText().toString();
            if(eventNameStr != null || !eventNameStr.isEmpty()) {
                try{
                    HashMap<String, Object> map = new Gson().fromJson(eventParams.getText().toString(), HashMap.class);
                    AppsFlyerLib.getInstance().logEvent(IAEActivity.this, eventNameStr, map, new AppsFlyerRequestListener() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String str = "Success!\n\n";
                                    str = str + "Event Name: \n"+ eventNameStr;
                                    str = str + "\n\nEvent values: \n";
                                    str = str + new Gson().toJson(map);
                                    output_tv.setText(str);
                                }
                            });
                        }

                        @Override
                        public void onError(int i, String s) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String str = "Error..\n\n"+ s;
                                    output_tv.setText(str);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    DemoUtils.getInstance().showToastMessage(IAEActivity.this, "Please make sure to input a valid JSON");
                }

            } else {
                DemoUtils.getInstance().showToastMessage(IAEActivity.this, "Please make sure to input a valid eventName");
            }
        });
    }
}
