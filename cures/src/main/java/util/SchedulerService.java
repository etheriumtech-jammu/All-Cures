package util;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.util.Date;
import java.time.LocalTime;
import java.util.Calendar;

public class SchedulerService {

    public void scheduleNotification(String tokenName, LocalTime appointmentStartTime, Date appointmentDate,String docName) throws SchedulerException {
        try  {
            
            // Calculate notification time (30 minutes before the appointment start time)
            LocalTime notificationTime = appointmentStartTime.minusMinutes(30);
            
            // Convert notification time to java.util.Date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(appointmentDate);
            calendar.set(Calendar.HOUR_OF_DAY, notification_Time.getHour());
            calendar.set(Calendar.MINUTE, notification_Time.getMinute());
            calendar.set(Calendar.SECOND, 0);
            Date notificationTime = new Date(calendar.getTimeInMillis());
            System.out.println("Notification Time: " + notificationTime);
          
            // Create job detail
            JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                    .withIdentity("notificationJob" , "group1")
                    .usingJobData("tokenName", tokenName)
                    .usingJobData("AppointmentTime", appointmentStartTime.toString())
                    .usingJobData("DoctorName", docName)
                    .build();

            // Create trigger
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("notificationTrigger" , "group1")
                    .startAt(notificationTime)
                    .build();

            // Schedule the job
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SchedulerException("Failed to schedule notification", e);
        }
    }
}
