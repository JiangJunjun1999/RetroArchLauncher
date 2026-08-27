package com.example.retroarchlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String RETROARCH_PACKAGE = "com.retroarch";

    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean startingRetroArch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showStatus("Starting RetroArch...");

        handler.postDelayed(this::startRetroArch, 300);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * 当 RetroArch 退出并返回 Launcher 时，
         * 再次启动 RetroArch。
         */
        if (!startingRetroArch) {
            handler.postDelayed(this::startRetroArch, 300);
        }
    }

    private void startRetroArch() {

        if (startingRetroArch) {
            return;
        }

        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(RETROARCH_PACKAGE);

        if (intent == null) {

            showStatus(
                    "RetroArch was not found.\n\n" +
                    "Please install RetroArch first.\n\n" +
                    "Package:\n" +
                    RETROARCH_PACKAGE
            );

            return;
        }

        startingRetroArch = true;

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );

        try {

            startActivity(intent);

            /*
             * 给 Android 一点时间切换到 RetroArch。
             */
            handler.postDelayed(() -> {
                startingRetroArch = false;
            }, 1000);

        } catch (Exception e) {

            startingRetroArch = false;

            showStatus(
                    "Unable to start RetroArch.\n\n" +
                    e.getMessage()
            );
        }
    }

    private void showStatus(String text) {

        TextView view = new TextView(this);

        view.setText(text);
        view.setTextSize(18);
        view.setGravity(Gravity.CENTER);

        view.setPadding(
                40,
                40,
                40,
                40
        );

        view.setBackgroundColor(0xFF000000);
        view.setTextColor(0xFFFFFFFF);

        setContentView(view);
    }

    @Override
    public void onBackPressed() {

        /*
         * 按返回键时重新启动 RetroArch。
         */
        startRetroArch();
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
