package com.appsflyer.sdk.support.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.appsflyer.deeplink.DeepLinkResult;

import java.util.Map;
import java.util.Objects;

public class DemoUtils {

    private static DemoUtils mInstance = null;

    private DemoUtils() {}

    public static DemoUtils getInstance() {
        if(mInstance == null) {
            mInstance = new DemoUtils();
        }

        return mInstance;
    }

    public void logInfo(String message) {
        Log.d("AppsFlyer - DemoUtils: ", message);
    }

    public void showClearDataPopup(Context ctx, OnOptionChangedListener onOptionChangedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage("Do you want to clear all previous data you set?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {

                    //Remove AppsFlyer's SDK shared preferences.
                    ctx.getApplicationContext()
                            .getSharedPreferences("appsflyer-data", Context.MODE_PRIVATE).edit().clear().commit();
                    ctx.getApplicationContext().getSharedPreferences("demo-app", Context.MODE_PRIVATE).edit().clear().commit();

                    saveToSP(ctx, "is_deferred", true);
                    saveToSP(ctx, "is_udl", true);

                    onOptionChangedListener.onReset(true);
                    //Remove all data from Prefs and reset checkbox values
                })
                .setNegativeButton("No", (dialog, id) -> {
                    onOptionChangedListener.onReset(false);
                    dialog.cancel();
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Clear Data");
        alert.show();
    }

    public void showToastMessage(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }

    public void handleGcdData(Context ctx, Map<String, Object> map)   {
    }

    public void handleGcdFailure(Context ctx, String s) {

    }

    public void handleUDLResponse(Context ctx, DeepLinkResult deepLinkResult) {

    }

    public void handleOAOA(Context ctx, Map<String, String> map) {

    }

    public void handleOAOAFailure(Context ctx, String message) {

    }

    public void saveToSP(Context ctx, String key, String value) {
        ctx.getApplicationContext().getSharedPreferences("demo-app", Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public void saveToSP(Context ctx, String key, long value) {
        ctx.getApplicationContext().getSharedPreferences("demo-app", Context.MODE_PRIVATE).edit().putLong(key, value).apply();
    }

    public void saveToSP(Context ctx, String key, boolean value) {
        ctx.getApplicationContext().getSharedPreferences("demo-app", Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public long getLongFromSP(Context ctx, String key) {
        return ctx.getApplicationContext().getSharedPreferences("demo-app", Context.MODE_PRIVATE).getLong(key, 0);
    }

    public boolean getBooleanFromSP(Context ctx, String key) {
        if(Objects.equals(key, "is_deferred") || Objects.equals(key, "is_udl")) {
            return ctx.getApplicationContext().getSharedPreferences("demo-app", Context.MODE_PRIVATE).getBoolean(key, true);
        } else {
            return ctx.getApplicationContext().getSharedPreferences("demo-app", Context.MODE_PRIVATE).getBoolean(key, false);
        }
    }

    public String getStringFromSP(Context ctx, String key) {
        return ctx.getApplicationContext().getSharedPreferences("demo-app", Context.MODE_PRIVATE).getString(key, "");
    }
}
