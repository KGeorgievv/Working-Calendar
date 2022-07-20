package com.myapplication;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    // фунцкия която показва клавиатурата на телефона
    public static void showKeyBoard(View view) {
        view.post(() -> {
            if (view.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    // фунцкия която скрива клавиатурата на телефона
    public static void hideKeyboard(View view) {
        view.post(() -> {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
    }

}
