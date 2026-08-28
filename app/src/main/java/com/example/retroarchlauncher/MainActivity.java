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

    // 开机后等待 5 秒
    private static final long BOOT_DELAY = 5000;

    // 普通 Home 启动延迟
    private static final long NORMAL_DELAY = 100;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean startingRetroArch = false;

    // 用来判断是否是本次 Launcher 第一次启动
    private boolean firstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Launcher 第一次启动时，
         * 等待 5 秒再启动 RetroArch。
         *
         * 这样可以给 Android 足够时间完成：
         *
         * - 系统启动
         * - 文件系统准备
         * - 应用数据初始化
         * - 存储访问准备
         */
        handler.postDelayed(
                this::startRetroArch,
                BOOT_DELAY
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * onCreate() 第一次启动时已经安排了
         * 5 秒后的启动任务。
         *
         * 因此第一次 onResume() 不再重复启动。
         */
        if (firstLaunch) {
            firstLaunch = false;
            return;
        }

        /*
         * 后续从 RetroArch 返回 Launcher 时，
         * 快速重新启动 RetroArch。
         */
        if (!startingRetroArch) {
            handler.postDelayed(
                    this::startRetroArch,
                    NORMAL_DELAY
            );
        }
    }

    private void startRetroArch() {

        if (startingRetroArch) {
            return;
        }

        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(
                        RETROARCH_PACKAGE
                );

        /*
         * RetroArch 没有安装。
         */
        if (intent == null) {

            showError(
                    "RetroArch was not found.\n\n" +
                    "Please install RetroArch first.\n\n" +
                    "Package:\n" +
                    RETROARCH_PACKAGE
            );

            return;
        }

        startingRetroArch = true;

        /*
         * 使用 RetroArch 自己的启动 Intent。
         */
        try {

            startActivity(intent);

            /*
             * 禁止 Launcher → RetroArch 的默认切换动画。
             */
            overridePendingTransition(0, 0);

            /*
             * 等待 Activity 切换完成后，
             * 允许下一次启动。
             */
            handler.postDelayed(() -> {
                startingRetroArch = false;
            }, 1000);

        } catch (Exception e) {

            startingRetroArch = false;

            showError(
                    "Unable to start RetroArch."
            );
        }
    }

    private void showError(String message) {

        TextView view = new TextView(this);

        view.setText(
                message +
                "\n\nPackage:\n" +
                RETROARCH_PACKAGE
        );

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
        startRetroArch();
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
