package com.dinnerOrdering;

import com.dinnerOrdering.config.DriverManagerMy;
import com.dinnerOrdering.db.DbController;
import com.dinnerOrdering.ui.forms.AdminForm;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

public class TestExcel {


    public static void main2(String[] args) throws IOException {
        DbController db;
        db = new DbController(new DriverManagerMy().dataSource());
       // new AdminForm().makeExcel(db.getOrderEntitiesByDate(new Date()), new Date());


    }
}
