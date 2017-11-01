package com.dinnerOrdering.entities;

public class SetEntity {
    private int id;
    private String name;
    private int price = 0;
    private boolean soup = false;
    private boolean salad = false;
    private boolean hot = false;
    private boolean hosper = false;
    private boolean fixings = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSoup() {
        return soup;
    }

    public void setSoup(boolean soup) {
        this.soup = soup;
    }

    public boolean isSalad() {
        return salad;
    }

    public void setSalad(boolean salad) {
        this.salad = salad;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public boolean isHosper() {
        return hosper;
    }

    public void setHosper(boolean hosper) {
        this.hosper = hosper;
    }

    public boolean isFixings() {
        return fixings;
    }

    public void setFixings(boolean fixings) {
        this.fixings = fixings;
    }

    public boolean isDrink() {
        return drink;
    }

    public void setDrink(boolean drink) {
        this.drink = drink;
    }

    public boolean isBread() {
        return bread;
    }

    public void setBread(boolean bread) {
        this.bread = bread;
    }

    private boolean drink = false;
    private boolean bread = false;

}

