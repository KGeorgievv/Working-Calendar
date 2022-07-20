package com.myapplication.data;

import java.util.List;

/**
 * Обект който съдържа данни да потребителя
 * и лист от неговите записани часове
 */
public class MonthSalary {

    private final User user; // обект потребител
    private final List<LoggedTime> loggedTimes; // лист от записани часове

    public MonthSalary(User user, Month month, List<LoggedTime> loggedTimes) {
        this.user = user;
        this.user.setWorkingDays(month.getWorkingDaysCount());
        this.loggedTimes = loggedTimes;
    }

    /**
     * Смята текущата заплатата като събира всички записани часове
     *
     * Формула:
     *
     * Текуща заплата = работни часове * заплата на час
     *                  + извънредни часове * заплата на час * 1.5
     *                  + болнични часове * заплата на час * 0.8
     *                  + часове отпуска * заплата на час
     */
    public long calculateSalary() {
        int workHours = 0;
        int overtimeHours = 0;
        int sickLeaveHours = 0;
        int timeOffHours = 0;

        for (int i = 0; i < loggedTimes.size(); i++) {
            LoggedTime loggedTime = loggedTimes.get(i);
            workHours += loggedTime.getWork();
            overtimeHours += loggedTime.getOvertime();
            sickLeaveHours += loggedTime.getSickLeave();
            timeOffHours += loggedTime.getTimeOff();
        }

        float hourSalary = user.getSalaryPerHour();
        double salary = workHours * hourSalary +
                overtimeHours * hourSalary * 1.5 +
                sickLeaveHours * hourSalary * 0.8 +
                timeOffHours * hourSalary;

        return Math.round(salary);
    }

    // връща заплатата на час
    public float getSalaryPerHour() {
        return user.getSalaryPerHour();
    }

    // връща заплатата за даден месец
    public int getMonthSalary() {
        float hourSalary = user.getSalaryPerHour();
        int workDays = user.getWorkingDays();
        return (int) (workDays * hourSalary * 8);
    }

}
