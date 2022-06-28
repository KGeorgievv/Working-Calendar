package com.myapplication.views;

import android.app.AlertDialog;
import android.content.Context;

import com.myapplication.R;

public class ExitAppDialog {

    private final AlertDialog.Builder builder;
    private Listener mListener;

    public ExitAppDialog(Context context) {

        builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.exit_dialog_title));
        builder.setMessage(context.getString(R.string.exit_dialog_message));
        builder.setPositiveButton(context.getString(R.string.exit), (dialogInterface, i) -> mListener.onExit());
        builder.setNegativeButton(context.getString(R.string.cancel), (dialogInterface, i) -> {});
    }

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void show() {
        builder.show();
    }

    public interface Listener {

        void onExit();

    }

}
