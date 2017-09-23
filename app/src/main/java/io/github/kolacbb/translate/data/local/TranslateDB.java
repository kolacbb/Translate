package io.github.kolacbb.translate.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.github.kolacbb.translate.data.entity.Translate;

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

    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public synchronized static TranslateDB getInstance() {
        if (translateDB == null) {
            translateDB = new TranslateDB(mContext);
        }
        return translateDB;
    }

    public synchronized static TranslateDB getInstance(Context context) {
        if (translateDB == null) {
            translateDB = new TranslateDB(context);
        }
        return translateDB;
    }

    public void addTranslate(String tableName, Translate translate) {
        String sql = "insert into " + tableName + " (query, source, us_phonetic, uk_phonetic, translation, explains, web) values (?, ?, ?, ?, ?, ?, ?)";
        db.execSQL(sql, new String[]{
                translate.getQuery(),
                translate.getSource(),
                translate.getUs_phonetic(),
                translate.getUk_phonetic(),
                translate.getTranslation(),
                translate.getExplains(),
                translate.getWeb()
        });
    }

    public Translate getTranslate(String query, String source) {
            String sql = "select query, source, us_phonetic, uk_phonetic, translation, explains, web, (select 1 from phrasebook where t1.query = phrasebook.query) as isfavor from dict t1 " +
                    "where query = ? and source = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{query, source});
        Translate translate = null;
        if (cursor.moveToFirst()) {
            translate = new Translate();
            translate.setQuery(cursor.getString(cursor.getColumnIndex("query")));
            translate.setSource(cursor.getString(cursor.getColumnIndex("source")));
            translate.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
            translate.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
            translate.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
            translate.setExplains(cursor.getString(cursor.getColumnIndex("explains")));
            translate.setWeb(cursor.getString(cursor.getColumnIndex("web")));
            translate.setFavor(cursor.getString(cursor.getColumnIndex("isfavor")) != null);
        }
        cursor.close();
        return translate;
    }

    /**
     * 获取历史中全部单词记录
     */
    public List<Translate> getTranslateFromHistory() {
        String sql = "select query, source, us_phonetic, uk_phonetic, translation, explains, web, (select 1 from phrasebook where t1.query = phrasebook.query) as isfavor from history t1 order by time desc";
        List<Translate> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[0]);
        if (cursor.moveToFirst()) {
            do {
                Translate translate = new Translate();
                translate.setQuery(cursor.getString(cursor.getColumnIndex("query")));
                translate.setSource(cursor.getString(cursor.getColumnIndex("source")));
                translate.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
                translate.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
                translate.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
                translate.setExplains(cursor.getString(cursor.getColumnIndex("explains")));
                translate.setWeb(cursor.getString(cursor.getColumnIndex("web")));
                translate.setFavor(cursor.getString(cursor.getColumnIndex("isfavor")) != null);
                list.add(translate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 获取单词本中全部单词记录
     */
    public List<Translate> getTranslateFromPhrasebook() {
        List<Translate> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TranslateOpenHelper.TABLE_NAME_PHRASEBOOK + " order by time desc",
                new String[0]);
        if (cursor.moveToFirst()) {
            do {
                Translate translate = new Translate();
                translate.setQuery(cursor.getString(cursor.getColumnIndex("query")));
                translate.setSource(cursor.getString(cursor.getColumnIndex("source")));
                translate.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
                translate.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
                translate.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
                translate.setExplains(cursor.getString(cursor.getColumnIndex("explains")));
                translate.setWeb(cursor.getString(cursor.getColumnIndex("web")));
                translate.setFavor(true);
                list.add(translate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public void removeTranslate(String tableName, Translate translate) {
        db.execSQL("delete from " + tableName + " where query = ? and source = ?", new String[]{translate.getQuery(), translate.getSource()});
    }

    public void removeTranslate(String tableName, List<Translate> list) {
        String sql = "delete from " + tableName + " where query in (?, ?, ?, ?, ?)";
        // N 是在sql语句中可变的部分的数量，我们可以找到一个效率的平衡点，来让sql更快的执行
        int N = 5;
        for (int i = 0; i <= list.size() / N; i++) {
            String[] args = new String[N];
            // 构造String
            for (int j = 0; j < N && i * N + j < list.size(); j++) {
                args[j] = list.get(i * N + j).getQuery();
            }
            db.execSQL(sql, args);
        }
    }
//
//    public Translate getTranslate(String tableName, String query, String source) {
//        Translate translate = null;
//        Cursor cursor = db.rawQuery("select * from " + tableName + " where query = ? and source = ?",
//                new String[]{query, source});
//        if (cursor.moveToFirst()) {
//            translate = new Translate();
//            translate.setQuery(query);
//            translate.setSource(source);
//            translate.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
//            translate.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
//            translate.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
//            translate.setExplains(cursor.getString(cursor.getColumnIndex("explains")));
//            translate.setWeb(cursor.getString(cursor.getColumnIndex("web")));
//        }
//        cursor.close();
//        return translate;
//    }

    public List<Translate> getTranslate(String tableName) {
        List<Translate> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + tableName + "order by time desc", new String[0]);
        if (cursor.moveToFirst()) {
            do {
                Translate translate = new Translate();
                translate.setQuery(cursor.getString(cursor.getColumnIndex("query")));
                translate.setSource(cursor.getString(cursor.getColumnIndex("source")));
                translate.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
                translate.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
                translate.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
                translate.setExplains(cursor.getString(cursor.getColumnIndex("explains")));
                translate.setWeb(cursor.getString(cursor.getColumnIndex("web")));
                list.add(translate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void clearTranslate(String tableName) {
        String sql = "delete from " + tableName;
        db.execSQL(sql, new String[0]);
    }

}
