package io.github.kolacbb.translate.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Kola on 2016/7/4.
 */
public class InputMethodUtils {
    /**
     * 为给定的编辑器开启软键盘
     * @param context
     * @param editText 给定的编辑器
     */
    public static void openSoftKeyboard(Context context, EditText editText){
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 关闭软键盘
     * @param view
     */
    public static void closeSoftKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //如果软键盘已经开启
        if(inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 切换软键盘的状态
     * @param context
     */
    public static void toggleSoftKeyboardState(Context context){
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
