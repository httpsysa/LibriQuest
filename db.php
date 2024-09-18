<?php
$conn = mysqli_connect('localhost', 'root', '', 'bookapp');

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
?>
