<?php
include 'db.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST['id'];
    $title = $_POST['title'];
    $author = $_POST['author'];
    $publisher = $_POST['publisher'];
    $genre = $_POST['genre'];
    $published_date = $_POST['publishedDate'];
    $status = $_POST['status'];

    $query = "UPDATE books SET 
              title='$title', author='$author', publisher='$publisher', 
              genre='$genre', published_date='$published_date', status='$status' 
              WHERE id=$id";

    if (mysqli_query($conn, $query)) {
        echo 'Book updated successfully';
    } else {
        echo 'Error: ' . mysqli_error($conn);
    }
}
?>
