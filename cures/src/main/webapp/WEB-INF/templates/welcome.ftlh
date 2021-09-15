<!DOCTYPE html>
<html>
<head>
 <title>Welcome</title>
</head>
<body>
	<h3>Dear ${name},</h3>

	<p>A warm welcome and lots of good wishes on becoming part of our growing team.</p>
	<p>We are pleased to inform you that you will work with the following team members:</p>
	<ol>
		<#list teamMembers as teamMember>		
			<li>${teamMember}</li>			
		</#list>		
	</ol>	
	<p>< style="color:red;font-size:14px;font-style: italic">Regards</p>
	<p>allcures etherium Team</p>
	<p>Location: ${location}</p>
</body>
</html>
