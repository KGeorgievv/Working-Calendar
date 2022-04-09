package com.myapplication.data;

public class User {

    private int salary;
    private int ptoDays;
    private int salaryPerHour;

    public User(int salary, int ptoDays) {
        this.salary = salary;
        this.ptoDays = ptoDays;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getSalary() {
        return salary;
    }

    public void setPtoDays(int ptoDays) {
        this.ptoDays = ptoDays;
    }

    public int getPtoDays() {
        return ptoDays;
    }

}
