<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Trec-Apps Login</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
	</head>
	<body>
	
	
	
	
		<div class="container">
			<div id="messageJumbo" class="jumbotron">Enter Basic Details for your Client OAuth Application
			</div>
			
			<form width="100%" class="form" id="Passwordform" action="/Secure/RegisterClientApp" method="post" onsubmit="return true;">
			
				<label name="nameLabel">App Name</label>
				<input type="text" name="appName">
				<br>
				<label name="AppType">App Type</label>
				<input type="radio" name="appType" value="WebApp" checked="checked">Web Application<br>
				<input type="radio" name="appType" value="SinglePage"> Single-Page Application<br>
				<input type="radio" name="appType" value="NativeApp"> Native Application<br>
				<input type="radio" name="appType" value="MobileApp"> Mobile Application<br>
				<input type="submit" class="btn btn-submit" value="Submit">
			</form>
			
		</div>
	
	</body>
</html>
