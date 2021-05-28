<?php

$dbhost = "localhost";
$dbuser = "root";
$dbpassword = "";
$dbname = "eon_memory";

$now = new DateTime("now", new DateTimeZone('Asia/Jakarta'));
$time = $now->format('Y-m-d H:i:s');

$connection = mysqli_connect($dbhost, $dbuser, $dbpassword, $dbname) or die("Error connecting to database");

?>