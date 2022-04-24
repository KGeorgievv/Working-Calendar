package com.myapplication.data;

public class User {

    private final int salary;
    private final int ptoDays;
    private int salaryPerHour;
    private int workingDays;

    public User(int salary, int ptoDays) {
        this.salary = salary;
        this.ptoDays = ptoDays;
    }

    public int getSalary() {
        return salary;
    }

    public int getPtoDays() {
        return ptoDays;
    }

    public int getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
        this.salaryPerHour = salary / this.workingDays / 8;
    }

    public int getWorkingDays() {
        return workingDays;
    }

}
