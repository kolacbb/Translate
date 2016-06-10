package io.github.kolacbb.translate.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.db.TranslateDB;
import io.github.kolacbb.translate.db.TranslateOpenHelper;
import io.github.kolacbb.translate.model.entity.Result;

public class TestActivity extends AppCompatActivity {
    Result result = new Result();
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        result.setQuery("query1");
        result.setTranslation("translation");
        result.setBasic("basic");
        result.setUk_phonetic("uk");
        result.setUs_phonetic("us");

    }

    public void widgetOnClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                System.out.println(result.getId());
                result.setQuery(result.getQuery() + i);
                i++;
                break;
            case R.id.bt2:
                //TranslateDB.getInstance(this).updateHistoryToDict(result);
                break;
            case R.id.bt3:
                //TranslateDB.getInstance(this).deleteWord(result);
                break;
            case R.id.bt4:
//                List<Result> list =TranslateDB.getInstance(this).getAllDictWord();
//                for (Result r : list) {
//                    System.out.println(r.getId() + " " + r.getQuery());
//                }
                break;
            case R.id.bt5:
//                List<Result> list2 =TranslateDB.getInstance(this).getAllHistoryWord();
//                for (Result r : list2) {
//                    System.out.println(r.getId() + " " + r.getQuery());
//                }
                break;

        }
    }
}
