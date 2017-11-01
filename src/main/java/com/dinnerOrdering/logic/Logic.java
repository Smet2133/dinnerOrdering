package com.dinnerOrdering.logic;

import com.dinnerOrdering.config.DriverManagerMy;
import com.dinnerOrdering.db.DbController;
import com.dinnerOrdering.entities.EmailEntity;
import com.dinnerOrdering.entities.OrderEntity;
import com.sun.mail.smtp.SMTPTransport;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class Logic {

    public void makeExcel(ArrayList<OrderEntity> entities, Date date) {
        DbController db = new DbController(new DriverManagerMy().dataSource());
        try {
            Cell cell;
            Row row;

            Workbook wb = new HSSFWorkbook();
            CreationHelper createHelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet("Лист1");
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("Дата: ");
            cell = row.createCell(1);
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat(
                    createHelper.createDataFormat().getFormat("dd/mm/yy"));
            cell.setCellValue(date);
            cell.setCellStyle(cellStyle);


            CellStyle style = wb.createCellStyle();
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setLeftBorderColor(IndexedColors.GREEN.getIndex());
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setRightBorderColor(IndexedColors.BLUE.getIndex());
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());

            int cellNum = 0;

            String[] headersNames = {"Номер", "Имя", "Тип", "Суп", "Салат", "Горячее","Хоспер",
                    "Гарнир", "Хлеб", "Напиток", "Цена"};

            row = sheet.createRow(1);

            for(cellNum = 0; cellNum < headersNames.length; cellNum++)
            {
                cell = row.createCell(cellNum);
                cell.setCellValue(headersNames[cellNum]);
                cell.setCellStyle(style);
            }
            /*

            cell = row.createCell(cellNum++);
            cell.setCellValue("Номер");
            cell.setCellStyle(style);

            row.createCell(cellNum++).setCellValue("Номер");
            row.createCell(cellNum++).setCellValue("Имя");
            row.createCell(cellNum++).setCellValue("Тип");
            row.createCell(cellNum++).setCellValue("Суп");
            row.createCell(cellNum++).setCellValue("Салат");
            row.createCell(cellNum++).setCellValue("Горячее");
            row.createCell(cellNum++).setCellValue("Хоспер");
            row.createCell(cellNum++).setCellValue("Гарнир");
            row.createCell(cellNum++).setCellValue("Хлеб");
            row.createCell(cellNum++).setCellValue("Напиток");
            row.createCell(cellNum++).setCellValue("Цена");
*/
            int i = 2;
            for (OrderEntity e : entities) {
                if (e.getSum() > 0) continue;
                row = sheet.createRow(i++);
                int j = 0;
                row.createCell(j++).setCellValue(i - 2);
                row.createCell(j++).setCellValue(e.getName());
                row.createCell(j++).setCellValue(e.getSet());
                row.createCell(j++).setCellValue(e.getSoup());
                row.createCell(j++).setCellValue(e.getSalad());
                row.createCell(j++).setCellValue(e.getHot());
                row.createCell(j++).setCellValue(e.getHosper());
                row.createCell(j++).setCellValue(e.getFixings());
                row.createCell(j++).setCellValue(e.getBread());
                row.createCell(j++).setCellValue(e.getDrink());
                row.createCell(j++).setCellValue(-e.getSum());
            }

            row = sheet.createRow(i++);
            row.createCell(9).setCellValue("Сумма:");
            row.createCell(10).setCellValue(db.getSumOfDay(date));

            for(i = 0; i < cellNum; i++){
                //if (i == 4) continue;;
                sheet.autoSizeColumn(i);
            }
            //sheet.setColumnWidth(4, 240);

            //File myFile = new File("webapps/workbook.xls");
            //File f = new File(System.getProperty("catalina.base") +                   File.separator + "webapps" + File.separator + "workbook.xls");
            //File f = new File ("C:/workbooks/workbook.xls");
            File inTomcat = new File("/opt/tomcat/webapps/workbooks/workbook.xls");
            FileOutputStream fileOut = new FileOutputStream(inTomcat);
            wb.write(fileOut);
            fileOut.close();



        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public void sendFileEmail(ArrayList<EmailEntity> addresses) {
        try {
            final String username = "dinnerordering@gmail.com";
            final String password = "dinnerspass";


        /*
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "567");
        */

            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

            // Get a Properties object
            Properties props = System.getProperties();
            props.setProperty("mail.smtps.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.setProperty("mail.smtps.auth", "true");
            props.put("mail.smtps.quitwait", "false");

            Session session = Session.getInstance(props, null);

            // -- Create a new message --
            final MimeMessage msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress("dinnerordering@gmail.com"));


            InternetAddress[] recipientAddress = new InternetAddress[addresses.size()];
            int counter = 0;
            for (EmailEntity s: addresses) {
                recipientAddress[counter] = new InternetAddress(s.getEmail());
                counter++;
            }
            msg.setRecipients(Message.RecipientType.TO, recipientAddress);
            /*
            for(EmailEntity s: addresses){
                msg.setRecipient(Message.RecipientType.TO, InternetAddress.parse(s.getEmail(), false));
            }
            */
            //msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("lex_vss90@mail.ru", false));


            msg.setSubject("dinners");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText("");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            //File myFile = new File("webapps/workbook.xls");
            //String filename = "C:/workbooks/workbook.xls";
            //File f = new File(System.getProperty("catalina.base") +                     File.separator + "webapps" + File.separator + "workbook.xls");
            //File f = new File ("C:/workbooks/workbook.xls");
            File inTomcat = new File("/opt/tomcat/webapps/workbooks/workbook.xls");
            DataSource source = new FileDataSource(inTomcat);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("dinners" + new Date() + ".xls");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            msg.setContent(multipart );

            SMTPTransport t = (SMTPTransport) session.getTransport("smtps");

            t.connect("smtp.gmail.com", username, password);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

}
