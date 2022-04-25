<?php
$connectionInfo = ["UID" => "abehrhof", "pwd" => 'E9RE8ih!fBaE9P$^*5z$Ztr*', "Database" => "Quiz-Kidz", "LoginTimeout" => 30, "Encrypt" => 1, "TrustServerCertificate" => 0];
$serverName = "tcp:whats-cooking.database.windows.net,1433";
$conn = sqlsrv_connect($serverName, $connectionInfo);

// Check connection
if ($conn === false) {
    echo "Failed to connect to SQL: " . sqlsrv_errors();
}
?>
