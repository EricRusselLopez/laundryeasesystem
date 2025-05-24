package src.main.java.components;

import src.main.java.App;
import src.main.java.auth.Authenticator;

import javax.swing.*;

import org.json.JSONObject;

import src.main.java.auth.session.java.FetchUser;

public class Employee extends JPanel {

    JSONObject user = FetchUser.getUserData();

    private App app;

    public Employee(App app) {

        this.app = app;

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new Authenticator().logout();
            app.showLogin();
        });
        add(new JLabel("Welcome, " + user.getString("firstname") + " " + user.getString("lastname") + " our " + user.getString("role") + "!"));
        add(logoutBtn);
    }
}
