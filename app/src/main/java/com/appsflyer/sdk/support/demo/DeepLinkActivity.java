package com.appsflyer.sdk.support.demo;

import static com.appsflyer.sdk.support.demo.MainActivity.devKey;
import static com.appsflyer.sdk.support.demo.MainActivity.isInit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.appsflyer.deeplink.DeepLinkListener;
import com.appsflyer.deeplink.DeepLinkResult;

public class DeepLinkActivity extends Activity {


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        setUpDeepLink();
    }

    private void setUpDeepLink() {
        String str = getIntent().getStringExtra("deep_link_intent_data");

        if(str != null) {
            ((TextView)findViewById(R.id.deep_link_tv)).setText(str);
        } else {
            AppsFlyerLib.getInstance().subscribeForDeepLink(new DeepLinkListener() {
                @Override
                public void onDeepLinking(DeepLinkResult deepLinkResult) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.deep_link_tv)).setText(deepLinkResult.toString());
                        }
                    });
                }
            });
        }
    }
}