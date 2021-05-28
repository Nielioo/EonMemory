<?php

require_once("db_controller.php");
header("Content-Type: application/json");

// For Android Studio
// $value = json_decode(file_get_contents('php://input'));
// $id = $value->id;

// For Postman
if (!empty($_POST)) {

    $id = $_POST['id'];
} else {
    $id = -1;
}

$query = $connection->prepare("SELECT * FROM `task` WHERE `id`= ?");
$query->bind_param("i", $id);
$query->execute();

$result = $query->get_result();
$data = $result->fetch_assoc();

if (!empty($data)) {
    $object = array(
        'id' => $data['id'],
        'username' => $data['username'],
        'title' => $data['title'],
        'description' => $data['description'],
        'category' => $data['category'],
        'due_date' => $data['due_date'],
        'time' => $data['time'],
        'created' => $data['created'],
        'updated' => $data['updated']
    );

    $response['task'] = $object;
} else {
    $response['message'] = "Data not found";
}

$query->close();
$connection->close();

echo json_encode($response);
?>