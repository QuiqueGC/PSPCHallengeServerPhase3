package j_dialogs;

import listeners.LoginDialogListener;
import utils.SpellBook;

import javax.swing.*;

public class LoginDialog extends JDialog {

    private final JPanel CONTENT_PANE;
    private JTextField nameField;
    private JPasswordField passwdField;

    public LoginDialog() {


        SpellBook.creatingStandardJDialog("Login", this);

        CONTENT_PANE = SpellBook.creatingStandardPanelForDialog();

        textFieldsCreation();

        buttonCreation();

        setContentPane(CONTENT_PANE);
    }

    private void buttonCreation() {
        JButton button = new JButton();

        button.setSize(100, 20);
        button.setText("Entrar");
        button.setLocation(CONTENT_PANE.getWidth() / 2 - button.getWidth() / 2, 120);
        button.addMouseListener(new LoginDialogListener(nameField, passwdField, this));
        CONTENT_PANE.add(button);

    }

    private void textFieldsCreation() {

        nameCreation();

        passwdCreation();

    }

    private void nameCreation() {
        JLabel labelTxt = SpellBook.textLabelCreation(CONTENT_PANE, "Nombre", 20, 20);
        nameField = SpellBook.txtFieldCreation(CONTENT_PANE, labelTxt.getX() + labelTxt.getWidth(), 20);
    }


    private void passwdCreation() {
        JLabel labelTxt = SpellBook.textLabelCreation(CONTENT_PANE, "Password", 20, 50);
        passwdField = SpellBook.passwdFieldCreation(CONTENT_PANE, labelTxt.getX() + labelTxt.getWidth(), 50);

    }
}
