<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Trec-Apps Register</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
	</head>
	<body>
	
		<script type="application/JavaScript">
			function redirect(redirectUrl) {
			
				//console.log("Redirect Funciton called");
			
				//let colonSep = window.location.href.indexOf('://');
			
				// add 5 to colonSep! If '://' is absent, value will be 4. Either way, the 
				// main .com url will be at least 5 characters
				//colonSep = window.location.href.indexOf('/', colonSep + 5);
			
				//let newUrl = window.location.href.indexOf(0, colonSep);

				window.location.href = redirectUrl;
			}
		
			function createAccount() {
				redirect("/NewUser");
			}
			
			function loginAccount() {
				redirect("/LogIn");
			}
			
			
		</script>
	
		<div class="container">
			<div id="messageJumbo" class="jumbotron"><h1>Error! ${staus}</h1>
			<br>
			<h1> ${message}</h1>
			<h3>Please either login or create an account!</h3>
			</div>
			
			<button class="btn btn-submit" onclick="createAccount()">Create Account</button>
			<button class="btn btn-submit" onclick="loginAccount()">Log In</button>
			
		</div>
	</body>
</html>