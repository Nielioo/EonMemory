<?php

require_once("db_controller.php");
header("Content-Type: application/json");

if (!empty($_POST)) {
    $username = $_POST['username'];
    $title = $_POST['title'];
    $category = $_POST['category'];
    $created = $time;

    $query = $connection->prepare("INSERT INTO `task`(`username`, `title`, `category`, `created`) VALUES (?, ?, ?, ?)");
    $query->bind_param("ssss", $username, $title, $category, $created);
    $result = $query->execute();

    if ($result) {
        $response['message'] = "Task saved";
    } else {
        $response['message'] = "Failed to save";
    }
} else {
  $response['message'] = "No post data";
}

$query->close();
$connection->close();

echo json_encode($response);
?>