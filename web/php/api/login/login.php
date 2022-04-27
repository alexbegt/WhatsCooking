<?php
$response = array();

include '../../util/functions.php';

error_reporting(E_ALL);
ini_set('display_errors', 1);
 
//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array
 
//Check for Mandatory parameters
if(isset($input['username']) && isset($input['password']) && isset($input['application'])) {
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

    $username = $input['username'];
    $password = $input['password'];

    $query = "SELECT first_name, last_name, password, salt FROM [DBO].[users] WHERE username = ?";
    $params = [&$username];
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
            $firstName = sqlsrv_get_field($stmt, 0);
            $lastName = sqlsrv_get_field($stmt, 1);
            $passwordDb = sqlsrv_get_field($stmt, 2);
            $salt = sqlsrv_get_field($stmt, 3);
        
            if(password_verify(concatPasswordWithSalt($password, $salt), $passwordDb)){
                $response["status"] = 0;
                $response["message"] = "Login successful";
                $response["first_name"] = $firstName;
                $response["last_name"] = $lastName;
            } else {
                $response["status"] = 1;
                $response["message"] = "Invalid username and password combination";
            }
        }
    }

    if ($stmt !== false) {
        sqlsrv_free_stmt( $stmt );
    }
}
else{
    $response["status"] = 2;
    $response["message"] = "Missing mandatory parameters";
}

echo json_encode($response);
?>
