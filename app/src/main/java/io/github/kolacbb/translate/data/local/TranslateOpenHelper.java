package io.github.kolacbb.translate.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kola on 2016/6/6.
 */
public class TranslateOpenHelper extends SQLiteOpenHelper {

    final String TABLE_SQL = "create table %s (" +
            "query text, " +
            "source text, " +
            "us_phonetic text, " +
            "uk_phonetic text, " +
            "translation text, " +
            "explains text, " +
            "web text, " +
            "primary key (query, source))";

    public static final String TABLE_NAME_DICT = "dict";
    public static final String TABLE_NAME_HISTORY = "history";
    public static final String TABLE_NAME_PHRASEBOOK = "phrasebook";

    public TranslateOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(TABLE_SQL, TABLE_NAME_DICT));
        db.execSQL(String.format(TABLE_SQL, TABLE_NAME_HISTORY));
        db.execSQL(String.format(TABLE_SQL, TABLE_NAME_PHRASEBOOK));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
