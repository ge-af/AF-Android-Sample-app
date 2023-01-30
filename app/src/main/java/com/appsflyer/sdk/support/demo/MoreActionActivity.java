package com.appsflyer.sdk.support.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class MoreActionActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_more_actions);

        findViewById(R.id.iae_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MoreActionActivity.this, IAEActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.user_invite_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MoreActionActivity.this, UserInviteActivity.class);
            startActivity(intent);
        });
    }
}
