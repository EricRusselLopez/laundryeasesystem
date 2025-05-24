<?php
header("Content-Type: application/json");
define('ACCESS_ALLOWED', true);
require_once "../php/conn.php";

    $token = ";;mslaundryshop2025;;";

    if($_SERVER['REQUEST_METHOD'] !== 'POST' || !isset($_POST['email']) || !isset($_POST['password']) || !isset($_POST['client_fetch_token_request']) || $_POST['client_fetch_token_request'] !== $token) {
        echo json_encode(["response" => "Unauthorized access"]);
        exit;
    } else {
        $email = $_POST['email'] ?? null;
        $pass = $_POST['password'] ?? null;
        $stmt = $conn->prepare("SELECT * FROM user_credentials WHERE email = ? AND password= ? ");
        $stmt->bind_param("ss", $email, $pass);
        $stmt->execute();
        $result = $stmt->get_result();
        if($result->num_rows === 1) {
            $data = $result->fetch_assoc();
            echo json_encode(array_merge(["response" => true], $data));
        } else {
            echo json_encode(["response" => false]);
        }

        $stmt->close();
        $conn->close();
    }

?>