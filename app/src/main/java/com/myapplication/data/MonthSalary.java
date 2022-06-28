package com.myapplication.data;

import java.util.List;

public class MonthSalary {

    private final User user;
    private final List<LoggedTime> loggedTimes;

    public MonthSalary(User user, Month month, List<LoggedTime> loggedTimes) {
        this.user = user;
        this.user.setWorkingDays(month.getWorkingDaysCount());
        this.loggedTimes = loggedTimes;
    }

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

    public float getSalaryPerHour() {
        return user.getSalaryPerHour();
    }

    public int getMonthSalary() {
        float hourSalary = user.getSalaryPerHour();
        int workDays = user.getWorkingDays();
        return (int) (workDays * hourSalary * 8);
    }

}
