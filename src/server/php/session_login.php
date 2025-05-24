<?php
header("Content-Type: application/json");
define('ACCESS_ALLOWED', true);
require_once "../php/conn.php";

    $token = ";;mslaundryshop2025;;";

    if($_SERVER['REQUEST_METHOD'] !== 'POST' || !isset($_POST['email']) || !isset($_POST['password']) || !isset($_POST['role']) || !isset($_POST['branch']) || !isset($_POST['client_fetch_token_request']) || $_POST['client_fetch_token_request'] !== $token) {
        echo json_encode(["response" => false]);
        exit;
    } else {
        $email = $_POST['email'] ?? null;
        $pass = $_POST['password'] ?? null;
        $role = $_POST['role'] ?? null;
        $branch = $_POST['branch'] ?? null;
        $stmt = $conn->prepare("SELECT * FROM user_credentials WHERE email = ? AND password= ? AND role = ? AND branch = ?");
        $stmt->bind_param("ssss", $email, $pass, $role, $branch);
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