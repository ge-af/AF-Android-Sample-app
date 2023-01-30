package com.appsflyer.sdk.support.demo;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.appsflyer.CreateOneLinkHttpTask;
import com.appsflyer.share.LinkGenerator;
import com.appsflyer.share.ShareInviteHelper;

public class UserInviteActivity extends Activity {

    private TextView output_tv;
    private EditText pid_et, deep_link_value_et, deep_link_sub1_et, deep_link_sub2_et, deep_link_sub3_et, deep_link_sub4_et, deep_link_sub5_et,
            deep_link_sub6_et, deep_link_sub7_et, deep_link_sub8_et, deep_link_sub9_et, deep_link_sub10_et, campaign_et, channel_et;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_invite);
        output_tv = findViewById(R.id.output_tv);
        output_tv.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied text", output_tv.getText().toString());
            clipboard.setPrimaryClip(clip);
            DemoUtils.getInstance().showToastMessage(UserInviteActivity.this, "Copied to Clipboard");
        });

        channel_et = findViewById(R.id.channel_et);
        campaign_et = findViewById(R.id.campaign_et);
        deep_link_value_et = findViewById(R.id.deep_link_value_et);
        pid_et = findViewById(R.id.pid_et);
        deep_link_sub1_et = findViewById(R.id.deep_link_sub1_et);
        deep_link_sub2_et = findViewById(R.id.deep_link_sub2_et);
        deep_link_sub3_et = findViewById(R.id.deep_link_sub3_et);
        deep_link_sub4_et = findViewById(R.id.deep_link_sub4_et);
        deep_link_sub5_et = findViewById(R.id.deep_link_sub5_et);
        deep_link_sub6_et = findViewById(R.id.deep_link_sub6_et);
        deep_link_sub7_et = findViewById(R.id.deep_link_sub7_et);
        deep_link_sub8_et = findViewById(R.id.deep_link_sub8_et);
        deep_link_sub9_et = findViewById(R.id.deep_link_sub9_et);
        deep_link_sub10_et = findViewById(R.id.deep_link_sub10_et);

        findViewById(R.id.generate_user_invite_nbtn).setOnClickListener(v -> {
            generate();
        });
    }


    private void generate() {
        LinkGenerator.ResponseListener listener = new LinkGenerator.ResponseListener() {
            @Override
            public void onResponse(String userInviteURL) {
                output_tv.setText(userInviteURL);
            }

            @Override
            public void onResponseError(String error) {
                output_tv.setText(error);
            }
        };

        LinkGenerator linkGenerator = ShareInviteHelper.generateInviteUrl(getApplicationContext());

        if(channel_et.getText() != null && !channel_et.getText().toString().isEmpty()) {
            linkGenerator.setChannel(channel_et.getText().toString());
        } else {
            linkGenerator.setChannel("demo_app_default_channel");
        }

        if(campaign_et.getText() != null && !campaign_et.getText().toString().isEmpty()) {
            linkGenerator.setCampaign(campaign_et.getText().toString());
        } else {
            linkGenerator.setCampaign("demo_app_user_invite_default_campaign");
        }

        if(pid_et.getText() != null && !pid_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("pid", pid_et.getText().toString());
        }

        if(deep_link_value_et.getText() != null && !deep_link_value_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_value", deep_link_value_et.getText().toString());
        }
        if(deep_link_sub1_et.getText() != null && !deep_link_sub1_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub1", deep_link_sub1_et.getText().toString());
        }

        if(deep_link_sub2_et.getText() != null && !deep_link_sub2_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub2", deep_link_sub2_et.getText().toString());
        }

        if(deep_link_sub3_et.getText() != null && !deep_link_sub3_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub3", deep_link_sub3_et.getText().toString());
        }

        if(deep_link_sub4_et.getText() != null && !deep_link_sub4_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub4", deep_link_sub5_et.getText().toString());
        }

        if(deep_link_sub5_et.getText() != null && !deep_link_sub5_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub5", deep_link_sub5_et.getText().toString());
        }

        if(deep_link_sub6_et.getText() != null && !deep_link_sub6_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub6", deep_link_sub6_et.getText().toString());
        }

        if(deep_link_sub7_et.getText() != null && !deep_link_sub7_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub7", deep_link_sub7_et.getText().toString());
        }

        if(deep_link_sub8_et.getText() != null && !deep_link_sub8_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub8", deep_link_sub8_et.getText().toString());
        }

        if(deep_link_sub9_et.getText() != null && !deep_link_sub9_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub9", deep_link_sub9_et.getText().toString());
        }

        if(deep_link_sub10_et.getText() != null && !deep_link_sub10_et.getText().toString().isEmpty()) {
            linkGenerator.addParameter("deep_link_sub10", deep_link_sub10_et.getText().toString());
        }

        linkGenerator.generateLink(this, listener);
    }

}
