# RetroArch Launcher — GitHub Actions

这是一个适用于 Android 11 的极简 RetroArch Launcher。

目标 RetroArch 包名：

`com.retroarch`

## 在线编译

1. 在 GitHub 新建一个 Repository。
2. 把本项目全部文件上传到 Repository。
3. 打开 Repository 的 **Actions**。
4. 选择 **Build RetroArch Launcher**。
5. 点击 **Run workflow**。
6. 等待构建完成。
7. 打开完成的 workflow。
8. 在 **Artifacts** 下载 `RetroArch-Launcher-APK`。
9. 解压后得到 `app-debug.apk`。
10. 把 APK 安装到掌机。

## 设置默认 Launcher

安装后按 Home。

如果系统询问 Home 应用：

选择 **RetroArch Launcher** → **始终**。

如果没有弹窗：

设置 → 应用 → 默认应用 → Home 应用

然后选择 RetroArch Launcher。

## 注意

本项目不会修改 RetroArch。

如果你的 RetroArch 确实是：

`com.retroarch`

Launcher 会自动调用它。

如果需要恢复原来的 Android 桌面：

设置 → 应用 → 默认应用 → Home 应用 → 选择原来的 Launcher。
