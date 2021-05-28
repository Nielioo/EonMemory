<?php

require_once("db_controller.php");
header("Content-Type: application/json");

$query = $connection->query("SELECT * FROM `task`");

$response['count'] = $query->num_rows;
$response['task'] = array();

while ($data = $query->fetch_assoc()) {
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

    array_push($response['task'], $object);
}

$query->close();
$connection->close();

echo json_encode($response);
?>