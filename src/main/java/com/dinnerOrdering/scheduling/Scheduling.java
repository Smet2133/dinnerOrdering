package com.dinnerOrdering.scheduling;

import com.dinnerOrdering.config.DriverManagerMy;
import com.dinnerOrdering.db.DbController;
import com.dinnerOrdering.logic.Logic;
import com.dinnerOrdering.ui.SecuredUI;
import com.vaadin.ui.UI;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Scheduling {
    int hours = 17;
    int minutes = 30;
    int seconds = 0;
    DbController db = new DbController(new DriverManagerMy().dataSource());


    public void tasksToDoOnTime(){
        db.addAutoupdatingOrders();
        new Logic().makeExcel(db.getOrderEntitiesByDate(new Date()), new Date());
        new Logic().sendFileEmail(db.getEmails());
    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Date now = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(now);

            ; // gets hour in 24h format

            if(!(calendar.get(Calendar.HOUR_OF_DAY) == hours && (calendar.get(Calendar.MINUTE) == minutes)))
                return;
            if (calendar.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY && calendar.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY) {
                if(db.checkAutoSending())
                    tasksToDoOnTime();
            }
        }
    };


    public void startScheduling() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, hours);
        today.set(Calendar.MINUTE, minutes);
        today.set(Calendar.SECOND, seconds);

// every night at 2am you run your task
        Timer timer = new Timer();
        timer.schedule(timerTask, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day
    }

    /*
    private TimerTask runScheduleMethods() {
        return new TimerTask() {
            @Override
            public void run() {
                Date now = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                if(calendar.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY && calendar.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY) {
                    DbController db = new DbController(new DriverManagerMy().dataSource());
                    db.addAutoupdatingOrders();
                    new Logic().makeExcel(db.getOrderEntitiesByDate(new Date()), new Date());

                    new Logic().sendFileEmail(db.getEmails());
                }
            }
        };

    }
*/

}
