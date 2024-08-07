package util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import Chat_Function.NotificationService;

public class NotificationJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Fetch the appointment details
        // Send the notification
    	String formattedTime="";
        String tokenName = (String) context.getJobDetail().getJobDataMap().get("tokenName");
        String time = (String) context.getJobDetail().getJobDataMap().get("AppointmentTime");
        String docName = (String) context.getJobDetail().getJobDataMap().get("DoctorName");
 //       SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
        Date date;
        try {
            date = inputFormat.parse(time);
            
            // Step 2: Format the Date object to the desired time format
            SimpleDateFormat outputFormat = new SimpleDateFormat("h.mm a");
           
             formattedTime = outputFormat.format(date).toUpperCase(); // Ensure uppercase AM/PM
            
            System.out.println("NotificationJob is executing at: " + formattedTime); // Output: 3.30 PM
        } catch (ParseException e) {
            e.printStackTrace();
        }
  
        System.out.println("Sending notification to tokenName: " + tokenName);
        // Add logic to send notification using appointmentId
     // Logic to fetch appointment details and send the notification
        NotificationService notificationService = new NotificationService();
        // Assuming NotificationService has a method to send notification by appointmentId
//        String title="All Cures!";
       String body="🔔 Reminder: Your appointment with " +docName + " is coming up at "+ formattedTime +" . Stay healthy and be prepared! ";
      
        try {
			notificationService.sendNotification(body,tokenName,"Video","1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

