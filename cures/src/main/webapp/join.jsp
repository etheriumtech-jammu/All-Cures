<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width,initial-scale=1"/>
  <title>Join Meeting</title>
  <script src="https://unpkg.com/@daily-co/daily-js"></script>
  <style>
    html, body { height: 100%; margin: 0; }
    #daily { width: 100%; height: 100vh; }
  </style>
</head>
<body>
<div id="daily"></div>
<script>
  (async () => {
    // Values injected from the controller
    const roomUrl = "<%= request.getAttribute("roomUrl") %>";
    const token   = "<%= request.getAttribute("token") %>";

    const callFrame = window.DailyIframe.createFrame(
      document.getElementById("daily"),
      { showLeaveButton: true }
    );

    try {
      await callFrame.join({ url: roomUrl, token });
      console.log("Joined:", roomUrl);
    } catch (e) {
      console.error("Join failed", e);
      document.body.innerHTML =
        "<p style='padding:16px;font:16px sans-serif;'>Join failed. Please try again.</p>";
    }
  })();
</script>
</body>
</html>
