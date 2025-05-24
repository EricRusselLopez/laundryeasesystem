package src.main.java.auth;

import java.awt.Component;
import java.awt.event.ActionEvent;
import src.main.java.App;

public class Login extends javax.swing.JPanel {

    private App app;

    Authenticator authenticator = new Authenticator();

    public Login(App app) {
        this.app = app;
        initComponents();
    }


        private void initComponents() {                          

        jLabel1 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        loginBtn = new javax.swing.JButton();
        signupBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        forgotpasswordBtn = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();


        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Login Or Signup");

        emailField.setToolTipText(" Your registerd email");
        emailField.setBorder(javax.swing.BorderFactory.createTitledBorder("Email address"));
        emailField.setOpaque(false);

        loginBtn.setBackground(new java.awt.Color(153, 255, 255));
        loginBtn.setFont(new java.awt.Font("Segoe UI", 1, 14));
        loginBtn.setForeground(new java.awt.Color(0, 0, 0));
        loginBtn.setText("Login");
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener((ActionEvent e) -> {
            String role = authenticator.login(
                emailField.getText(),
                new String(passwordField.getPassword()),
                new Component[]{emailField, passwordField, loginBtn}
            );

            switch (role) {
                case "employee" -> app.showEmployee();
                case "owner" -> app.showOwner();
                default -> {
                }
            }
        });

        signupBtn.setText("Don't have an account? Signup here!");
        signupBtn.setFocusPainted(false);
        signupBtn.addActionListener(e -> app.showRegister());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Or");

        forgotpasswordBtn.setFont(new java.awt.Font("Segoe UI", 2, 14));
        forgotpasswordBtn.setForeground(new java.awt.Color(51, 102, 255));
        forgotpasswordBtn.setText("Forgot password?");
        forgotpasswordBtn.setBorderPainted(false);
        forgotpasswordBtn.setContentAreaFilled(false);
        forgotpasswordBtn.setFocusPainted(false);
        forgotpasswordBtn.setMargin(new java.awt.Insets(2, 14, 3, 5));
        forgotpasswordBtn.setOpaque(false);

        passwordField.setBorder(javax.swing.BorderFactory.createTitledBorder("Password"));
        passwordField.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(emailField)
            .addComponent(passwordField)
            .addComponent(loginBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(forgotpasswordBtn))
            .addComponent(signupBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forgotpasswordBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }                        

          
    private javax.swing.JButton loginBtn;
    private javax.swing.JButton signupBtn;
    private javax.swing.JButton forgotpasswordBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField emailField;              
}
