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
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.flux.actions.creator.ActionCreatorManager;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.flux.stores.ClipboardListenerStore;
import io.github.kolacbb.translate.inject.component.ApplicationComponent;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.ui.activity.HomeActivity;
import io.github.kolacbb.translate.ui.fragment.SettingsFragment;
import io.github.kolacbb.translate.util.SpUtil;
import io.github.kolacbb.translate.util.StringUtils;

public class ClipboardListenerService extends Service
        implements ClipboardManager.OnPrimaryClipChangedListener{

    @Inject
    Dispatcher dispatcher;
    @Inject
    ActionCreatorManager actionCreatorManager;
    ClipboardListenerStore store;

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
    public void onCreate() {
        super.onCreate();
        store = new ClipboardListenerStore();
        dispatcher.register(store);
        store.register(ClipboardListenerService.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clipboardManager.removePrimaryClipChangedListener(this);
        if (SpUtil.findBoolean(SettingsFragment.KEY_TAP_TO_TRANSLATE)) {
            SpUtil.save(SettingsFragment.KEY_TAP_TO_TRANSLATE, false);
        }

        dispatcher.unregister(store);
        store.unregister(ClipboardListenerService.this);
    }

    public void render() {
        Result result = store.getData();
        //showChooseDialog(result.getQuery(), result.getTranslation());
        showHeadsUpNotification(result.getQuery(), result.getTranslation());
    }

    @Subscribe
    public void onStoreChanged(ClipboardListenerStore.ClipboardListenerChangeEvent event) {
        render();
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
        String copyData = item.getText().toString();
        showTranslation(copyData);
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

        //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
        actionCreatorManager.getTranslateActionCreator().fetchTranslation(data);
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
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_star_black_24px)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle(title)
                .setContentText(message);
        Intent push = new Intent();
        push.setClass(getApplicationContext(), HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setFullScreenIntent(pendingIntent, true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        notificationManager.cancel(0);

    }
}
