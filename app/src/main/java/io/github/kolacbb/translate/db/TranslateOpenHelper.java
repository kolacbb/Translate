package io.github.kolacbb.translate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kola on 2016/6/6.
 */
public class TranslateOpenHelper extends SQLiteOpenHelper {

    final String CREATE_DICT_TABLE_SQL = "create table dict ( id integer primary key autoincrement," +
            "query text, " +
            "us_phonetic text, " +
            "uk_phonetic text, " +
            "translation text, " +
            "basic text, " +
            "isFavor boolean)";
    final String CREATE_PHRASEBOOK_TABLE_SQL = "create table phrasebook ( " +
            "id integer primary key autoincrement, " +
            "query text, " +
            "us_phonetic text, " +
            "uk_phonetic text, " +
            "translation text, " +
            "basic text) ";
    final String CREATE_HISTORY_TABLE_SQL = "create table history ( " +
            "id integer primary key autoincrement, " +
            "query text, " +
            "us_phonetic text, " +
            "uk_phonetic text, " +
            "translation text, " +
            "basic text) ";

    public TranslateOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // old version
        db.execSQL(CREATE_DICT_TABLE_SQL);
        // create phrasebook table
        db.execSQL(CREATE_PHRASEBOOK_TABLE_SQL);
        // create history table
        db.execSQL(CREATE_HISTORY_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
