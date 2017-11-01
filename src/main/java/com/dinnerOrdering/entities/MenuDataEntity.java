package com.dinnerOrdering.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by vlza1116 on 11.05.2017.
 */
public class MenuDataEntity {


    private ArrayList<String> salads;
    private ArrayList<String> hots;
    private ArrayList<String> hospers;
    private ArrayList<String> fixings;
    private ArrayList<String> drinks;
    private ArrayList<String> breads;
    private HashMap<String, Integer> sets;
    private ArrayList<String> setsList;
    private ArrayList<SetEntity> setEntityList;

    public ArrayList<String> getSetsList() {
        return setsList;
    }

    public void setSetsList(ArrayList<String> setsList) {
        this.setsList = setsList;
    }



    public ArrayList<SetEntity> getSetEntityList() {
        return setEntityList;
    }

    public void setSetEntityList(ArrayList<SetEntity> setEntityList) {
        this.setEntityList = setEntityList;
    }




    public HashMap<String, Integer> getSets() {
        return sets;
    }

    public void setSets(HashMap<String, Integer> sets) {
        this.sets = sets;
    }





    public void initMenuDataEntity(String field, ArrayList<String> list){
        switch (field){
            case "salads": salads = list; break;
            case "hots": hots = list; break;
            case "hospers": hospers = list; break;
            case "fixings": fixings = list; break;
            case "drinks": drinks = list; break;
            case "breads": breads = list; break;
            case "sets": setsList = list; break;
        }

    }

    public ArrayList<String> getSalads() {
        return salads;
    }

    public void setSalads(ArrayList<String> salads) {
        this.salads = salads;
    }

    public ArrayList<String> getHots() {
        return hots;
    }

    public void setHots(ArrayList<String> hots) {
        this.hots = hots;
    }

    public ArrayList<String> getHospers() {
        return hospers;
    }

    public void setHospers(ArrayList<String> hospers) {
        this.hospers = hospers;
    }

    public ArrayList<String> getFixings() {
        return fixings;
    }

    public void setFixings(ArrayList<String> fixings) {
        this.fixings = fixings;
    }

    public ArrayList<String> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<String> drinks) {
        this.drinks = drinks;
    }

    public ArrayList<String> getBreads() {
        return breads;
    }

    public void setBreads(ArrayList<String> breads) {
        this.breads = breads;
    }

/*  private int dataId;
    private int moduleId;
    private float temperature;
    private float power;
    private Date date;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/

}
