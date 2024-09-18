<?php
include 'db.php';

$query = "SELECT * FROM books";
$result = mysqli_query($conn, $query);

$books = array();
while ($row = mysqli_fetch_assoc($result)) {
    $books[] = $row;
}

echo json_encode($books);
?>
