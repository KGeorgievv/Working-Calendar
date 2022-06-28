package com.myapplication.data;

public class User {

    private final float salary;
    private final int ptoDays;
    private float salaryPerHour;
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

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
        this.salaryPerHour = salary / this.workingDays / 8F;
    }

    public int getWorkingDays() {
        return workingDays;
    }

}
