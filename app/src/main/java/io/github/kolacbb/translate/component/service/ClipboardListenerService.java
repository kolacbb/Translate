package io.github.kolacbb.translate.component.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.data.TranslateDataSource;
import io.github.kolacbb.translate.data.TranslateRepository;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.inject.component.ApplicationComponent;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.ui.activity.HomeActivity;
import io.github.kolacbb.translate.ui.fragment.SettingsFragment;
import io.github.kolacbb.translate.util.SpUtil;
import io.github.kolacbb.translate.util.StringUtils;

public class ClipboardListenerService extends Service
        implements ClipboardManager.OnPrimaryClipChangedListener{

    private ClipboardManager clipboardManager = null;
    // 存储上一次复制时间
    private long previousTime = 0;

    public ClipboardListenerService() {
        ApplicationComponent.Instance.get().inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (clipboardManager == null) {
            clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        clipboardManager.addPrimaryClipChangedListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clipboardManager.removePrimaryClipChangedListener(this);
        if (SpUtil.findBoolean(SettingsFragment.KEY_TAP_TO_TRANSLATE)) {
            SpUtil.save(SettingsFragment.KEY_TAP_TO_TRANSLATE, false);
        }
    }

    @Override
    public void onPrimaryClipChanged() {
        // 解决一次复制，两次方法调用的方案
        long nowTime = System.currentTimeMillis();
        if (nowTime - previousTime < 200) {
            previousTime = nowTime;
            return;
        }
        previousTime = nowTime;
        // 获取剪贴板内容
        ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
        if (item != null && item.getText() != null) {
            String copyData = item.getText().toString();
            showTranslation(copyData);
        }
    }

    public void showTranslation(String data) {
        if (StringUtils.isOnlyNumber(data)) {
            return;
        }

        String email = StringUtils.isValidEmail(data);
        if (!email.equals(StringUtils.INVALID)) {
            //Toast.makeText(getApplicationContext(), "email:" + email, Toast.LENGTH_SHORT).show();
            showChooseDialog("email", email);
            return;
        }

        String url = StringUtils.isValidURL(data);
        if (!url.equals(StringUtils.INVALID)) {
//            Toast.makeText(getApplicationContext(), "url:" + url, Toast.LENGTH_SHORT).show();
            showChooseDialog("url", url);
            return;
        }

        String tel = StringUtils.isValidTEL(data);
        if (!tel.equals(StringUtils.INVALID)) {
//            Toast.makeText(getApplicationContext(), "tel:" + tel, Toast.LENGTH_SHORT).show();
            showChooseDialog("tel", tel);
            return;
        }

        TranslateRepository.getInstance().getTranslate(data, SpUtil.findString(SettingsFragment.KEY_TRANSLATE_SOURCE), new TranslateDataSource.GetTranslateCallback() {
            @Override
            public void onTranslateLoaded(Translate translate) {
                showHeadsUpNotification(translate.getQuery(), translate.getTranslation());
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public void showChooseDialog(String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(getApplicationContext())
                .setTitle(title)
                .setMessage(message)
                .create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    public void showHeadsUpNotification(String title, String message) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_star_black_24px)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_star_black_24px))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[]{1, 0});

        Intent push = new Intent();
        push.putExtra("query", title);
        push.setClass(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(0);
            }
        }, 5000);
    }
}
