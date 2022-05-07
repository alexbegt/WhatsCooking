<?php
$response = array();

include '../../util/functions.php';

error_reporting(E_ALL);
ini_set('display_errors', 1);

//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

//Check for Mandatory parameters
if(isset($input['first_name']) && isset($input['last_name']) && isset($input['email']) && isset($input['username']) && isset($input['password']) && isset($input['application']) ){
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

    $firstName = $input['first_name'];
    $lastName  = $input['last_name'];
    $email     = $input['email'];
    $username  = $input['username'];
    $password  = $input['password'];

    //Check password
    if (!password_validate($password)) {
        $response["status"] = 1;
        $response["message"] = "Password does not meet standards";
    } else {
        if(userExists($username)) {
            $response["status"] = 2;
            $response["message"] = "Username already in use";
            echo json_encode($response);
            return;
        }

        if(emailExists($email)) {
            $response["status"] = 3;
            $response["message"] = "Email already in use";
            echo json_encode($response);
            return;
        }

        //Check if user already exist
        //Get a unique Salt
        $salt         = getSalt();

        //Generate a unique password Hash
        $passwordHash = password_hash(concatPasswordWithSalt($password, $salt), PASSWORD_DEFAULT);

        //Query to register new user
        $insertQuery  = "INSERT INTO [DBO].[users](first_name, last_name, email, username, password, salt) VALUES (?,?,?,?,?,?) ; SELECT SCOPE IDENTITY() AS uniqueId";
        $params       = array($firstName, $lastName, $email, $username, $passwordHash, $salt);
        $insertStatement = sqlsrv_query($conn, $insertQuery, array($email, $fullName, $passwordHash, $salt));

        if ($insertStatement === false) {
            $response["status"] = 4;
            $response["message"] = "Error creating user";
        } else {
            function lastId($queryID) {
                sqlsrv_next_result($queryID);
                sqlsrv_fetch($queryID);
                return sqlsrv_get_field($queryID, 0);
            }

            $response["status"] = 0;
            $response["message"] = "User created";
            $response["account_id"] = "User created " . lastId($stmt);
        }

        if ($insertStatement !== false) {
            sqlsrv_free_stmt( $insertStatement );
        }
    }
}
else{
    $response["status"] = 5;
    $response["message"] = "Missing mandatory parameters";
}

echo json_encode($response);
?>
