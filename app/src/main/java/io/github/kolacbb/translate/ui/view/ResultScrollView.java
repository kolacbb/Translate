package io.github.kolacbb.translate.ui.view;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import io.github.kolacbb.translate.R;

/**
 * Created by Kola on 2016/7/7.
 */
public class ResultScrollView extends LinearLayout {

    LayoutInflater mInflater;

    ViewGroup.LayoutParams mParams;

    View mWidgetResultView;
    LinearLayout mResultContainer;
    FrameLayout mFloatInputHolder;
    LinearLayout mFirstTwoContainer;
    FrameLayout mProgress_container;

    View mCardResult;
    LinearLayout mSpeakerViewWrapper;
    ImageButton mSpeakView;
    TextView mLangText;
    ImageButton mStarBtn;
    TextView mTranslation;
    TextView mTransliteration;
    ImageView mCopyBtn;
    ImageView mMenuBtn;


    CardResultClickListener mListener;
    Context mContext;
    TextToSpeech mTts;

    public ResultScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mWidgetResultView = mInflater.inflate(R.layout.widget_result_view, null);
        // 添加widget_result_view 到ResultScrollView
        mParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(mWidgetResultView, mParams);
        mFirstTwoContainer = (LinearLayout) mWidgetResultView.findViewById(R.id.firstTwoContainer);
        mProgress_container = (FrameLayout) mWidgetResultView.findViewById(R.id.progress_container);
    }

    public void setTranslation(String translation, String phonetic, final Locale locale) {
        if (mCardResult == null) {
            mCardResult = mInflater.inflate(R.layout.card_result, null);
            mSpeakerViewWrapper = (LinearLayout) mCardResult.findViewById(R.id.speaker_view_wrapper);
            mSpeakView = (ImageButton) mCardResult.findViewById(R.id.speak_view);
            mLangText = (TextView) mCardResult.findViewById(R.id.text_lang);
            mStarBtn = (ImageButton) mCardResult.findViewById(R.id.btn_star);
            mTranslation = (TextView) mCardResult.findViewById(android.R.id.text1);
            mTransliteration = (TextView) mCardResult.findViewById(R.id.txt_transliteration);
            mCopyBtn = (ImageView) mCardResult.findViewById(R.id.copy_text);
            mMenuBtn = (ImageView) mCardResult.findViewById(R.id.menu_overflow);

            mTts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = mTts.setLanguage(locale);
                        if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                                && result != TextToSpeech.LANG_AVAILABLE) {
                            // 不支持该语言
                            mSpeakView.setImageResource(R.drawable.ic_volume_off_black_24px);
                        } else {
                            mSpeakerViewWrapper.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mTts.speak(mTranslation.getText().toString(), TextToSpeech.QUEUE_ADD, null, "speech");
                                }
                            });
                        }
                    }
                }
            });
            mFirstTwoContainer.addView(mCardResult, mParams);
        }

        mProgress_container.setVisibility(View.GONE);
        mTranslation.setText(translation);
        mTransliteration.setText(phonetic);
        mLangText.setText(locale.getDisplayName());

        mStarBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.starClick();
            }
        });
    }

    public void setOnCardResultClickListener(CardResultClickListener mListener) {
        this.mListener = mListener;
    }

    public interface CardResultClickListener {
        void starClick();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTts.shutdown();
    }
}
