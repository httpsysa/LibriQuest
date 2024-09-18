<?php
include 'db.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $title = $_POST['title'];
    $author = $_POST['author'];
    $publisher = $_POST['publisher'];
    $genre = $_POST['genre'];
    $published_date = $_POST['publishedDate'];
    $status = $_POST['status'];

    $query = "INSERT INTO books (title, author, publisher, genre, published_date, status) 
              VALUES ('$title', '$author', '$publisher', '$genre', '$published_date', '$status')";

    if (mysqli_query($conn, $query)) {
        echo 'Book added successfully';
    } else {
        echo 'Error: ' . mysqli_error($conn);
    }
}
?>
