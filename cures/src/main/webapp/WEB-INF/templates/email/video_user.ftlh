<!DOCTYPE html>
<html lang="en">

<head>

  
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> Video Consultation - Appointment Confirmation</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"> <!-- Use the latest (edge) version of IE rendering engine -->
    <meta name="x-apple-disable-message-reformatting">  <!-- Disable auto-scale in iOS 10 Mail entirely -->
    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->

    <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700" rel="stylesheet">
    
    <style>
        /* Add your styles if needed */
    </style>
</head>
<!-- Comment inside the head section -->
<body>
    <p><strong>Subject:</strong> Video Consultation - Appointment Confirmation</p>
<div>
        <p>Dear ${patientName},</p>
         <p>Greetings from All-Cures!</p>
        <p>Your video consultation appointment has been confirmed successfully.</p>
        <p><strong>Doctor's Name:</strong> ${doctorName}</p>
        <p><strong>Date & Time:</strong> ${appointmentDate}</p>
        <p><strong>Video Chat Link:</strong> <a href="${videoChatLink}">${videoChatLink}</a></p>
        <p>Please use the above link to join the video call at the scheduled time.</p>
        <p>If you have any questions or need further assistance, please feel free to contact us at ${supportEmail}.</p>
        <p>We look forward to assisting you during the consultation.</p>
        <p>Best regards,</p>
        <p>All-Cures Support Team</p>
    </div>
</body>

</html>
