package com.myapplication.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.myapplication.R;
import com.myapplication.Utils;
import com.myapplication.config.PreferenceManager;
import com.myapplication.data.LoggedTime;
import com.myapplication.data.TimeType;
import com.myapplication.data.User;
import com.myapplication.database.LoggedTimeDao;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

/**
 * Обект който показва диалог за запазване на часове
 */
public class InputTimeDialog {

    private final Context context;
    // обект който генерира диалога
    private final AlertDialog.Builder builder;
    private final View content;
    // избраната дата за запазване на часове
    private final LocalDate date;
    // кутиийката за попълване на часове
    private final TextInputEditText viewInput;
    // интерфейс който ще следи за промени
    private Listener mListener;

    public InputTimeDialog(
            Context context,
            LocalDate date,
            TimeType type,
            LoggedTimeDao loggedTimeDao
    ) {
        this.context = context;
        this.date = date;

        // взимане на данните за потребителя
        User user = new PreferenceManager(context).getUser();
        content = generateContent();

        // генериране на диалог
        builder = new AlertDialog.Builder(context);
        // добавяне на заглавие
        builder.setCustomTitle(generateTitle(type));
        // добавяне на изгледа на диалога
        builder.setView(content);
        // добавяне на бутон "добави"
        builder.setPositiveButton(context.getString(R.string.add), (dialogInterface, i) -> logHours(type, loggedTimeDao));
        // добавяне на бутон "отказ"
        builder.setNegativeButton(context.getString(R.string.cancel), (dialogInterface, i) -> {
        });

        // попълва вече запсани часове
        prefillHours(type, loggedTimeDao);
        // попълва заглавието
        setupTitle(type);
        // попълва допълнителния текст
        setupHintText(type, user, loggedTimeDao);

        // показва клавиатурата на телефона
        viewInput = content.findViewById(R.id.input);
        Utils.showKeyBoard(viewInput);
    }

    // функция която добавя интерфейса който ще информара за промени
    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    // фунцкия която показва диалога
    public void show() {
        builder.show();
    }

    // фунцкия която генерира изгледа на диалога
    private View generateContent() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.dialog_input_time, null);
    }

    // фунцкия която показва заглавието на диалога
    // и променя цвета спрямо типа на часовете
    private View generateTitle(TimeType type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewTitle = inflater.inflate(R.layout.dialog_title, null);
        View header = viewTitle.findViewById(R.id.header);
        TextView title = viewTitle.findViewById(R.id.text);
        header.setBackgroundColor(getHeaderColor(type));
        title.setText(context.getString(type.getStrRes()));
        return viewTitle;
    }

    // функция която връща цвета на диалога спрямо типа на часовете
    private int getHeaderColor(TimeType type) {
        int headerColor = 0;
        switch (type) {
            // работни часове - синьо
            case Work:
                headerColor = ContextCompat.getColor(this.context, R.color.work);
                break;
            // почивни часове - жълто
            case TimeOff:
                headerColor = ContextCompat.getColor(this.context, R.color.time_off);
                break;
            // неплатени часове - лилаво
            case UnpaidTimeOff:
                headerColor = ContextCompat.getColor(this.context, R.color.time_off_unpaid);
                break;
            // болнични часове - зелено
            case SickTimeOff:
                headerColor = ContextCompat.getColor(this.context, R.color.sick_leave);
                break;
            // извънредни часове - червено
            case Overtime:
                headerColor = ContextCompat.getColor(this.context, R.color.overtime);
                break;
        }
        return headerColor;
    }

    // фунцкия която променя заглавието спрямо типа на часовете
    private void setupTitle(TimeType type) {
        TextView viewTitle = content.findViewById(R.id.text_title);
        String typeStr = context.getString(type.getStrRes());
        String title = context.getString(R.string.input_hours, typeStr);
        viewTitle.setText(title);
    }

    // фунцкия която изписва допълнителен текст за типа на часовете
    private void setupHintText(TimeType type, User user, LoggedTimeDao dao) {
        TextView viewHint = content.findViewById(R.id.text_hint);
        TextInputEditText viewInput = content.findViewById(R.id.input);

        // ако типа е почивни часове
        if (type == TimeType.TimeOff) {
            viewHint.setVisibility(View.VISIBLE);
            // смята колко още часове са останали
            int hoursLeft = user.getPtoDays() * 8 - dao.getLoggedTimeOffHours();

            // ако са повече от 0 изписва колко остават
            if (hoursLeft > 0) {
                String text = context.getString(
                        R.string.hint_remaining_days,
                        hoursLeft,
                        user.getPtoDays() * 8
                );
                viewHint.setText(text);

                // ако са по-малко или равни на 0 изписва, че вече няма часове
                // и прави полето за часове неактивно
            } else {
                viewHint.setText(R.string.no_days_left);
                viewInput.setEnabled(false);
            }

            // когато типа е различен от почивни дни допълнителният текст не се скрива
        } else {
            viewHint.setVisibility(View.GONE);
        }
    }

    // фунцкия която попълва записаните вече часове спрямо типа, ако има такива
    private void prefillHours(TimeType type, LoggedTimeDao dao) {
        // ако няма записани часове не прави нищо
        LoggedTime loggedTime = dao.getLoggedTimeByDate(date);
        if (loggedTime == null) return;

        int hours = 0;
        switch (type) {
            // чете раобните часове
            case Work:
                hours = loggedTime.getWork();
                break;
            // чете почивните часове
            case TimeOff:
                hours = loggedTime.getTimeOff();
                break;
            // чете неплатените часове
            case UnpaidTimeOff:
                hours = loggedTime.getUnpaidTimeOff();
                break;
            // чете болничните часове
            case SickTimeOff:
                hours = loggedTime.getSickLeave();
                break;
            // чете извънредните часове
            case Overtime:
                hours = loggedTime.getOvertime();
                break;
        }

        // ако има въведени часове добавя "Изтрии" бутон
        // и попълва вече въведените часове
        if (hours != 0) {
            addClearButton(type, dao);
            viewInput.setText(String.valueOf(hours));
            viewInput.setSelection(viewInput.length());
        }
    }

    // функция която добавя "Изчисти" бутон
    private void addClearButton(TimeType type, LoggedTimeDao dao) {
        builder.setNeutralButton(context.getString(R.string.clear_hours),
                (dialogInterface, i) -> clearHours(type, dao));
    }

    // функция която записва данните спрямо типа на часовете
    private void logHours(TimeType type, LoggedTimeDao dao) {
        // ако полето за часове е празно не прави нищо
        String hoursStr = viewInput.getText().toString();
        if (StringUtils.isBlank(hoursStr)) return;

        // проверява дали има запис в базата и ако няма създава празен
        boolean hasRecord = dao.getLoggedTimeByDate(date) != null;
        if (!hasRecord) dao.saveLoggedTime(new LoggedTime(date));

        // взима часовете които са въведени
        int hours = Integer.parseInt(hoursStr);

        switch (type) {
            // ъпдейтва раобните часове и информира интерфейса
            case Work:
                dao.updateWorkTime(date, hours);
                notifyListener();
                break;
            // ъпдейтва почивните часове и информира интерфейса
            case TimeOff:
                dao.updateTimeOffHours(date, hours);
                notifyListener();
                break;
            // ъпдейтва неплатените часове и информира интерфейса
            case UnpaidTimeOff:
                dao.updateUnpaidTimeOffHours(date, hours);
                notifyListener();
                break;
            // ъпдейтва болничните часове и информира интерфейса
            case SickTimeOff:
                dao.updateSickTimeOffHours(date, hours);
                notifyListener();
                break;
            // ъпдейтва извънредните часове и информира интерфейса
            case Overtime:
                dao.updateOvertimeHours(date, hours);
                notifyListener();
                break;
        }
    }

    // функция която изтрива данните спрямо типа на часовете
    private void clearHours(TimeType type, LoggedTimeDao dao) {
        switch (type) {
            // изтрива работните часове и информира интерфейса
            case Work:
                dao.clearWorkTime(date);
                notifyListener();
                break;
            // изтрива почивдните часове и информира интерфейса
            case TimeOff:
                dao.clearTimeOffHours(date);
                notifyListener();
                break;
            // изтрива неплатените часове и информира интерфейса
            case UnpaidTimeOff:
                dao.clearUnpaidTimeOffHours(date);
                notifyListener();
                break;
            // изтрива болничните часове и информира интерфейса
            case SickTimeOff:
                dao.clearSickTimeOffHours(date);
                notifyListener();
                break;
            // изтрива извънредните часове и информира интерфейса
            case Overtime:
                dao.clearOvertimeHours(date);
                notifyListener();
                break;
        }
    }

    // функция която информира интерфейса
    private void notifyListener() {
        if (mListener != null) mListener.onLoggedTime(date);
    }

    /**
     * Интерфейс който информира кога са запазени часове спрямо избрана дата
     */
    public interface Listener {

        void onLoggedTime(LocalDate date);

    }

}
