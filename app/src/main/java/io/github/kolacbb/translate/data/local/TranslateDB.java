package io.github.kolacbb.translate.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.github.kolacbb.translate.data.entity.Translate;
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
        String sql = "insert into " + tableName + " (query, source, us_phonetic, uk_phonetic, translation, explains, web) values (?, ?, ?, ?, ?)";
        db.execSQL(sql, new String[]{
                translate.getQuery(),
                translate.getQuery(),
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
        String sql = "select query, source, us_phonetic, uk_phonetic, translation, explains, web, (select 1 from phrasebook where t1.query = phrasebook.query) as isfavor from history t1";
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
        Cursor cursor = db.rawQuery("select * from " + TranslateOpenHelper.TABLE_NAME_PHRASEBOOK, new String[0]);
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
        db.execSQL("delete from " + tableName + " where query = ? & source = ?", new String[]{translate.getQuery(), translate.getSource()});
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
        Cursor cursor = db.rawQuery("select * from " + tableName, new String[0]);
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

    // ----------------------------------------- 下面方法全部删除---------------------------

    /**
     * 操作History表
     * 对于History表只用到增加，删除，和全部删除的操作，故不添加其他的业务方法，如修改
     * 保存：使用事物来保证在History表中query字段的唯一性。并且可以在最新保存了query字段时，query在列表顶端
     */
    public void saveToHistory(Result result) {
        db.beginTransaction();
        try {
            deleteFromHistory(result);
            saveToHistoryDirect(result);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void saveToHistoryDirect(Result result) {
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
        db.execSQL(sql, new String[]{result.getQuery()});
    }

    public void clearHistory() {
        String sql = "delete from history";
        db.execSQL(sql, new String[0]);
    }

    public List<Result> getAllHistory() {
        String sql = "select id, query, us_phonetic, uk_phonetic, translation, basic, (select 1 from phrasebook where t1.query = phrasebook.query) as isfavor from history t1 order by id desc";
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
                result.setFavor(cursor.getString(cursor.getColumnIndex("isfavor")) != null);
                list.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private List<Result> getAllHistoryIgnorePhrasebook() {
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
     */
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
        db.execSQL(sql, new String[]{result.getQuery()});
    }

    /**
     * 删除多条Phrasebook表中的列
     * 1.尽可能的减少连接数据库的次数
     * 2.保证sql语句的唯一性（sqlite会对相类似的语句进行检存储，会在下一次调用相同语句时更加迅速）
     */
    public void deleteFromPhrasebook(List<Result> list) {
        String sql = "delete from phrasebook where query in (?, ?, ?, ?, ?)";
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

    public List<Result> getAllPhrasebookSoryByAlpha() {
        String sql = "select * from phrasebook order by query";
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
     * localDatabase表存储的应该只是单词，并没有句子，所以可以先做一个判断。String中是否有空格存在
     * trim后的String若是存在空格，那它就不属于一个单词
     * <p>
     * 如果有本地数据库：phrasebook表 -> localDatabase表与history表联合（新的method）后查询
     * 没有本地数据库：phrasebook表 -> history表
     */
    @Nullable
    public Result getTransalte(String query) {
        Result result = getTranslateFromPhrasebook(query);
        if (result != null) {
            return result;
        }
        return getTranslateFromHistory(query);
    }

    private Result getTranslateFromPhrasebook(String query) {
        Result result = null;
        Cursor cursor = db.rawQuery("select * from phrasebook where query = ?", new String[]{query});
        if (cursor.moveToFirst()) {
            result = new Result();
            result.setId(cursor.getInt(cursor.getColumnIndex("id")));
            result.setQuery(cursor.getString(cursor.getColumnIndex("query")));
            result.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
            result.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
            result.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
            result.setBasic(cursor.getString(cursor.getColumnIndex("basic")));
            //从Phrasebook表获取的数据，将Favor值设置为true
            result.setFavor(true);
        }
        cursor.close();
        return result;
    }

    private Result getTranslateFromHistory(String query) {
        Result result = null;
        Cursor cursor = db.rawQuery("select * from history where query = ?", new String[]{query});
        if (cursor.moveToFirst()) {
            result = new Result();
            result.setId(cursor.getInt(cursor.getColumnIndex("id")));
            result.setQuery(cursor.getString(cursor.getColumnIndex("query")));
            result.setUk_phonetic(cursor.getString(cursor.getColumnIndex("us_phonetic")));
            result.setUs_phonetic(cursor.getString(cursor.getColumnIndex("uk_phonetic")));
            result.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
            result.setBasic(cursor.getString(cursor.getColumnIndex("basic")));
            //从Phrasebook表获取的数据，将Favor值设置为false， 嗯。。其实默认也是false
            result.setFavor(false);
        }
        cursor.close();
        return result;
    }


    // old table --------------------------------------------

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
