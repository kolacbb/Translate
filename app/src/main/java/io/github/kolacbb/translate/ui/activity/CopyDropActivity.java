package io.github.kolacbb.translate.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseActivity;
import io.github.kolacbb.translate.data.TranslateDataSource;
import io.github.kolacbb.translate.data.TranslateRepository;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.ui.fragment.SettingsFragment;
import io.github.kolacbb.translate.util.SpUtil;

public class CopyDropActivity extends BaseActivity {

    @BindView(R.id.query)
    TextView queryTextView;
    @BindView(R.id.translation)
    TextView translationTextView;
    @BindView(R.id.replace_btn)
    Button replaceButton;
    @BindView(R.id.container)
    ViewGroup mContainerView;
    @BindView(R.id.error_view)
    View errorView;

    PopupMenu popupMenu = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_copy_drop;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        reSizeWindow();
        handleIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void reSizeWindow() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (width * 0.9);
        lp.verticalMargin = 0.02F;
        lp.gravity = Gravity.TOP;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        //Intent intent = getIntent();
        CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        boolean readOnly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);
        replaceButton.setClickable(!readOnly);
        if (text != null) {
            queryTextView.setText(text.toString());
            TranslateRepository.getInstance().getTranslate(text.toString(),
                    SpUtil.findString(SettingsFragment.KEY_TRANSLATE_SOURCE),
                    new TranslateDataSource.GetTranslateCallback() {
                        @Override
                        public void onTranslateLoaded(Translate translate) {
                            render(translate);
                        }

                        @Override
                        public void onDataNotAvailable() {

                        }
                    });
        }
    }

    private void render(Translate translate) {
        translationTextView.setText(translate.getTranslation());
    }

    public void setCallBackResult(String replacementText) {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, replacementText);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick({R.id.close_btn, R.id.replace_btn, R.id.more_menu})
    public void widgetOnClicked(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                CopyDropActivity.this.finish();
                break;
            case R.id.replace_btn:
                setCallBackResult(translationTextView.getText().toString());
                break;
            case R.id.more_menu:
                if (popupMenu == null) {
                    initPopupMenu(view);
                } else {
                    popupMenu.show();
                }
                break;
        }
    }

    private void initPopupMenu(View view) {
        popupMenu = new PopupMenu(this, view);
        getMenuInflater().inflate(R.menu.copy_drop_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.copy:
                        ClipboardManager clipboard = (ClipboardManager)
                                getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", translationTextView.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(CopyDropActivity.this, "Translation copied, paste to enter", Toast.LENGTH_SHORT).show();
                        popupMenu.dismiss();
                        finish();
                        break;
                    case R.id.open_application:
                        Toast.makeText(CopyDropActivity.this, "open", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
