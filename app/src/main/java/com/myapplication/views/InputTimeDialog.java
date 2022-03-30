package com.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InputTimeDialog extends Dialog {

    public InputTimeDialog(@NonNull Context context) {
        super(context);
    }

    public InputTimeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected InputTimeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
