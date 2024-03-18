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

    <p>Dear ${doctorLastName},</p>

    <p>We hope this message finds you well. A video consultation appointment has been scheduled with you on All-Cures.com, and we are reaching out to confirm your availability.</p>
     <p><strong>Patient Name:</strong> ${patientName}</p>
    <p><strong>Date & Time:</strong> ${appointmentDate}</p>
    
    <p><strong>Video Consultation Link:</strong> ${videoChatLink}</p>

    <p>Please use the above link to join the video call at the scheduled time. Your prompt attention to this appointment is highly appreciated, and we trust that your expertise will contribute to a swift and effective consultation.</p>

    <p><strong>Important Notes:</strong></p>
    <ol>
        <li>Please ensure that your camera and microphone are in working order.</li>
        <li> Please familiarize yourself with the patient's medical history, if available.</li>
        <li>For any technical assistance, our support team can be reached at ${supportEmail}.</li>
    </ol>

    <p>Thank you for your attention. We look forward to your presence. </p>

    <p>If you have any questions or need further information, please reply to this email without changing the subject line.</p>
 	<p>Thank you!<br>All-Cures Support Team</p>
</body>

</html>
