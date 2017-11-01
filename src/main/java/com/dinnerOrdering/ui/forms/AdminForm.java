package com.dinnerOrdering.ui.forms;

import com.dinnerOrdering.config.DriverManagerMy;
import com.dinnerOrdering.db.DbController;
import com.dinnerOrdering.entities.EmailEntity;
import com.dinnerOrdering.entities.OrderEntity;
import com.dinnerOrdering.entities.UserEntity;
import com.dinnerOrdering.logic.Logic;
import com.dinnerOrdering.scheduling.Scheduling;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Notification;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.util.NumberUtils;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AdminForm extends AdminFormDesign {

    DbController db;
    UserEntity userEntity;
    HashMap<String, Integer> namesMap;
    ArrayList<OrderEntity> orderEntities;
    ArrayList<EmailEntity> emailEntities;

    public AdminForm() {
        db = new DbController(new DriverManagerMy().dataSource());
        namesMap = db.getUserNamesAndIDs();
        nameComb.setItems(namesMap.keySet());
        insertButton.setEnabled(false);
        makeGrid(db.getOrderEntitiesByDate(new Date()));
        emailEntities = db.getEmails();
        emailGrid.setItems(emailEntities);

        //File f = new File(System.getProperty("catalina.base") +
        //        File.separator + "workbooks" + File.separator + "workbook.xls");
        //File f = new File( "C:/workbooks/workbook.xls");
        //Notification.show("catal " + System.getProperty("catalina.base"));
        File inTomcat = new File("/opt/tomcat/webapps/workbooks/workbook.xls");
            Resource res = new FileResource(inTomcat);
            FileDownloader fd = new FileDownloader(res);
            fd.extend(getTableButton);


        enableAutoSendingChb.setValue(db.checkAutoSending());
        dateField.setValue(LocalDate.now());
        changedDateField();

        addEmailBtn.addClickListener(e -> addEmail(addEmailTxt.getValue()));
        delEmailBtn.addClickListener(e -> delEmail(emailGrid.getSelectedItems()));
        nameComb.addValueChangeListener(e -> changeUser());
        insertButton.addClickListener(e -> addBalance());
        deleteSelectedRowsBtn.addClickListener(e -> deleteSelectedRows());
        copyRowBtn.addClickListener(e -> copyRow());
        makeAutoOrdersAndSendMailButton.addClickListener(e -> makeAutoOrdersAndSendMail());
        enableAutoSendingChb.addValueChangeListener(e -> db.updateAutoSending(enableAutoSendingChb.getValue()));


        dateField.addValueChangeListener(e -> changedDateField());

        getTableButton.addClickListener(e -> saveExcel());
    }

    public boolean isNumeric(String str)
    {
        return str.matches("\\d+");  //match a number with optional '-' and decimal.
    }

    private void deleteSelectedRows() {
        db.deleteSelectedRows(grid.getSelectedItems());
        Page.getCurrent().reload();

    }

    private void makeAutoOrdersAndSendMail() {
        new Scheduling().tasksToDoOnTime();
        Page.getCurrent().reload();
    }

    private void copyRow() {
        if(isNumeric(countToCopyText.getValue())){
            db.copyRowsFromGrid(grid.getSelectedItems(), Integer.parseInt(countToCopyText.getValue()));
        }
        else Notification.show("Введите число");
        Page.getCurrent().reload();
    }

    public boolean checkAutoSendingEnableStatus() {
        return enableAutoSendingChb.getValue();
    }

    private void addEmail(String email) {
        if(new Logic().isValidEmailAddress(email)) {
            db.addEmail(email);
            Page.getCurrent().reload();
        }
        else
            Notification.show("Неверный адрес");
    }

    private void delEmail(Set<EmailEntity> set) {
        for (Iterator<EmailEntity> it = set.iterator(); it.hasNext(); ) {
            EmailEntity f = it.next();
            db.delEmail(f.getEmail());
        }
        Page.getCurrent().reload();
    }

    public void saveExcel(){

        //private String basepath = VaadinService.getCurrent()
        //        .getBaseDirectory().getAbsolutePath();
        Date date = java.sql.Date.valueOf((dateField.getValue()));
        new Logic().makeExcel(db.getOrderEntitiesByDate(date), date);


    }

    public void changeUser(){
        insertButton.setEnabled(true);
        userEntity = db.getUserEntityByID(namesMap.get(nameComb.getValue()));
        balanceLabel.setValue(String.valueOf(userEntity.getBalance()));
    }

    public void addBalance(){
        String str = insertSumText.getValue();
        if(!str.matches("-?\\d+(\\.\\d+)?")){
            Notification.show(
                    "Введите число");
            return;
        }
        db.addBalanceToDB(userEntity.getUser_id(), Integer.parseInt(str));
        changeUser();
        Notification.show("Сумма внесена");
    }

    private void changedDateField(){
        Date date = java.sql.Date.valueOf((dateField.getValue()));
        makeGrid(db.getOrderEntitiesByDate(date));
        sumForDayLabel.setValue(String.valueOf(db.getSumOfDay(date)));

    }

    private void makeGrid( ArrayList<OrderEntity> orderEntities){
        grid.setItems(orderEntities);
        //grid.addColumn(OrderEntity::getName).setCaption("Имя");

    }







}
