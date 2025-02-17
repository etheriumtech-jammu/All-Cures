package service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import dao.DocRotationDaoImpl;

@Component
public class WeeklyRotationScheduler {

    @Scheduled(cron = "0 0 0 * * MON") // Every Monday at midnight
    public static void rotateResults() {
         DocRotationDaoImpl.getRotatedDocIds();
    }
}
