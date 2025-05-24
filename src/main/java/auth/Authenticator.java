package src.main.java.auth;

import java.awt.Color;
import java.awt.Component;
import java.io.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.JSONObject;

import src.main.java.auth.session.java.FetchUser;
import src.server.java.ServerURL;
import src.utils.CustomBorder;

public class Authenticator extends ServerURL implements CustomBorder {

    CookieManager cookieManager = new CookieManager();

    public Authenticator() {
        CookieHandler.setDefault(cookieManager);
    }


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    protected String[] loadedUserSession = readUserSessionFromFile("./src/main/java/auth/session/user.txt");

    public String login(String email, String password, Component[] component) {
        Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        JComponent emailField = (JComponent) component[0];
        JComponent passwordField = (JComponent) component[1];
        Component loginButton = component[2];

        disableComponent(loginButton);

        if (email.isEmpty()) {
            setInputBorderColor(emailField, "Email address", Color.RED);
            enableComponent(loginButton);
            return "";
        }

        if (!emailMatcher.matches()) {
            setInputBorderColor(emailField, "Email address", Color.RED);
            showError("Please enter your email address correctly.", "Invalid email");
            enableComponent(loginButton);
            return "";
        }

        if (password.isEmpty()) {
            setInputBorderColor(emailField, "Email address", Color.BLACK);
            setInputBorderColor(passwordField, "Password", Color.RED);
            enableComponent(loginButton);
            return "";
        }

        setInputBorderColor(emailField, "Email address", Color.BLACK);
        setInputBorderColor(passwordField, "Password", Color.BLACK);

        try {
            String postData = "email=" + email + "&password=" + password + "&client_fetch_token_request=;;mslaundryshop2025;;";
            HttpURLConnection conn = createPostConnection("login.php", postData);
            JSONObject response = parseJsonResponse(conn);

            if (response != null && response.getBoolean("response") == true) {
                FetchUser.setUserData(response);
                saveUserSessionToFile(response.getString("email"), response.getString("password"), response.getString("role"), response.getString("branch"));
                enableComponent(loginButton);
                return response.getString("role");
            } else {
                setInputBorderColor(passwordField, "Password", Color.RED);
                showError("Incorrect email or password.", "Invalid credentials");
                ((javax.swing.text.JTextComponent) passwordField).setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Something went wrong. Try again later.", "Error");
        }

        enableComponent(loginButton);
        return "";
    }



    public void requestApproval(String firstname, String lastname, String email, String password, String cPassword, String branch, String gender, Component[] component) {
        Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        JComponent fnameField = (JComponent) component[0];
        JComponent lnameField = (JComponent) component[1];
        JComponent emailField = (JComponent) component[2];
        JComponent passwordField = (JComponent) component[3];
        JComponent cPasswordField = (JComponent) component[4];
        JComponent branchField = (JComponent) component[5];
        JComponent genderField = (JComponent) component[6];
        Component requestBtn = component[7];
        Component loginBtn = component[8];

        disableComponent(requestBtn);

        // Reset all input borders to default
        setInputBorderColor(fnameField, "Firstname", Color.BLACK);
        setInputBorderColor(lnameField, "Lastname", Color.BLACK);
        setInputBorderColor(emailField, "Email address (Contact)", Color.BLACK);
        setInputBorderColor(passwordField, "Password", Color.BLACK);
        setInputBorderColor(cPasswordField, "Confirm password", Color.BLACK);
        setInputBorderColor(branchField, "Branch", Color.BLACK);
        setInputBorderColor(genderField, "Gender", Color.BLACK);

        // Validate each input with early return
        if (firstname.isEmpty()) {
            setInputBorderColor(fnameField, "Firstname", Color.RED);
            showError("Please enter your firstname.", "Missing field");
            enableComponent(requestBtn);
            return;
        }
        if (lastname.isEmpty()) {
            setInputBorderColor(lnameField, "Lastname", Color.RED);
            showError("Please enter your lastname.", "Missing field");
            enableComponent(requestBtn);
            return;
        }
        if (email.isEmpty()) {
            setInputBorderColor(emailField, "Email address (Contact)", Color.RED);
            showError("Please enter your email address.", "Invalid email");
            enableComponent(requestBtn);
            return;
        }
        if (!emailMatcher.matches()) {
            setInputBorderColor(emailField, "Email address (Contact)", Color.RED);
            showError("Please enter your email address correctly.", "Invalid email");
            enableComponent(requestBtn);
            return;
        }
        if (password.isEmpty()) {
            setInputBorderColor(passwordField, "Password", Color.RED);
            showError("Please enter your password.", "Missing field");
            enableComponent(requestBtn);
            return;
        }
        if (cPassword.isEmpty()) {
            setInputBorderColor(cPasswordField, "Confirm password", Color.RED);
            showError("Please confirm your password.", "Missing field");
            enableComponent(requestBtn);
            return;
        }
        if (!password.equals(cPassword)) {
            setInputBorderColor(cPasswordField, "Confirm password", Color.RED);
            showError("Passwords do not match. Try again.", "Mismatch");
            enableComponent(requestBtn);
            return;
        }
        if (branch.equals("Select branch")) {
            setInputBorderColor(branchField, "Select branch", Color.RED);
            showError("Please select your branch.", "Missing branch");
            enableComponent(requestBtn);
            return;
        }
        if (gender.equals("Select your gender")) {
            setInputBorderColor(genderField, "Select gender", Color.RED);
            showError("Please select your gender.", "Missing gender");
            enableComponent(requestBtn);
            return;
        }

        disableInputs(new Component[] {
            fnameField, lnameField, emailField, passwordField, cPasswordField, branchField, loginBtn
        });

        String postData = "&action=send&client_fetch_token_request=;;mslaundryshop2025;;&fname=" + firstname + "&lname=" + lastname + "&email=" + email + "&gender=" + gender + "&branch=" + branch;
        HttpURLConnection conn = createPostConnection("email_verification.php", postData);
        JSONObject response = parseJsonResponse(conn);

        if (response != null && response.optBoolean("response")) {
            while (true) {
                String code = JOptionPane.showInputDialog(null, "Enter the verification code sent to your email:", "Email Verification", JOptionPane.QUESTION_MESSAGE);

                if (code == null) {
                    showInfo("Verification canceled.");
                    resetFormFields(fnameField, lnameField, emailField, passwordField, cPasswordField, branchField, genderField);
                    enableInputs(new Component[] {
                        fnameField, lnameField, emailField, passwordField, cPasswordField, branchField, loginBtn, requestBtn
                    });
                    return;
                }

                String verifyData = "&action=verify&client_fetch_token_request=;;mslaundryshop2025;;&code=" + code + "&fname=" + firstname + "&lname=" + lastname + "&email=" + email + "&password=" + password + "&role=employee&branch=" + branch + "&gender=" + gender;
                HttpURLConnection verifyConn = createPostConnection("email_verification.php", verifyData);
                JSONObject verifyResponse = parseJsonResponse(verifyConn);

                if (verifyResponse != null && verifyResponse.optBoolean("response")) {
                    showInfo("Verification successful!");
                    resetFormFields(fnameField, lnameField, emailField, passwordField, cPasswordField, branchField, genderField);
                    enableInputs(new Component[] {
                        fnameField, lnameField, emailField, passwordField, cPasswordField, branchField, loginBtn, requestBtn
                    });
                    return;
                } else {
                    showError("Incorrect code. Try again.", "Error");
                }
            }
        } else if (response != null && response.optBoolean("exist")) {
            setInputBorderColor(emailField, "Email address (Contact)", Color.RED);
            showError(response.optString("error", "Email already exists."), "Invalid email");
        } else {
            showError("Something went wrong. Try again later.", "Error");
        }

        enableInputs(new Component[] {
            fnameField, lnameField, emailField, passwordField, cPasswordField, branchField, loginBtn, requestBtn
        });
    }


    private void resetFormFields(JComponent fname, JComponent lname, JComponent email, JComponent pass, JComponent cpass, JComponent branch, JComponent gender) {
        ((JTextField) fname).setText("");
        ((JTextField) lname).setText("");
        ((JTextField) email).setText("");
        ((JPasswordField) pass).setText("");
        ((JPasswordField) cpass).setText("");
        ((JComboBox<?>) branch).setSelectedIndex(0);
        ((JComboBox<?>) gender).setSelectedIndex(0);
    }

    private void enableInputs(Component[] components) {
        for (Component c : components) enableComponent(c);
    }

    private void disableInputs(Component[] components) {
        for (Component c : components) disableComponent(c);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message);
    }




    public final String isAuthenticated() {
        if (loadedUserSession != null && loadedUserSession.length >= 4) {
            try {
                String postData = "email=" + loadedUserSession[0] +
                                "&password=" + loadedUserSession[1] +
                                "&role=" + loadedUserSession[2] +
                                "&branch=" + loadedUserSession[3] +
                                "&client_fetch_token_request=;;mslaundryshop2025;;";

                HttpURLConnection conn = createPostConnection("session_login.php", postData);
                JSONObject response = parseJsonResponse(conn);

                if (response != null && response.has("response") && response.getBoolean("response") == true) {
                    FetchUser.setUserData(response);
                    return loadedUserSession[2];

                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Something went wrong. Try again later.", "Error");
            }
        }
        return null;
    }


    public final void logout() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/java/auth/session/user.txt", false))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /* ------------------ Helpers ------------------ */

    private static void saveUserSessionToFile(String email, String password, String role, String branch) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/java/auth/session/user.txt", false))) {
            writer.write(email + "," + password + "," + role + "," + branch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] readUserSessionFromFile(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return (line != null) ? line.split(",") : null;
        } catch (IOException e) {
            return null;
        }
    }

    private JSONObject parseJsonResponse(HttpURLConnection conn) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        StringBuilder responseBuilder = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            responseBuilder.append(inputLine);
        }

        return new JSONObject(responseBuilder.toString());

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}


    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void enableComponent(Component c) {
        c.setEnabled(true);
    }

    private void disableComponent(Component c) {
        c.setEnabled(false);
    }
}
