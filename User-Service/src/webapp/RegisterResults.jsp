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
	
			<div id="failedMode" class="jumbotron" ${failHide}>
			
				<h1>We're Sorry, Your application failed to generate!</h1>
			
			</div>
			
			<div id="SuccessMode" class="jumbotron" ${workMode}>
			
				<h1>Congratulations! Your App has been registered!</h1><br>
				<h4>Client Name: </h4> <p>${clientName}</p><br>
				<h4>Client ID: </h4> <p>${clientId }</p><br>
				<h4>Client Type:</h4> <p>${clientType }</p>
				
				<div id="appSecretDiv" ${secretHide }>
				
					<h4>Client Secret: </h4> <p>${clientSecret}</p>
				
				</div>
			
			</div>
			
			<a href="/Secure/home">Home Page</a><br>
	
		</div>
	</body>
</html>