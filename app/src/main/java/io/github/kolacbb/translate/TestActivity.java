package io.github.kolacbb.translate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import io.github.kolacbb.translate.ui.view.ResultScrollView;

public class TestActivity extends AppCompatActivity {

    ResultScrollView resultScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        resultScrollView = (ResultScrollView) findViewById(R.id.resultContainer);
    }

    public void widgetOnClicked(View view) {
        resultScrollView.setTranslation("hello", "phonetic", Locale.US);
    }
}
