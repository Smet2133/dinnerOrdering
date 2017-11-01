package com.dinnerOrdering.ui.forms;

import com.dinnerOrdering.config.DriverManagerMy;
import com.dinnerOrdering.db.DbController;
import com.dinnerOrdering.entities.MenuDataEntity;
import com.dinnerOrdering.entities.OrderEntity;
import com.dinnerOrdering.entities.SetEntity;
import com.dinnerOrdering.entities.UserEntity;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Date;

public class UserForm extends UserFormDesign {
    public MenuDataEntity menuDataEntity;
    public UserEntity userEntity;
    public OrderEntity orderEntity;
    public OrderEntity currOrderEntity;
    public SetEntity currSetEntity;
    DbController db;
    public Boolean soupChecked = false;
    ArrayList<ComboBox> comboBoxes;

    public int price = 0;


    public UserForm(UserEntity userEntity){
        //setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //setComponentAlignment(this,Alignment.MIDDLE_CENTER);
        db = new DbController(new DriverManagerMy().dataSource());
        this.menuDataEntity = db.getMenuDataEntity();
        this.userEntity = userEntity;
        confirmButton.setEnabled(false);
        headerLabel.setValue("Добро пожаловать, " + userEntity.getUsername() + "!");
        setComponentAlignment(headerLabel, Alignment.MIDDLE_CENTER);


        comboBoxes = new ArrayList<>();
        comboBoxes.add(saladComb);
        comboBoxes.add(hosperComb);
        comboBoxes.add(hotComb);
        comboBoxes.add(fixingsComb);
        comboBoxes.add(drinkComb);
        comboBoxes.add(breadComb);



        saladComb.setItems(menuDataEntity.getSalads());
        hotComb.setItems(menuDataEntity.getHots());
        hosperComb.setItems(menuDataEntity.getHospers());
        fixingsComb.setItems(menuDataEntity.getFixings());
        drinkComb.setItems(menuDataEntity.getDrinks());
        breadComb.setItems(menuDataEntity.getBreads());
        setComb.setItems(menuDataEntity.getSetsList());
        balanceLabel.setValue(String.valueOf(userEntity.getBalance()));
        autoupdateChb.setValue(userEntity.getAutoupdate());


        if(db.alreadyOrdered(userEntity)){
            confirmButton.setEnabled(false);
            setComb.setEnabled(false);
            messageLabel.setValue("Сегодня вы уже делали заказ");
            currOrdLayout.setVisible(true);
            currOrderEntity = db.getOrderByUserID(userEntity);
            //Binder<OrderEntity> binder = new Binder<>();
            setLbl.setValue(currOrderEntity.getSet());
            soupLbl.setValue(currOrderEntity.getSoup());
            saladLbl.setValue(currOrderEntity.getSalad());
            hotLbl.setValue(currOrderEntity.getHot());
            hospLbl.setValue(currOrderEntity.getHosper());
            fixingsLbl.setValue(currOrderEntity.getFixings());
            breadLbl.setValue(currOrderEntity.getBread());
            drinkLbl.setValue(currOrderEntity.getDrink());
        }

        setComb.addValueChangeListener(e -> setChanged());
        confirmButton.addClickListener(e -> sentOrderToDb());
        for(ComboBox comboBox : comboBoxes){
            comboBox.addValueChangeListener(e -> checkCombFilling());
        }
        autoupdateChb.addValueChangeListener(e -> db.updateAutoupdateStatus(autoupdateChb.getValue(), userEntity));
        showSoupsBtn.addClickListener(e -> showSoups());
        delCurrButton.addClickListener(e -> delCurrOrderAndReload());
    }

    private void checkCombFilling() {
        for(ComboBox comboBox : comboBoxes){
            if(comboBox.isEnabled() && comboBox.isEmpty()){
                confirmButton.setEnabled(false);
                messageLabel.setValue("Заполните все поля");
                return;
            }
            messageLabel.setValue("");
            confirmButton.setEnabled(true);
        }
    }

    private void delCurrOrderAndReload(){
        db.delOrderByOrderID(currOrderEntity);
        Page.getCurrent().reload();
    }

    private void showSoups() {
        Window window = new Window("Супы");
        window.setWidth(300.0f, Unit.PIXELS);
        VerticalLayout layout = new VerticalLayout();
        Label label1 = new Label("ПОНЕДЕЛЬНИК - БОРЩ");
        Label label2 = new Label("ВТОРНИК - ДОМАШНИЙ КУРИНЫЙ СУП с вермишелью");
        Label label3 = new Label("СРЕДА - РАССОЛЬНИК");
        Label label4 = new Label("ЧЕТВЕРГ - КРЕМ-СУП ИЗ ШАМПИНЬОНОВ");
        Label label5 = new Label("ПЯТНИЦА - СЫРНЫЙ СУП С КОПЧЕНОЙ ГРУДИНКОЙ и КУРИЦЕЙ");
        layout.addComponents(label1, label2, label3, label4, label5);
        window.setContent(layout);
        window.center();
        window.setWidth(600.0f, Unit.PIXELS);
        getUI().getUI().addWindow(window);
    }

    void sentOrderToDb(){

        orderEntity = new OrderEntity();
        orderEntity.setName(userEntity.getName());
        orderEntity.setDate(new Date());
        orderEntity.setSet(setComb.getValue());
        orderEntity.setSoup(soupChecked);
        orderEntity.setHosper(hosperComb.getValue());
        orderEntity.setHot(hotComb.getValue());
        orderEntity.setSalad(saladComb.getValue());
        orderEntity.setFixings(fixingsComb.getValue());
        orderEntity.setDrink(drinkComb.getValue());
        orderEntity.setBread(breadComb.getValue());
        orderEntity.setSum(price);
        db.addOrder(orderEntity, userEntity);
        confirmButton.setEnabled(false);
        Page.getCurrent().reload();
    }

    void setChanged(){
        //logic
        for(SetEntity entity : menuDataEntity.getSetEntityList()){
            if(entity.getName().equals(setComb.getValue())){
                currSetEntity = entity;
                break;
            }
        }

        if(!enoughBalance()){
            confirmButton.setEnabled(false);
            messageLabel.setValue("Недостаточно денег на счету");
            return;
        }




        price = currSetEntity.getPrice();
        priceLabel.setValue(Integer.toString(price));

        soupChecked = currSetEntity.isSoup();
        saladComb.setEnabled(currSetEntity.isSalad());
        hotComb.setEnabled(currSetEntity.isHot());
        hosperComb.setEnabled(currSetEntity.isHosper());
        fixingsComb.setEnabled(currSetEntity.isFixings());
        drinkComb.setEnabled(currSetEntity.isDrink());
        breadComb.setEnabled(currSetEntity.isBread());


        for(ComboBox comboBox : comboBoxes){
            comboBox.clear();
        }

        checkCombFilling();

/*

        if(setComb.getValue().equals("Салат + Суп + Напиток")) {
            saladComb.setEnabled(true);
            soupChecked = true;
        }
        else if(setComb.getValue().equals("Салат + Горячее + Напиток")) {
            //price = 225;
            saladComb.setEnabled(true);
            hotComb.setEnabled(true);
        }
        else if(setComb.getValue().equals("Суп + Горячее + Напиток")) {
            //price = 225;
            hotComb.setEnabled(true);
            soupChecked = true;
        }
        else if(setComb.getValue().equals("Салат + Хоспер + Напиток")) {
            //price = 250;
            saladComb.setEnabled(true);
            hosperComb.setEnabled(true);
        }
        else if(setComb.getValue().equals("Суп + Хоспер + Напиток")) {
            //price = 250;
            hosperComb.setEnabled(true);
            soupChecked = true;
        }
        else if(setComb.getValue().equals("Салат + Суп + Горячее + Напиток")) {
            //price = 270;
            saladComb.setEnabled(true);
            hotComb.setEnabled(true);
            soupChecked = true;
        }
        else if(setComb.getValue().equals("Салат + Суп + Хоспер + Напиток")) {
            //price = 290;
            saladComb.setEnabled(true);
            hosperComb.setEnabled(true);
            soupChecked = true;
        }
        else if(setComb.getValue().equals("Обед месяца без супа")){
            soupChecked = false;
        }
        else if(setComb.getValue().equals("Обед месяца с супом")){
            soupChecked = true;
        }
        else if(setComb.getValue().equals("Рыбный обед")){
            soupChecked = true;
            saladComb.setEnabled(true);
        }
        else{
            saladComb.setEnabled(true);
            hosperComb.setEnabled(true);
            hotComb.setEnabled(true);
        }
        priceLabel.setValue(Integer.toString(menuDataEntity.getSets().get(setComb.getValue())));
*/


    }

    boolean enoughBalance(){
        return userEntity.getBalance() + 1000 >= currSetEntity.getPrice();
    }


}
