<?php
include 'db.php';

if (isset($_POST['id'])) {
    $id = $_POST['id'];
    
    $query = "DELETE FROM books WHERE id=$id";

    if (mysqli_query($conn, $query)) {
        echo 'Book deleted successfully';
    } else {
        echo 'Error: ' . mysqli_error($conn);
    }
}
?>
