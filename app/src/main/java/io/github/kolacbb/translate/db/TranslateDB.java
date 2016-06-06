package io.github.kolacbb.translate.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by Kola on 2016/6/6.
 */
public class TranslateDB {
    public static final String DB_NAME = "translate";
    public static final int VERSION = 1;
    private static TranslateDB translateDB;
    private SQLiteDatabase db;

    private TranslateDB(Context context) {
        TranslateOpenHelper dbHelper = new TranslateOpenHelper(context, DB_NAME, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static TranslateDB getInstance(Context context) {
        if (translateDB == null) {
            translateDB = new TranslateDB(context);
        }
        return translateDB;
    }

    public void saveWord(Result result) {
        if (result.getId() == 0) {
            db.execSQL("insert into dict (query, us_phonetic, uk_phonetic, translation, basic, isFavor) " +
                            "values (?, ?, ?, ?, ?, ?) ",
                    new String[]{result.getQuery(),
                            result.getUs_phonetic(),
                            result.getUk_phonetic(),
                            result.getTranslation(),
                            result.getBasic(),
                            "false"
                    });
        }
    }

    public void deleteWord(Result result) {
        db.execSQL("delete from dict where query = ?", new String[]{result.getQuery()});
    }

    public void updateHistoryToDict(Result result) {
        db.execSQL("update dict set isFavor = 'true' where query = ?", new String[]{result.getQuery()});
    }

    public List<Result> getAllDictWord() {
        return getWords("true");
    }

    public List<Result> getAllHistoryWord() {
        return getWords("false");
    }

    private List<Result> getWords(String type) {
        List<Result> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from dict where isFavor == ?", new String[]{type});
        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(cursor.getInt(cursor.getColumnIndex("id")));
                result.setQuery(cursor.getString(cursor.getColumnIndex("query")));
                result.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
                result.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
                result.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
                result.setBasic(cursor.getString(cursor.getColumnIndex("basic")));

                list.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
