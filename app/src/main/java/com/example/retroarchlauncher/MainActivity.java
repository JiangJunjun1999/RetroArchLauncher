package com.example.retroarchlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String RETROARCH_PACKAGE = "com.retroarch";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean launching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStatus("Starting RetroArch...");
        launchRetroArch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!launching) {
            handler.postDelayed(this::launchRetroArch, 250);
        }
    }

    private void launchRetroArch() {
        if (launching) return;

        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(RETROARCH_PACKAGE);

        if (intent == null) {
            launching = false;
            showStatus("RetroArch was not found.\n\nRequired package:\n"
                    + RETROARCH_PACKAGE);
            return;
        }

        launching = true;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        try {
            startActivity(intent);
            handler.postDelayed(() -> launching = false, 1000);
        } catch (Exception e) {
            launching = false;
            showStatus("Unable to start RetroArch.");
        }
    }

    private void showStatus(String text) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(18);
        view.setGravity(Gravity.CENTER);
        view.setPadding(40, 40, 40, 40);
        view.setBackgroundColor(0xFF000000);
        view.setTextColor(0xFFFFFFFF);
        setContentView(view);
    }

    @Override
    public void onBackPressed() {
        launchRetroArch();
    }
}
