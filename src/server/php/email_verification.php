<?php 
header("Content-Type: application/json");
session_start();
define('ACCESS_ALLOWED', true);
require_once "../php/conn.php";

require 'PHPMailer-master/src/PHPMailer.php';
require 'PHPMailer-master/src/SMTP.php';
require 'PHPMailer-master/src/Exception.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

$token = ";;mslaundryshop2025;;";
$action = $_POST['action'] ?? '';

// ============ SEND VERIFICATION CODE ============
if ($_SERVER["REQUEST_METHOD"] === "POST" && $action === 'send') {
    if (
        !isset($_POST['client_fetch_token_request']) || $_POST['client_fetch_token_request'] !== $token ||
        !isset($_POST['email']) || !isset($_POST['fname']) || !isset($_POST['lname']) || !isset($_POST['gender']) || !isset($_POST['branch'])
    ) {
        echo json_encode(["response" => false]);
        exit;
    }

    $email = $_POST['email'];
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $gender = $_POST['gender'];
    $branch = $_POST['branch'];

    $stmt_email_exist = $conn->prepare("
        SELECT email FROM approvals WHERE email = ? AND branch = ?
        UNION
        SELECT email FROM user_credentials WHERE email = ? AND branch = ?
    ");
    $stmt_email_exist->bind_param("ssss", $email, $branch, $email, $branch);
    $stmt_email_exist->execute();
    $result = $stmt_email_exist->get_result();
    if($result->num_rows > 0) {
        echo json_encode(["response" => false, "error" => "An account with this email has either already been created or is pending approval.", "exist" => true]);
    } else {
        try {
            $code = rand(100000, 999999);
            $_SESSION['email_code'] = $code;
            $_SESSION['email_to_verify'] = $email;

            $mail = new PHPMailer(true);
            $mail->isSMTP();
            $mail->Host       = 'smtp.gmail.com';
            $mail->SMTPAuth   = true;
            $mail->Username   = 'ghostzyroyt2026@gmail.com';
            $mail->Password   = 'poeo knhm ojva kcbl';
            $mail->SMTPSecure = 'tls';
            $mail->Port       = 587;

            $mail->setFrom('ghostzyroyt2026@gmail.com', 'Ms. Clean Laundry Ease');
            $mail->addAddress($email, "$fname $lname");
            $mail->isHTML(true);
            $mail->Subject = 'Please Verify Your Email';
            $mail->Body = 'Hello <b>' . (($gender === "Male") ? "Mr. " : "Ms. ") . $fname . ' ' . $lname . '</b>. To verify your email, please enter this code: <b>' . $code . '</b>';


            $mail->send();
            echo json_encode(["response" => true]);
        } catch (Exception $e) {
            echo json_encode(["response" => false, "error" => $mail->ErrorInfo]);
        }
    }
}

// ============ VERIFY CODE ============
else if ($_SERVER["REQUEST_METHOD"] === "POST" && $action === 'verify') {
    if (
        !isset($_POST['client_fetch_token_request']) || $_POST['client_fetch_token_request'] !== $token ||
        !isset($_POST['code']) || !isset($_SESSION['email_code'])
    ) {
        echo json_encode(["response" => false]);
        exit;
    }

    $code = $_POST['code'];
    if ($code == $_SESSION['email_code'] && isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['role']) && isset($_POST['branch'])) {

        $fname = $_POST['fname'] ?? null;
        $lname = $_POST['lname'] ?? null;
        $email = $_POST['email'] ?? null;
        $pass = $_POST['password'] ?? null;
        $role = $_POST['role'] ?? null;
        $branch = $_POST['branch'] ?? null;
        $gender = $_POST['gender'] ?? null;
        $status = "requested";
        $hashedPassword = password_hash($pass, PASSWORD_DEFAULT);
        $stmt = $conn->prepare("INSERT INTO approvals (firstname, lastname, email, password, role, branch, gender, status) VALUES (?,?,?,?,?,?,?,?)");
        $stmt->bind_param("ssssssss", $fname, $lname, $email, $hashedPassword, $role, $branch, $gender, $status);
        if($stmt->execute()) {
            unset($_SESSION['email_code']);
            echo json_encode(["response" => true]);
        } else {
            echo json_encode(["response" => false]);
        }
    } else {
        echo json_encode(["response" => false]);
    }
}
