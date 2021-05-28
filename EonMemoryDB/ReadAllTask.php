<?php

require_once("db_controller.php");
header("Content-Type: application/json");

$query = $connection->query("SELECT * FROM `barang`");

$response['count'] = $query->num_rows;
$response['barang'] = array();

while ($data = $query->fetch_assoc()) {
    $object = array(
        'id' => $data['id'],
        'nama' => $data['nama'],
        'image_path' => $data['image_path'],
        'jumlah' => $data['jumlah'],
        'created' => $data['created']
    );

    array_push($response['barang'], $object);
}

$query->close();
$connection->close();

echo json_encode($response);
?>