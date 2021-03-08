<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Trec-Apps User Update</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
	</head>
	<body>
	
	
	<div class="container">
		<div id="messageJumbo" class="jumbotron">
			<h1 id="messageJumboHeader">Log-In</h1>
			</div>

			<div width="100%" class="form" id="Userform">
				<label id="Label">Username:</label>
				<input id="input" type="text"><br>
				<label id="Label2">Password:</label>
				<input id="input2" type="password" name="password"><br>
				<button class="btn btn-submit" onclick="submitLogin()">Submit</button>
			</div>
			
			<div hidden width="100%" class="form" id="UpdateForm">
				<input type="text" id="userLabel" readonly="readonly" name="username">
				
				<label id="colorLabel">Account Color:</label>
				<input id="colorInput" type="color" name="color"><br>
				
				
				<label id="firstNameLabel">First Name:</label>
				<input id="firstNameInput" type="text" name="firstName"><br>
				
				<label id="lastNameLabel">Last Name:</label>
				<input id="lastNameInput" type="text" name="lastName"><br>
				
				
				<label id="backupEmailLabel">Backup Email:</label>
				<input id="backupEmailInput" type="email" name="backupEmail"><br>
				
				
				<label id="maxLoginAttemptsLabel">Permit Failed Log-in Attempts:</label>
				<input id="maxLoginAttemptsInput" type="number" name="maxLoginAttempts"><br>
				
				
				<label id="lockTimeLabel">Lock Account Time (10 minutes):</label>
				<input id="lockTimeInput" type="number" name="lockTime"><br>
				
				<label id="timeForValidTokenLabel">Time Before Token Expiration (10 minutes):</label>
				<input id="timeForValidTokenInput" type="number" name="timeForValidToken"><br>
				
				<button class="btn btn-submit" onclick="submitUpdate()">Submit</button>
			</div>
		</div>
	
	<script type="application/JavaScript">
	
	var token;
	var account;
	// user.setTimeForValidToken(newSettings.getTimeForValidToken());
	// user.setValidTimeFromActivity(newSettings.getValidTimeFromActivity());
	
	function submitLogin() {
		let xhr = (new XMLHttpRequest() || new ActiveXObject("Microsoft.HTTPRequest"));
		
		let initUrl = window.location.href;
		let username = document.getElementById("input").value;
		let password = document.getElementById("input2").value;
		
		initUrl += '/UpdateUser';
		
		xhr.onreadystatechange = function() {
			
			if (this.readyState === 4 && this.status == 200) {
				
				document.getElementById("Userform").hidden = true;
				document.getElementById("UpdateForm").hidden = false;
				
				let retObj = JSON.parse( this.responseText );
				
				token = retObj.token;
				account = retObj.account;
				
				
				
			} else if (this.readyState === 4 && this.status == 401) {
				document.getElementById("messageJumboHeader").innerHTML = "Log-In: Previous Log in Failed!";
			}
		}
		
		xhr.open("POST", initUrl);
		xhr.send();
	}
	
	function submitUpdate() {
		let xhr = (new XMLHttpRequest() || new ActiveXObject("Microsoft.HTTPRequest"));
		
		let initUrl = window.location.href;
		
		initUrl += '/UpdateUser';
		

		let obj = { 
				color: document.getElementById("colorInput").value,
				firstName: document.getElementById("firstNameInput").value,
				lastName: document.getElementById("lastNameInput").value,
				backupEmail: document.getElementById("backupEmailInput").value,
				maxLoginAttempts: document.getElementById("maxLoginAttemptsInput").value,
				lockTime: document.getElementById("lockTimeInput").value,
				timeForValidToken: document.getElementById("timeForValidTokenInput").value
		}
		
		let objStr = JSON.stringify(obj);
		
		xhr.onreadystatechange = function() {
			
			if (this.readyState === 4 && this.status == 200) {
				
				document.getElementById("Userform").hidden = true;
				document.getElementById("UpdateForm").hidden = false;
				
				let retObj = JSON.parse( this.responseText );
				
				token = retObj.token;
				account = retObj.account;
				
				
				
			} else if (this.readyState === 4 && this.status == 401) {
				document.getElementById("messageJumboHeader").innerHTML = "Log-In: Previous Log in Failed!";
			}
		}
		
		xhr.open("PUT", initUrl);
		
		xhr.setRequestHeader("Authorization", token);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(objStr);
	}
	
	
	</script>
	
	</body>
</html>