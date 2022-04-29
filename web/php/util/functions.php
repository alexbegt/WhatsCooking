<?php
$random_salt_length = 32;

/**
* Queries the database and checks whether the email provided is already in use
*
* @param $email
*
* @return
*/
function emailExits($email){
	$query = "SELECT email FROM [DBO].[users] WHERE email = ?";
	$params = [&$email];
	global $conn;
	$stmt = sqlsrv_query($conn, $query, $params, array( "Scrollable" => SQLSRV_CURSOR_KEYSET ));

	if ($stmt === false) {
		//sqlsrv_free_stmt( $stmt );
	 	return false;
	} else {
		$row_count = sqlsrv_num_rows( $stmt );

		if($row_count === 1){
			sqlsrv_free_stmt( $stmt );
			return true;
		}

		sqlsrv_free_stmt( $stmt );
	}

	return false;
}

/**
* Queries the database and checks whether the user already exists
* 
* @param $username
* 
* @return
*/
function userExists($username){
	$query = "SELECT email FROM [DBO].[users] WHERE username = ?";
	$params = [&$email];
	global $conn;
	$stmt = sqlsrv_query($conn, $query, $params, array( "Scrollable" => SQLSRV_CURSOR_KEYSET ));

	if ($stmt === false) {
		//sqlsrv_free_stmt( $stmt );
	 	return false;
	} else {
		$row_count = sqlsrv_num_rows( $stmt );

		if($row_count === 1){
			sqlsrv_free_stmt( $stmt );
			return true;
		}
		
		sqlsrv_free_stmt( $stmt );
	}
 
	return false;
}
 
/**
* Creates a unique Salt for hashing the password
* 
* @return
*/
function getSalt(){
	global $random_salt_length;
	return bin2hex(openssl_random_pseudo_bytes($random_salt_length));
}
 
/**
* Creates password hash using the Salt and the password
* 
* @param $password
* @param $salt
* 
* @return
*/
function concatPasswordWithSalt($password, $salt){
	global $random_salt_length;

	if($random_salt_length % 2 == 0){
		$mid = $random_salt_length / 2;
	}
	else{
		$mid = ($random_salt_length - 1) / 2;
	}
 
	return substr($salt, 0, $mid - 1).$password.substr($salt, $mid, $random_salt_length - 1);
 
}

function password_validate($password, $min_length=8, $min_lowercases=1, $min_uppercases=1, $min_numbers=1, $min_specials=0) {
    preg_replace('#[a-z]#', '', $password, -1, $lowercases);
    preg_replace('#[A-Z]#', '', $password, -1, $uppercases);
    preg_replace('#[0-9]#', '', $password, -1, $numbers);
    preg_replace('#[^\w]#', '', $password, -1, $specials);

    return (mb_strlen($password) >= $min_length && $lowercases >= $min_lowercases && $uppercases >= $min_uppercases && $numbers >= $min_numbers && $specials >= $min_specials);
  }
?>
