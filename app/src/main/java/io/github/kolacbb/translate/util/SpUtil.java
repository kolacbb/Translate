package io.github.kolacbb.translate.util;

import android.content.SharedPreferences;

/**
 * Created by Kola on 2016/5/16.
 */
public class SpUtil {

    private static SharedPreferences mSp;

    public static void init(SharedPreferences sp) {
        mSp = sp;
    }

    /**
     * 操作SharedPreferences 的基础方法为私有，只能在本类中访问
     * 所暴露的共有方法为了更方便的使用SharedPreferences进行基本设置
     * */

    private static void saveOrUpdate(String key, String json) {
        mSp.edit().putString(key, json).apply();
    }

    private static String find(String key) {
        return mSp.getString(key, null);
    }

    private static void delete(String key) {
        mSp.edit().remove(key).apply();
    }

    private static void clearAll() {
        mSp.edit().clear().apply();
    }
}