<?php
$response = array();

include '../../util/db/db_connect.php';
include '../../util/functions.php';

error_reporting(E_ALL);
ini_set('display_errors', 1);

//Get the input request parameters
$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE); //convert JSON into array

$categories = array();

$query = "SELECT * FROM [DBO].[recipe_category]";
global $conn;
$stmt = sqlsrv_query($conn, $query);

if ($stmt === false) {
    $response["status"] = 1;
    $response["message"] = "Unable to find any categories";
} else {
    while( $row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_NUMERIC) ) {
        $categories[] = $row[1];
    }

    if(empty($categories)) {
         $response["status"] = 1;
         $response["message"] = "Unable to find any categories";
    } else {
        $response["status"] = 0;
        $response["categories"] = array($categories);
    }
}

echo json_encode($response);
?>
