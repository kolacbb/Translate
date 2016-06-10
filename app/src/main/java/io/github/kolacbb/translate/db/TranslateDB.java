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
    public static final String DB_HISTORY_NAME = "history";
    public static final String DB_PHRASEBOOK_NAME = "phrasebook";
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

    /**
     * 操作History表
     * 对于History表只用到增加，删除，和全部删除的操作，故不添加其他的业务方法，如修改
     */
    public void saveToHistory(Result result) {
        String sql = "insert into history (query, us_phonetic, uk_phonetic, translation, basic) values (?, ?, ?, ?, ?)";
        db.execSQL(sql, new String[]{result.getQuery(),
                result.getUs_phonetic(),
                result.getUk_phonetic(),
                result.getTranslation(),
                result.getBasic()
        });
    }

    public void deleteFromHistory(Result result) {
        String sql = "delete from history where query = ?";
        db.execSQL(sql, new String[] {result.getQuery()});
    }

    public void clearHistory() {
        String sql = "delete from history";
        db.execSQL(sql, new String[0]);
    }

    public List<Result> getAllHistory() {
        List<Result> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from history order by id desc", new String[0]);
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

    /**
     * 操作Phrasebook表
     *
     * */
    public void saveToPhrasebook(Result result) {
        String sql = "insert into phrasebook (query, us_phonetic, uk_phonetic, translation, basic) values (?, ?, ?, ?, ?)";
        db.execSQL(sql, new String[]{result.getQuery(),
                result.getUs_phonetic(),
                result.getUk_phonetic(),
                result.getTranslation(),
                result.getBasic()
        });
    }


    public void deleteFromPhrasebook(Result result) {
        String sql = "delete from phrasebook where query = ?";
        db.execSQL(sql, new String[] {result.getQuery()});
    }

    /**
     * 删除多条Phrasebook表中的列
     * 1.尽可能的减少连接数据库的次数
     * 2.保证sql语句的唯一性（sqlite会对相类似的语句进行检存储，会在下一次调用相同语句时更加迅速）
     * */
    public void deleteFromRhrasebook(List<Result> list) {
        String sql = "delete from history where query in (?, ?, ?, ?, ?)";
        
    }

    public List<Result> getAllPhrasebook() {
        String sql = "select * from phrasebook order by id desc";
        List<Result> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[0]);
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

    /**
     * 面向应用的方法，比如在查询单词时，先查询本地的，在查询来自于网络的
     * 如果有本地数据库：phrasebook表 -> localDatabase表
     * 没有本地数据库：phrasebook表 -> history表
     * */
    public Result getTransalte(String query) {
        return null;
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

    public void deleteWordFromDict(Result result) {
        db.execSQL("update dict set isFavor = 'false' where query = ?", new String[]{result.getQuery()});
    }

    public List<Result> getAllDictWord() {
        return getWords("true");
    }

    public List<Result> getAllHistoryWord() {
        return getWords("false");
    }

    public Result queryWords(String word) {
        Result result = null;
        Cursor cursor = db.rawQuery("select * from dict where query = ?", new String[]{word});
        if (cursor.moveToFirst()) {
            result = new Result();
            result.setId(cursor.getInt(cursor.getColumnIndex("id")));
            result.setQuery(cursor.getString(cursor.getColumnIndex("query")));
            result.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
            result.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
            result.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
            result.setBasic(cursor.getString(cursor.getColumnIndex("basic")));
        }
        cursor.close();
        return result;
    }

    private List<Result> getWords(String type) {
        List<Result> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from dict where isFavor == ? order by id desc", new String[]{type});
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
