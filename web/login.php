<?php
$response = array();
include 'db/db_connect.php';
include 'functions.php';

error_reporting(E_ALL);
ini_set('display_errors', 1);
 
//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array
 
//Check for Mandatory parameters
if(isset($input['email']) && isset($input['password'])){
	$email = $input['email'];
	$password = $input['password'];

	$query = "SELECT full_name, password, salt FROM [DBO].[users] WHERE email = ?";
	$params = [&$email];
	global $conn;
	$stmt = sqlsrv_query($conn, $query, $params);

	if ($stmt === false) {
		$response["status"] = 1;
		$response["message"] = "Invalid username and password combination";
	} else {
		if( sqlsrv_fetch( $stmt ) === false ) {
			$response["status"] = 1;
			$response["message"] = "Invalid username and password combination";
		} else {
			$fullName = sqlsrv_get_field($stmt, 0);
			$passwordDb = sqlsrv_get_field($stmt, 1);
			$salt = sqlsrv_get_field($stmt, 2);
		
			if(password_verify(concatPasswordWithSalt($password, $salt), $passwordDb)){
				$response["status"] = 0;
				$response["message"] = "Login successful";
				$response["full_name"] = $fullName;
			} else {
				$response["status"] = 1;
				$response["message"] = "Invalid username and password combination";
			}
		}
	}
	
	sqlsrv_free_stmt( $stmt );
}
else{
	$response["status"] = 2;
	$response["message"] = "Missing mandatory parameters";
}

echo json_encode($response);
?>