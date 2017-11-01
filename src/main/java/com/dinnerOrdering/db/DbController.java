package com.dinnerOrdering.db;

import com.dinnerOrdering.entities.*;
import com.dinnerOrdering.ui.forms.UserForm;
import com.vaadin.event.dd.acceptcriteria.Or;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.xml.transform.Result;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DbController {
    @Autowired
    Properties properties;


    //DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
    @Autowired
    DriverManagerDataSource driverManagerDataSource;

    private final String DB_NAME = "name";
    private final String DB_MODULE_ID = "module_id";
    private final String DB_TEMPERATURE = "temperature";
    private final String DB_POWER = "power";
    private final String DB_DATE = "date";
    private final String DB_USER_ID = "user_id";
    private final String DB_REQUEST_PERIOD = "request_per";
    private final String DB_MODULE_NUM = "module_num";
    private final String DB_MANAGER_NUM = "manager_num";


    public DbController() {
    }

    public DbController(DriverManagerDataSource dm) {
        driverManagerDataSource = dm;
    }

    public MenuDataEntity getMenuDataEntity() {
        ResultSet resultSet;
        MenuDataEntity menuDataEntity = new MenuDataEntity();
        try {

            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            ArrayList<String> list;
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            String[] tables = {"salads", "hots", "hospers", "fixings", "drinks", "breads", "sets"};
            for(String s: tables){
                statement = connection.prepareStatement("SELECT name FROM " + s);
                resultSet = statement.executeQuery();
                list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(resultSet.getString(DB_NAME));
                }
                menuDataEntity.initMenuDataEntity(s, list);

                /*
                statement = connection.prepareStatement("SELECT name, price FROM sets");
                resultSet = statement.executeQuery();
                while(resultSet.next()){
                    map.put(resultSet.getString("name"), resultSet.getInt("price"));
                }
                menuDataEntity.setSets(map);
                */

                ArrayList<SetEntity> setEntityList = new ArrayList<>();
                statement = connection.prepareStatement("SELECT * FROM sets");
                resultSet = statement.executeQuery();
                SetEntity setEntity;
                while (resultSet.next()) {
                    setEntity = new SetEntity();

                    setEntity.setId(resultSet.getInt("id"));
                    setEntity.setName(resultSet.getString("name"));
                    setEntity.setPrice(resultSet.getInt("price"));
                    setEntity.setSoup(resultSet.getBoolean("soup"));
                    setEntity.setSalad(resultSet.getBoolean("salad"));
                    setEntity.setHot(resultSet.getBoolean("hot"));
                    setEntity.setHosper(resultSet.getBoolean("hosper"));
                    setEntity.setFixings(resultSet.getBoolean("fixings"));
                    setEntity.setDrink(resultSet.getBoolean("drink"));
                    setEntity.setBread(resultSet.getBoolean("bread"));
                    setEntityList.add(setEntity);
                }
                menuDataEntity.setSetEntityList(setEntityList);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuDataEntity;
    }

    public HashMap<String, Integer> getUserNamesAndIDs(){
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        try {
            ResultSet resultSet;
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("SELECT username, user_id FROM users WHERE role_id = 1");
            resultSet = statement.executeQuery();

            while(resultSet.next()){
                map.put(resultSet.getString("username"), resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public UserEntity getCurrentUserEntity() {
        ResultSet resultSet;
        UserEntity userEntity = null;
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
       //     statement = connection.prepareStatement("SELECT user_id, username, name FROM users u WHERE u.username = ?" +
       //             SecurityContextHolder.getContext().getAuthentication().getName() + "");
            statement = connection.prepareStatement("SELECT user_id, username, name, autoupdate FROM users u WHERE u.username = ?");
            statement.setString(1,SecurityContextHolder.getContext().getAuthentication().getName());
            resultSet = statement.executeQuery();
            resultSet.next();
            userEntity = new UserEntity();
            userEntity.setUser_id(resultSet.getInt("user_id"));
            userEntity.setName( resultSet.getString("name"));
            userEntity.setUsername(resultSet.getString("username"));
            userEntity.setAutoupdate(resultSet.getBoolean("autoupdate"));
            statement = connection.prepareStatement("SELECT sum(sum) s FROM orders WHERE user_id = ?");
            statement.setInt(1, userEntity.getUser_id());
            resultSet = statement.executeQuery();
            resultSet.next();
            userEntity.setBalance(resultSet.getInt("s"));

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userEntity;

    }

    public UserEntity getUserEntityByID(int id){
        ResultSet resultSet;
        UserEntity userEntity = null;
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("SELECT user_id, username, name, autoupdate FROM users u WHERE u.user_id = ?");
            statement.setInt(1,id);
            resultSet = statement.executeQuery();
            resultSet.next();
            userEntity = new UserEntity();
            userEntity.setUser_id(resultSet.getInt("user_id"));
            userEntity.setName( resultSet.getString("name"));
            userEntity.setUsername(resultSet.getString("username"));
            userEntity.setAutoupdate(resultSet.getBoolean("autoupdate"));
            statement = connection.prepareStatement("SELECT sum(sum) s FROM orders WHERE user_id = ?");
            statement.setInt(1, userEntity.getUser_id());
            resultSet = statement.executeQuery();
            resultSet.next();
            userEntity.setBalance(resultSet.getInt("s"));
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userEntity;

    }

    public void addBalanceToDB(int id, int sum){
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            String columnNames = "(user_id,sum)";
            statement = connection.prepareStatement("INSERT INTO orders " + columnNames + " VALUES (?,?)");            statement.setInt(1,id);
            statement.setInt(1, id);
            statement.setInt(2, sum);
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOrder(OrderEntity orderEntity, UserEntity userEntity){
        ResultSet resultSet;
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            String columnNames = "(user_id,soup,salad,hot,hosper,fixings,drink,bread,sum,set_id)";
            statement = connection.prepareStatement("INSERT INTO orders " + columnNames + " VALUES (?,?,?,?,?,?,?,?,?,?)");
            statement.setInt(1, userEntity.getUser_id());
            //statement.setDate(2, new Date(Calendar.getInstance().getTimeInMillis()));

            statement.setString(2, orderEntity.getSoup());
            statement.setString(3, orderEntity.getSalad());
            statement.setString(4, orderEntity.getHot());
            statement.setString(5, orderEntity.getHosper());
            statement.setString(6, orderEntity.getFixings());
            statement.setString(7, orderEntity.getDrink());
            statement.setString(8, orderEntity.getBread());
            statement.setInt(9, -orderEntity.getSum());
            statement.setInt(10, getSetIdFromDB(orderEntity));
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSetIdFromDB(OrderEntity orderEntity){
        ResultSet resultSet = null;
        int id = 0;
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("SELECT id FROM sets WHERE name = '" + orderEntity.getSet() + "'");
            resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt("id");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public boolean alreadyOrdered(UserEntity userEntity){
        ResultSet resultSet = null;
        boolean ordered = false;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date dt = new java.util.Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            dt = c.getTime();
            String date = "date >= '" + dateFormat.format(new java.util.Date()) + " 00:00:00.000'" +
                    "  AND date < '" + dateFormat.format(dt)  + " 00:00:00.000'";

            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT NULL FROM orders o WHERE  " + date + " AND user_id = ? AND o.sum < 0");
            statement.setInt(1, userEntity.getUser_id());
             resultSet = statement.executeQuery();
             ordered = resultSet.next();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ordered;
    }



    public ArrayList<OrderEntity> getOrderEntitiesByDate(Date date){
        ArrayList<OrderEntity> entities = null;
        try {
            Date tomorrowDate = tomorrowDate(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Connection connection = driverManagerDataSource.getConnection();
            String restSelect = "o.id, o.date, u.name, s.name as 'set_name', o.soup, o.salad, o.hot, o.hosper, o.fixings, o.bread, o.drink, o.sum ";
            String strDate = "date >= '" + dateFormat.format(date) + " 00:00:00.000'" +
                    "  AND date < '" + dateFormat.format(tomorrowDate)  + " 00:00:00.000'";
            PreparedStatement statement = connection.prepareStatement("SELECT " + restSelect +
                    " FROM orders o LEFT OUTER JOIN sets s ON o.set_id = s.id JOIN users u on o.user_id = u.user_id WHERE  " + strDate);
            ResultSet resultSet = statement.executeQuery();



            entities = new ArrayList<>();
            OrderEntity entity;
            while (resultSet.next()) {
                entity = new OrderEntity();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setDate(resultSet.getDate("date"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                if (timestamp != null)
                    entity.setDate(new java.util.Date(timestamp.getTime()));

                entity.setSet(resultSet.getString("set_name"));
                entity.setSoup(resultSet.getString("soup"));
                entity.setSalad(resultSet.getString("salad"));
                entity.setHot(resultSet.getString("hot"));
                entity.setHosper(resultSet.getString("hosper"));
                entity.setFixings(resultSet.getString("fixings"));
                entity.setBread(resultSet.getString("bread"));
                entity.setDrink(resultSet.getString("drink"));
                entity.setSum(resultSet.getInt("sum"));
                entities.add(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public OrderEntity getOrderByUserID(UserEntity userEntity) {
        OrderEntity entity = null;
        try {
            Date date = new Date();
            Date tomorrowDate = tomorrowDate(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Connection connection = driverManagerDataSource.getConnection();
            String restSelect = "o.id, o.date, u.name, s.name as 'set_name', o.soup, o.salad, o.hot, o.hosper, o.fixings, o.bread, o.drink, o.sum ";
            String strDate = "date >= '" + dateFormat.format(date) + " 00:00:00.000'" +
                    "  AND date < '" + dateFormat.format(tomorrowDate)  + " 00:00:00.000'";
            PreparedStatement statement = connection.prepareStatement("SELECT " + restSelect +
                    " FROM orders o LEFT OUTER JOIN sets s ON o.set_id = s.id JOIN users u on o.user_id = u.user_id WHERE  " + strDate + " AND o.sum < 0 AND o.user_id = " +
                    userEntity.getUser_id());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            entity = new OrderEntity();
            entity.setId(resultSet.getInt("id"));
            entity.setName(resultSet.getString("name"));
            entity.setDate(resultSet.getDate("date"));
            Timestamp timestamp = resultSet.getTimestamp("date");
            if (timestamp != null)
                entity.setDate(new java.util.Date(timestamp.getTime()));
            entity.setSet(resultSet.getString("set_name"));
            entity.setSoup(resultSet.getString("soup"));
            entity.setSalad(resultSet.getString("salad"));
            entity.setHot(resultSet.getString("hot"));
            entity.setHosper(resultSet.getString("hosper"));
            entity.setFixings(resultSet.getString("fixings"));
            entity.setBread(resultSet.getString("bread"));
            entity.setDrink(resultSet.getString("drink"));
            entity.setSum(resultSet.getInt("sum"));
            }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

/*
    public List<OrderEntity> getOrderEntitiesByName(String name){

    }*/

    private Date tomorrowDate(Date date){
        Date dt = date;
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        return dt;
    }

    public void sendToDbTest(){
        ResultSet resultSet;
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("INSERT INTO test (name) VALUES (?)");
            statement.setString(1,"hi");
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSumOfDay(Date date)  {
        ResultSet resultSet = null;
        int sum = 0;
        try {
            Date tomorrowDate = tomorrowDate(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Connection connection = driverManagerDataSource.getConnection();
            String strDate = "date >= '" + dateFormat.format(date) + " 00:00:00.000'" +
                    "  AND date < '" + dateFormat.format(tomorrowDate)  + " 00:00:00.000'";

            PreparedStatement statement;
            statement = connection.prepareStatement("SELECT sum(sum) s FROM orders o WHERE " + strDate + " AND o.sum < 0");
            resultSet = statement.executeQuery();
            resultSet.next();
            sum = resultSet.getInt("s");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -sum;
    }

    public void addAutoupdatingOrders(){
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            /*statement = connection.prepareStatement("INSERT INTO orders (user_id, set_id, soup, salad, hot, hosper, fixings, drink, bread, sum)\n" +
                    "SELECT o.user_id, o.set_id, o.soup, o.salad, o.hot, o.hosper, o.fixings, o.drink, o.bread, o.sum\n" +
                    "FROM orders o, users u\n" +
                    "WHERE u.autoupdate = 1 AND\n" +
                    "date = (SELECT max(date) from orders o2 WHERE o2.user_id = o.user_id AND o2.sum < 0)\n" +
                    "AND o.user_id = u.user_id");
*/
            Date date = new Date();
            Date tomorrowDate = tomorrowDate(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = "date >= '" + dateFormat.format(date) + " 00:00:00.000'" +
                    "  AND date < '" + dateFormat.format(tomorrowDate)  + " 00:00:00.000'";
            statement = connection.prepareStatement("INSERT INTO orders (user_id, set_id, soup, salad, hot, hosper, fixings, drink, bread, sum) \n" +
                    "                    SELECT o.user_id, o.set_id, o.soup, o.salad, o.hot, o.hosper, o.fixings, o.drink, o.bread, o.sum \n" +
                    "                    FROM orders o, users u \n" +
                    "                    WHERE u.autoupdate = 1 AND \n" +
                    "                    (SELECT sum(sum) s FROM orders o4 WHERE u.user_id = o4.user_id) >= -1000  " +
                    "                    date = (SELECT max(date) from orders o2 WHERE o2.user_id = o.user_id AND o2.sum < 0) \n" +
                    "                    AND o.user_id = u.user_id\n" +
                    "                    AND NOT exists (select NULL from orders o3 WHERE o3.user_id = o.user_id AND "+ strDate + " AND o3.sum < 0)");
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAutoupdateStatus(Boolean turnOn, UserEntity userEntity) {
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            if(turnOn){
                statement = connection.prepareStatement("UPDATE `testfordinners`.`users` SET `autoupdate`='1' WHERE `user_id`=?");
            } else  statement = connection.prepareStatement("UPDATE `testfordinners`.`users` SET `autoupdate`='0' WHERE `user_id`=?");
            statement.setInt(1, userEntity.getUser_id());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<EmailEntity> getEmails() {
        ArrayList<EmailEntity> emailEntities = new ArrayList<>();
        try {
            ResultSet resultSet;
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("SELECT email FROM emails");
            resultSet = statement.executeQuery();
            EmailEntity entity;
            while (resultSet.next()) {
                entity = new EmailEntity();
                entity.setEmail(resultSet.getString("email"));
                emailEntities.add(entity);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emailEntities;
    }

    public void delEmail(String email) {
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("DELETE FROM emails WHERE email = '" + email + "'");
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmail(String email) {
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("INSERT INTO emails (email) VALUES  ('" + email + "')");
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delOrderByOrderID(OrderEntity currOrderEntity) {
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("DELETE FROM orders WHERE id = " + currOrderEntity.getId());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkAutoSending() {
        ResultSet resultSet = null;
        boolean status = false;
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("SELECT value v FROM configuration c WHERE name = 'enableAutoSending'");
            resultSet = statement.executeQuery();
            resultSet.next();
            status = resultSet.getBoolean("v");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public void updateAutoSending(Boolean turnOn) {
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;
            if(turnOn){
                statement = connection.prepareStatement("UPDATE `testfordinners`.`configuration` SET `value`='1' WHERE `name`=?");
            } else  statement = connection.prepareStatement("UPDATE `testfordinners`.`configuration` SET `value`='0' WHERE `name`=?");
            statement.setString(1, "enableAutoSending");
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSelectedRows(Set<OrderEntity> selectedItems) {
        try {
            Connection connection = driverManagerDataSource.getConnection();
            PreparedStatement statement;

            for(OrderEntity orderEntity: selectedItems){
                statement = connection.prepareStatement("DELETE FROM orders WHERE id = " + orderEntity.getId());
                statement.execute();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void copyRowsFromGrid(Set<OrderEntity> selectedItems, int count) {

        try {
        Connection connection = driverManagerDataSource.getConnection();
        PreparedStatement statement;

        for(OrderEntity orderEntity: selectedItems){
            for(int i = 0; i < count; i++) {
                statement = connection.prepareStatement("INSERT INTO orders (user_id, set_id, soup, salad, hot, hosper, fixings, drink, bread, sum) " +
                        "                    SELECT o.user_id, o.set_id, o.soup, o.salad, o.hot, o.hosper, o.fixings, o.drink, o.bread, o.sum " +
                        "                    FROM orders o, users u " +
                        "                    WHERE o.id = '" + orderEntity.getId() + "' " +
                        "                    AND o.user_id = u.user_id");
                statement.execute();
            }
        }
        connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
