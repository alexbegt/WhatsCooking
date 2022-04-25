<?php
$response = array();

include '../../util/functions.php';

error_reporting(E_ALL);
ini_set('display_errors', 1);
 
//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array
 
//Check for Mandatory parameters
if(isset($input['email']) && isset($input['password']) && isset($input['full_name']) && isset($input['application'])){
    switch ($input['application']) {
        case 'whats-cooking':
            include '../../util/db/db_connect.php';
            break;
        case 'quiz-kidz':
            include '../../util/db/qk_db_connect.php';
            break;
        default:
            $response["status"] = 4;
            $response["message"] = "Invalid application passed";
            echo json_encode($response);
            return;
    }

	$email = $input['email'];
	$password = $input['password'];
	$fullName = $input['full_name'];

	//Check password 
	if (!password_validate($password)) {
		$response["status"] = 1;
		$response["message"] = "Password does not meet standards";
	} else {	
		//Check if user already exist
		if(!userExists($email)){
			//Get a unique Salt
			$salt         = getSalt();
		
			//Generate a unique password Hash
			$passwordHash = password_hash(concatPasswordWithSalt($password, $salt), PASSWORD_DEFAULT);
		
			//Query to register new user
			$insertQuery  = "INSERT INTO [DBO].[users](email, full_name, password, salt) VALUES (?,?,?,?)";
			$params       = array($email, $fullName, $passwordHash, $salt);

			$stmt = sqlsrv_query($conn, $insertQuery, array($email, $fullName, $passwordHash, $salt));

			if ($stmt === false) {
				$response["status"] = 3;
				$response["message"] = "Error creating user";
			} else {
				$response["status"] = 0;
				$response["message"] = "User created";
			}
		}
		else{
			$response["status"] = 2;
			$response["message"] = "User already exists";
		}
	}
}
else{
	$response["status"] = 4;
	$response["message"] = "Missing mandatory parameters";
}

echo json_encode($response);
?>
