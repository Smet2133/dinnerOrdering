package com.dinnerOrdering.entities;

import java.util.Date;

public class OrderEntity {
    private int id;
    private String name;
    private Date date;
    private String set;
    private String soup;
    private String salad;
    private String hot;
    private String hosper;
    private String fixings;
    private String bread;
    private String drink;
    private int sum;

    public int getId() {        return id;
    }

    public void setId(int id) {        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getSoup() { return soup;
    }

    public void setSoup(boolean b) {
        if (b) soup = "+";
        else soup = "";
    }

    public String getSalad() {
        return salad;
    }

    public void setSalad(String salad) {
        this.salad = salad;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getHosper() {
        return hosper;
    }

    public void setHosper(String hosper) {
        this.hosper = hosper;
    }

    public String getFixings() {
        return fixings;
    }

    public void setFixings(String fixings) {
        this.fixings = fixings;
    }

    public String getBread() {
        return bread;
    }

    public void setBread(String bread) {
        this.bread = bread;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setSoup(String s) {
        if(s == null) soup = "";
        else if (s.equals("+")) soup = "+";
        else soup = "";
    }


    // "u.name, s.name as 'set_name', o.soup, o.salad, o.hot, o.hosper, o.fixings, o.bread, o.drink, o.sum ";


}
