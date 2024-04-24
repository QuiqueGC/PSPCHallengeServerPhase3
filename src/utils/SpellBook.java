package utils;

import data_classes.User;
import j_panels.PanelAdmin;
import p_s_p_challenge.PSPChallenge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Almacena métodos diversos para usar a lo largo de la aplicación
 */
public abstract class SpellBook {

    public static JPasswordField passwdFieldCreation(JPanel contentPane, int xPosition, int yPosition) {
        JPasswordField passwdField = new JPasswordField();

        passwdField.setSize(150, 20);
        passwdField.setLocation(
                xPosition,
                yPosition);
        contentPane.add(passwdField);

        return passwdField;
    }

    public static JLabel textLabelCreation(JPanel contentPane, String text, int xPosition, int yPosition) {
        JLabel labelTxt = new JLabel();
        labelTxt.setText(text);
        labelTxt.setForeground(Color.WHITE);
        labelTxt.setSize(160, 20);
        labelTxt.setLocation(
                xPosition,
                yPosition);
        contentPane.add(labelTxt);

        return labelTxt;
    }

    public static JTextField txtFieldCreation(JPanel contentPane, int xPosition, int yPosition) {
        JTextField txtField = new JTextField();
        txtField.setSize(150, 20);
        txtField.setLocation(
                xPosition,
                yPosition);
        contentPane.add(txtField);

        return txtField;
    }


    public static void creatingStandardPanelForFrame(JPanel contentPane) {

        contentPane.setPreferredSize(new Dimension(640, 480));
        contentPane.setSize(new Dimension(640, 480));
        contentPane.setBackground(new Color(60, 61, 59));

        contentPane.setLayout(null);
    }


    public static void creatingStandardJDialog(String tittle, JDialog jDialog) {

        jDialog.setTitle(tittle);
        jDialog.setSize(380, 240);
        jDialog.setPreferredSize(jDialog.getSize());
        jDialog.setLocationRelativeTo(null);

        Toolkit pantalla = Toolkit.getDefaultToolkit();
        Image icon = pantalla.getImage("src/resources/img/server.png");
        jDialog.setIconImage(icon);

        jDialog.setModal(true);

    }

    public static JPanel creatingStandardPanelForDialog() {
        JPanel contentPane = new JPanel();

        contentPane.setPreferredSize(new Dimension(380, 240));
        contentPane.setSize(new Dimension(380, 240));
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(60, 61, 59));

        return contentPane;
    }

    public static void addingBackButton(JPanel actualPanel, JPanel panelAdmin) {
        JButton backButton = new JButton();

        backButton.setSize(200, 50);
        backButton.setText("Volver");
        backButton.setLocation(actualPanel.getWidth() / 2 - backButton.getWidth() / 2, 360);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                PSPChallenge.frame.setContentPane(panelAdmin);
            }
        });
        actualPanel.add(backButton);
    }


    public static boolean checkingIfUserExist(String name) {
        boolean alreadyExist = false;
        for (User user :
                PSPChallenge.usersList) {

            if (user.getName().toUpperCase().trim().equals(name.toUpperCase())) {
                alreadyExist = true;
            }
        }
        return alreadyExist;
    }

    public static void creatingNewUser(String name, String passwd, int userType) {

        passwd = BlowFishManager.encryptingPasswd(passwd);

        User user = new User(name, passwd, userType);

        FilesRW.addingNewUser(user);

        PSPChallenge.usersList.add(user);
    }


    public static User lookingForUser(String name) {
        User foundUser = null;
        for (User user :
                PSPChallenge.usersList) {

            if (user.getName().toUpperCase().trim().equals(name.toUpperCase())) {

                foundUser = user;
            }
        }
        return foundUser;
    }

    public static String loginClient(String passwd, User foundUser) {
        String response = "Login realizado con éxito";
        if (foundUser.getUserType() == 2) {

            if (!BlowFishManager.checkingPasswd(passwd, foundUser)) {
                response = "La contraseña no coincide con la del usuario.";
            }

        } else {
            response = "Este usuario no tiene permiso para entrar en la aplicación";
        }
        return response;
    }



}
