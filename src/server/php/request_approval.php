<?php
header("Content-Type: application/json");
define('ACCESS_ALLOWED', true);
require_once "../php/conn.php";

    $token = ";;mslaundryshop2025;;";

    if($_SERVER['REQUEST_METHOD'] !== 'POST' || !isset($_POST['firstname']) || !isset($_POST['lastname']) || !isset($_POST['email']) || !isset($_POST['password']) || !isset($_POST['role']) || !isset($_POST['branch']) || !isset($_POST['client_fetch_token_request']) || $_POST['client_fetch_token_request'] !== $token) {
        echo json_encode(["response" => false]);
        exit;
    } else {
        $fname = $_POST['firstname'] ?? null;
        $lname = $_POST['lastname'] ?? null;
        $email = $_POST['email'] ?? null;
        $pass = $_POST['password'] ?? null;
        $role = $_POST['role'] ?? null;
        $branch = $_POST['branch'] ?? null;
        $status = "requested";
        $stmt = $conn->prepare("INSERT INTO approvals (firstname, lastname, email, password, role, branch, status) VALUES (?,?,?,?,?,?,?)");
        $stmt->bind_param("sssssss", $fname, $lname, $email, $pass, $role, $branch, $status);
        if($stmt->execute()) {
            echo json_encode(["response" => true]);
        } else {
            echo json_encode(["response" => false]);
        }

        $stmt->close();
        $conn->close();
    }

?>