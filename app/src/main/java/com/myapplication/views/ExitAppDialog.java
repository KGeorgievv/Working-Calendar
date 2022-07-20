package com.myapplication.views;

import android.app.AlertDialog;
import android.content.Context;

import com.myapplication.R;

/**
 * Обект който показва диалог за излизане от приложението
 */
public class ExitAppDialog {

    // обект който генерира диалога
    private final AlertDialog.Builder builder;
    // интерфейс който ще следи за промени
    private Listener mListener;

    public ExitAppDialog(Context context) {

        // създава се диалог
        builder = new AlertDialog.Builder(context);
        // заглавие на диалога
        builder.setTitle(context.getString(R.string.exit_dialog_title));
        // съобщение на диалога
        builder.setMessage(context.getString(R.string.exit_dialog_message));
        // бутон "изход" на диалога
        builder.setPositiveButton(context.getString(R.string.exit), (dialogInterface, i) -> mListener.onExit());
        // бутон "отказ" на диалога
        builder.setNegativeButton(context.getString(R.string.cancel), (dialogInterface, i) -> {});
    }

    // функция която добавя интерфейса който ще информара за промени
    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    // функция която показва диалога
    public void show() {
        builder.show();
    }

    /**
     * Интерфейс който информира кога потребителят иска да излезне от прилжението
     */
    public interface Listener {

        void onExit();

    }

}
