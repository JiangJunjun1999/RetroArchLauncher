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
    private boolean launching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 不再显示 "Starting RetroArch..."
        // 直接启动 RetroArch，减少 Launcher 闪现
        launchRetroArch();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * 当 RetroArch 退出并返回 Launcher 时，
         * 自动重新启动 RetroArch。
         *
         * 延迟 250ms 是为了避免 Android 在 Activity
         * 切换过程中重复触发启动。
         */
        if (!launching) {
            handler.postDelayed(this::launchRetroArch, 250);
        }
    }

    private void launchRetroArch() {

        if (launching) {
            return;
        }

        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(RETROARCH_PACKAGE);

        /*
         * 如果没有安装 RetroArch，
         * 显示提示信息。
         */
        if (intent == null) {

            launching = false;

            showStatus(
                    "RetroArch was not found.\n\n" +
                    "Please install RetroArch first.\n\n" +
                    "Required package:\n" +
                    RETROARCH_PACKAGE
            );

            return;
        }

        launching = true;

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );

        try {

            startActivity(intent);

            /*
             * 给 Android 一点时间完成 Activity 切换。
             */
            handler.postDelayed(() -> {
                launching = false;
            }, 1000);

        } catch (Exception e) {

            launching = false;

            showStatus(
                    "Unable to start RetroArch."
            );
        }
    }

    /*
     * 只有 RetroArch 没安装或者启动失败时，
     * 才显示这个界面。
     */
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
         * 如果 Launcher 收到返回键，
         * 重新启动 RetroArch。
         */
        launchRetroArch();
    }

    @Override
    protected void onDestroy() {

        /*
         * Activity 销毁时清理所有延迟任务。
         */
        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
