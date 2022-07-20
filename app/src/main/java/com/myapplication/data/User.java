package com.myapplication.data;

/**
 * Обект който съдържа данни за потребителя
 */
public class User {

    // заплата
    private final float salary;
    // брой почивни дни
    private final int ptoDays;
    // заплата на час
    private float salaryPerHour;
    // брой работни дни
    private int workingDays;

    public User(float salary, int ptoDays) {
        this.salary = salary;
        this.ptoDays = ptoDays;
    }

    public float getSalary() {
        return salary;
    }

    public int getPtoDays() {
        return ptoDays;
    }

    public float getSalaryPerHour() {
        return salaryPerHour;
    }

    // записва работните часове и смята заплатата на час спрямо тях
    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
        this.salaryPerHour = salary / this.workingDays / 8F;
    }

    public int getWorkingDays() {
        return workingDays;
    }

}
